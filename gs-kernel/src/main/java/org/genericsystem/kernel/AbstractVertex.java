package org.genericsystem.kernel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.genericsystem.api.core.ISignature;
import org.genericsystem.api.core.IVertex;
import org.genericsystem.api.core.Snapshot;
import org.genericsystem.api.exception.AliveConstraintViolationException;
import org.genericsystem.api.exception.AmbiguousSelectionException;
import org.genericsystem.api.exception.ConsistencyConstraintViolationException;
import org.genericsystem.api.exception.ConstraintViolationException;
import org.genericsystem.api.exception.CrossEnginesAssignementsException;
import org.genericsystem.api.exception.ExistsException;
import org.genericsystem.api.exception.NotFoundException;
import org.genericsystem.api.exception.ReferentialIntegrityConstraintViolationException;
import org.genericsystem.kernel.Dependencies.DependenciesEntry;
import org.genericsystem.kernel.Statics.Supers;
import org.genericsystem.kernel.systemproperty.AxedPropertyClass;
import org.genericsystem.kernel.systemproperty.constraints.Constraint;
import org.genericsystem.kernel.systemproperty.constraints.Constraint.CheckingType;

public abstract class AbstractVertex<T extends AbstractVertex<T, U>, U extends DefaultRoot<T, U>> implements DefaultVertex<T, U> {

	private T meta;
	private List<T> composites;
	private Serializable value;
	private boolean throwExistException;

	@SuppressWarnings("unchecked")
	protected T init(boolean throwExistException, T meta, Serializable value, List<T> composites) {
		this.throwExistException = throwExistException;
		if (meta != null) {
			meta.checkIsAlive();
			this.meta = meta;
		} else
			this.meta = (T) this;
		this.value = value;
		this.composites = new ArrayList<>(composites);
		for (int i = 0; i < composites.size(); i++) {
			T composite = composites.get(i);
			if (composite != null) {
				composite.checkIsAlive();
				this.composites.set(i, composite);
			} else
				this.composites.set(i, (T) this);
		}
		return (T) this;
	}

	public boolean isThrowExistException() {
		return throwExistException;
	}

	@Override
	public T getMeta() {
		return meta;
	}

	@Override
	public List<T> getComposites() {
		return composites;
	}

	@Override
	public Serializable getValue() {
		return value;
	}

	@Override
	public String toString() {
		return Objects.toString(getValue());
	}

	@Override
	public int getLevel() {
		return (isRoot() || composites.stream().allMatch(c -> c.isRoot()) && Objects.equals(getValue(), getRoot().getValue())) ? 0 : meta.getLevel() + 1;
	}

	protected List<T> supers;

	protected abstract Dependencies<T> getInstancesDependencies();

	protected abstract Dependencies<T> getInheritingsDependencies();

	protected abstract Dependencies<DependenciesEntry<T>> getMetaComponentsDependencies();

	protected abstract Dependencies<DependenciesEntry<T>> getSuperComponentsDependencies();

	@SuppressWarnings("unchecked")
	protected T init(boolean throwExistException, T meta, List<T> supers, Serializable value, List<T> components) {
		init(throwExistException, meta, value, components);
		this.supers = supers;
		return (T) this;
	}

	protected T newT(Class<?> clazz, boolean throwExistException, T meta, List<T> supers, Serializable value, List<T> components) {
		return newT(clazz).init(throwExistException, meta, supers, value, components);
	}

	protected T newT(Class<?> clazz) {
		return newT();
	}

	@Override
	public List<T> getSupers() {
		return supers;
	}

	@SuppressWarnings("static-method")
	protected <H> Dependencies<H> buildDependencies() {
		return new DependenciesImpl<>();
	}

	protected void forceRemove() {
		computeDependencies().forEach(this::simpleRemove);
	}

	private void simpleRemove(T vertex) {
		if (!vertex.isAlive())
			getRoot().discardWithException(new AliveConstraintViolationException(vertex.info() + " is not alive"));
		if (!vertex.getInstances().isEmpty() || !vertex.getInheritings().isEmpty() || !vertex.getComponents().isEmpty())
			getRoot().discardWithException(new ReferentialIntegrityConstraintViolationException(vertex.info() + " has dependencies"));
		vertex.unplug();
	}

	private Iterable<T> reverseLinkedHashSet(LinkedHashSet<T> linkedHashSet) {
		List<T> dependencies = new ArrayList<>(linkedHashSet);
		Collections.reverse(dependencies);
		return dependencies;
	}

	private Iterable<T> getOrderedDependenciesToRemove() {
		return reverseLinkedHashSet(buildOrderedDependenciesToRemove());
	}

	@Override
	public void remove() {
		getOrderedDependenciesToRemove().forEach(x -> simpleRemove(x));
	}

	final T update(List<T> supersToAdd, Serializable newValue, List<T> newComposites) {
		if (newComposites.size() != getComposites().size())
			getRoot().discardWithException(new IllegalArgumentException());
		return rebuildAll(() -> getMeta().bindInstance(null, isThrowExistException(), new Supers<>(getSupers(), supersToAdd), newValue, newComposites), computeDependencies());
	}

	private static class ConvertMap<T extends AbstractVertex<T, U>, U extends DefaultRoot<T, U>> extends HashMap<T, T> {
		private static final long serialVersionUID = 5003546962293036021L;

		T convert(T dependency) {
			if (dependency.isAlive())// KK ?
				return dependency;
			T newDependency = get(dependency);
			if (newDependency == null) {
				T meta = (dependency.isRoot()) ? dependency : convert(dependency.getMeta());
				List<T> composites = dependency.getComposites().stream().map(x -> x.equals(this) ? null : convert(x)).collect(Collectors.toList());
				meta = meta.adjustMeta(dependency.getValue(), composites);
				newDependency = meta.buildInstance(null, dependency.isThrowExistException(), dependency.getSupers().stream().map(x -> convert(x)).collect(Collectors.toList()), dependency.getValue(), composites).plug();
				put(dependency, newDependency);
			}
			return newDependency;
		}
	}

	protected LinkedHashSet<T> computeDependencies() {
		return new DependenciesComputer<T, U>() {
			private static final long serialVersionUID = 4116681784718071815L;

			@Override
			boolean checkDependency(T node) {
				return isAncestorOf(node);
			}
		}.visit(getMeta());
	}

	@SuppressWarnings("unchecked")
	private LinkedHashSet<T> buildOrderedDependenciesToRemove() {
		return new LinkedHashSet<T>() {
			private static final long serialVersionUID = -3610035019789480505L;
			{
				visit((T) AbstractVertex.this);
			}

			// TODO clean
			public void visit(T generic) {
				if (add(generic)) {// protect from loop
					if (!generic.getInheritings().isEmpty() || !generic.getInstances().isEmpty())
						getRoot().discardWithException(new ReferentialIntegrityConstraintViolationException("Ancestor : " + generic + " has an inheritance or instance dependency"));

					for (T component : generic.getComponents())
						if (!generic.equals(component)) {
							for (int compositePos = 0; compositePos < component.getComposites().size(); compositePos++)
								if (/* !compositeDependency.isAutomatic() && */component.getComposites().get(compositePos).equals(generic) && !contains(component) && component.isReferentialIntegrityEnabled(compositePos))
									getRoot().discardWithException(new ReferentialIntegrityConstraintViolationException(component + " is Referential Integrity for ancestor " + generic + " by component position : " + compositePos));
							visit(component);
						}
					for (int axe = 0; axe < generic.getComposites().size(); axe++)
						if (generic.isCascadeRemove(axe))
							visit(generic.getComposites().get(axe));
				}
			}
		};
	}

	@SuppressWarnings("unchecked")
	protected LinkedHashSet<T> computePotentialDependencies(List<T> overrides, Serializable value, List<T> composites) {
		return new DependenciesComputer<T, U>() {
			private static final long serialVersionUID = -3611136800445783634L;

			@Override
			boolean checkDependency(T node) {
				return node.dependsFrom((T) AbstractVertex.this, overrides, value, composites);
			}
		}.visit((T) this);
	}

	public T adjustMeta(Serializable value, @SuppressWarnings("unchecked") T... composites) {
		return adjustMeta(value, Arrays.asList(composites));
	}

	@SuppressWarnings("unchecked")
	T adjustMeta(Serializable value, List<T> composites) {
		T result = null;
		for (T directInheriting : getInheritings()) {
			if (isAdjusted(directInheriting, value, composites)) {
				if (result == null)
					result = directInheriting;
				else
					getRoot().discardWithException(new AmbiguousSelectionException("Ambigous selection : " + result.info() + directInheriting.info()));
			}
		}
		return result == null ? (T) this : result.adjustMeta(value, composites);
	}

	boolean isAdjusted(T directInheriting, Serializable value, List<T> composites) {
		return !composites.equals(getComposites()) && !directInheriting.equalsRegardlessSupers(this, value, composites)/* && Objects.equals(getValue(), directInheriting.getValue()) */
				&& compositesDepends(composites, directInheriting.getComposites());
	}

	// TODO KK if a composite is null
	T getDirectInstance(Serializable value, List<T> composites) {
		for (T instance : getInstances())
			if (((AbstractVertex<?, ?>) instance).equalsRegardlessSupers(this, value, composites))
				return instance;
		return null;
	}

	T getDirectInstance(List<T> overrides, Serializable value, List<T> composites) {
		T result = getDirectInstance(value, composites);
		return result != null && Statics.areOverridesReached(overrides, result.getSupers()) ? result : null;
	}

	// TODO KK should be protected
	public final T bindInstance(Class<?> clazz, boolean throwExistException, List<T> overrides, Serializable value, List<T> composites) {
		checkSameEngine(composites);
		checkSameEngine(overrides);
		T adjustedMeta = adjustMeta(value, composites);
		if (!throwExistException) {
			T equivInstance = adjustedMeta.getDirectEquivInstance(value, composites);
			if (equivInstance != null)
				return equivInstance.equalsRegardlessSupers(adjustedMeta, value, composites) && Statics.areOverridesReached(overrides, equivInstance.getSupers()) ? equivInstance : equivInstance.update(overrides, value, composites);
		} else {
			T equivInstance = adjustedMeta.getDirectInstance(value, composites);
			if (equivInstance != null)
				getRoot().discardWithException(new ExistsException("An equivalent instance already exists : " + equivInstance.info()));
		}
		return rebuildAll(() -> adjustedMeta.buildInstance(clazz, throwExistException, overrides, value, composites).plug(), adjustedMeta.computePotentialDependencies(overrides, value, composites));
	}

	boolean dependsFrom(T meta, List<T> overrides, Serializable value, List<T> composites) {
		return inheritsFrom(meta, value, composites) || getComposites().stream().filter(composite -> composite != null && composite != this).anyMatch(composite -> composite.dependsFrom(meta, overrides, value, composites))
				|| (!isRoot() && getMeta().dependsFrom(meta, overrides, value, composites)) || (!composites.isEmpty() && compositesDepends(getComposites(), composites) && overrides.stream().anyMatch(override -> override.inheritsFrom(getMeta())));
	}

	T getDirectEquivInstance(Serializable value, List<T> composites) {
		for (T instance : getInstances())
			if (instance.equiv(this, value, composites))
				return instance;
		return null;
	}

	private final Function<? super ISignature<?>, ? extends IVertex<?, ?>> NULL_TO_THIS = x -> x == null ? this : (IVertex<?, ?>) x;

	boolean equiv(IVertex<?, ?> meta, Serializable value, List<? extends IVertex<?, ?>> composites) {
		if (!getMeta().equiv(meta))
			return false;
		if (getComposites().size() != composites.size())
			return false;// for the moment, not equivalent when component size is different
		List<? extends IVertex<?, ?>> notNullComposites = composites.stream().map(NULL_TO_THIS).collect(Collectors.toList());
		List<T> compositesList = getComposites();
		for (int i = 0; i < compositesList.size(); i++)
			if (!isReferentialIntegrityEnabled(i) && isSingularConstraintEnabled(i) && compositesList.get(i).equiv(notNullComposites.get(i)))
				return true;
		for (int i = 0; i < compositesList.size(); i++)
			if (!compositesList.get(i).equiv(notNullComposites.get(i)))
				return false;
		if (!meta.isPropertyConstraintEnabled())
			return Objects.equals(getValue(), value);
		return true;
	}

	private void checkSameEngine(List<T> generics) {
		if (generics.stream().anyMatch(generic -> generic != null && !generic.getRoot().equals(getRoot())))
			getRoot().discardWithException(new CrossEnginesAssignementsException());
	}

	@SuppressWarnings("unchecked")
	T rebuildAll(Supplier<T> rebuilder, LinkedHashSet<T> dependenciesToRebuild) {
		ConvertMap<T, U> convertMap = new ConvertMap<>();
		dependenciesToRebuild.forEach(this::simpleRemove);
		T build = rebuilder.get();
		dependenciesToRebuild.remove(this);
		convertMap.put((T) this, build);
		dependenciesToRebuild.forEach(x -> convertMap.convert(x));
		return build;
	}

	@SuppressWarnings("unchecked")
	Snapshot<T> getInheritings(final T origin, final int level) {
		return () -> new InheritanceComputer<>((T) AbstractVertex.this, origin, level).inheritanceIterator();
	}

	abstract protected T newT();

	abstract protected T[] newTArray(int dim);

	@SuppressWarnings("unchecked")
	@Override
	public T[] coerceToTArray(Object... array) {
		T[] result = newTArray(array.length);
		for (int i = 0; i < array.length; i++)
			result[i] = (T) array[i];
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] addThisToTargets(T... targets) {
		T[] components = newTArray(targets.length + 1);
		components[0] = (T) this;
		System.arraycopy(targets, 0, components, 1, targets.length);
		return components;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	T buildInstance(Class<?> clazz, boolean throwExistException, List<T> overrides, Serializable value, List<T> composites) {
		int level = getLevel() == 0 && Objects.equals(getValue(), getRoot().getValue()) && getComposites().stream().allMatch(c -> c.isRoot()) && Objects.equals(value, getRoot().getValue()) && composites.stream().allMatch(c -> c.isRoot()) ? 0
				: getLevel() + 1;
		overrides.forEach(AbstractVertex::checkIsAlive);
		composites.stream().filter(x -> x != null).forEach(T::checkIsAlive);
		List<T> supers = new ArrayList<>(new SupersComputer(level, this, overrides, value, composites));
		checkOverridesAreReached(overrides, supers);
		return newT(clazz, throwExistException, (T) this, supers, value, composites);
	}

	void checkOverridesAreReached(List<T> overrides, List<T> supers) {
		if (!Statics.areOverridesReached(overrides, supers))
			getRoot().discardWithException(new IllegalStateException("Unable to reach overrides : " + overrides + " with computed supers : " + supers));
	}

	boolean compositesDepends(SingularsLazyCache singulars, List<T> subComposites, List<T> superComposites) {
		int subIndex = 0;
		loop: for (T superComposite : superComposites) {
			for (; subIndex < subComposites.size(); subIndex++) {
				T subComposite = subComposites.get(subIndex);
				assert subComposite != null || superComposite != null;
				if ((subComposite == null && equals(superComposite)) || (superComposite == null && equals(subComposite)) || (subComposite != null && superComposite != null && subComposite.isSpecializationOf(superComposite))) {
					if (singulars.get(subIndex))
						return true;
					subIndex++;
					continue loop;
				}
			}
			return false;
		}
		return true;
	}

	static interface SingularsLazyCache {
		boolean get(int i);
	}

	boolean compositesDepends(List<T> subComposites, @SuppressWarnings("unchecked") T... superComposites) {
		return compositesDepends(subComposites, Arrays.asList(superComposites));
	}

	boolean compositesDepends(List<T> subComposites, List<T> superComposites) {
		class SingularsLazyCacheImpl implements SingularsLazyCache {
			private final Boolean[] singulars = new Boolean[subComposites.size()];

			@Override
			public boolean get(int i) {
				return singulars[i] != null ? singulars[i] : (singulars[i] = isSingularConstraintEnabled(i));
			}
		}
		return compositesDepends(new SingularsLazyCacheImpl(), subComposites, superComposites);
	}

	@SuppressWarnings("unchecked")
	protected boolean isSuperOf(T subMeta, List<T> overrides, Serializable subValue, List<T> subComposites) {
		return overrides.stream().anyMatch(override -> override.inheritsFrom((T) this)) || isSuperOf(subMeta, subValue, subComposites, getMeta(), getValue(), getComposites());
	}

	protected boolean inheritsFrom(T superMeta, Serializable superValue, List<T> superComposites) {
		return isSuperOf(getMeta(), getValue(), getComposites(), superMeta, superValue, superComposites);
	}

	private static <T extends AbstractVertex<T, U>, U extends DefaultRoot<T, U>> boolean isSuperOf(T subMeta, Serializable subValue, List<T> subComposites, T superMeta, Serializable superValue, List<T> superComposites) {
		if (!subMeta.inheritsFrom(superMeta))
			return false;
		if (!subMeta.compositesDepends(subComposites, superComposites))
			return false;
		if (subMeta.isPropertyConstraintEnabled())
			return !subComposites.equals(superComposites);
		return Objects.equals(subValue, superValue);
	}

	@Override
	public Snapshot<T> getComponents() {
		return () -> getMetaComponentsDependencies().stream().map(entry -> entry.getValue().stream()).flatMap(x -> x).iterator();
	}

	// TODO KK public -> package
	public Snapshot<T> getMetaComponents(T meta) {
		return () -> {
			for (DependenciesEntry<T> entry : getMetaComponentsDependencies())
				if (meta.equals(entry.getKey()))
					return entry.getValue().iterator();
			return Collections.emptyIterator();
		};
	}

	// TODO KK public -> package
	public Snapshot<T> getSuperComponents(T superT) {
		return () -> {
			for (DependenciesEntry<T> entry : getSuperComponentsDependencies())
				if (superT.equals(entry.getKey()))
					return entry.getValue().iterator();
			return Collections.emptyIterator();
		};
	}

	@SuppressWarnings("unchecked")
	protected <subT extends T> subT plug() {
		T result = ((AbstractVertex<T, U>) getMeta()).indexInstance((T) this);
		getSupers().forEach(superGeneric -> ((AbstractVertex<T, U>) superGeneric).indexInheriting((T) this));
		getComposites().stream().filter(composite -> !equals(composite)).forEach(composite -> ((AbstractVertex<T, U>) composite).indexByMeta(getMeta(), (T) this));
		getSupers().forEach(superGeneric -> getComposites().stream().filter(composite -> !equals(composite)).forEach(composite -> ((AbstractVertex<T, U>) composite).indexBySuper(superGeneric, (T) this)));
		getRoot().check(CheckingType.CHECK_ON_ADD, true, (T) this);
		return (subT) result;
	}

	@SuppressWarnings("unchecked")
	protected boolean unplug() {
		getRoot().check(CheckingType.CHECK_ON_REMOVE, true, (T) this);
		boolean result = ((AbstractVertex<T, U>) getMeta()).unIndexInstance((T) this);
		if (!result)
			getRoot().discardWithException(new NotFoundException(this.info()));
		getSupers().forEach(superGeneric -> ((AbstractVertex<T, U>) superGeneric).unIndexInheriting((T) this));
		getComposites().stream().filter(composite -> !equals(composite)).forEach(composite -> ((AbstractVertex<T, U>) composite).unIndexByMeta(getMeta(), (T) this));
		getSupers().forEach(superGeneric -> getComposites().stream().filter(composite -> !equals(composite)).forEach(composite -> ((AbstractVertex<T, U>) composite).unIndexBySuper(superGeneric, (T) this)));
		return result;
	}

	private T indexByMeta(T meta, T composite) {
		return index(getMetaComponentsDependencies(), meta, composite);
	}

	private T indexBySuper(T superVertex, T composite) {
		return index(getSuperComponentsDependencies(), superVertex, composite);
	}

	private static <T extends AbstractVertex<T, U>, U extends DefaultRoot<T, U>> T index(Dependencies<DependenciesEntry<T>> multimap, T index, T composite) {
		for (DependenciesEntry<T> entry : multimap)
			if (index.equals(entry.getKey()))
				return entry.getValue().set(composite);

		Dependencies<T> dependencies = composite.buildDependencies();
		T result = dependencies.set(composite);
		multimap.set(new DependenciesEntry<>(index, dependencies));
		return result;
	}

	private static <T> boolean unIndex(Dependencies<DependenciesEntry<T>> multimap, T index, T composite) {
		for (DependenciesEntry<T> entry : multimap)
			if (index.equals(entry.getKey()))
				return entry.getValue().remove(composite);
		return false;
	}

	private boolean unIndexByMeta(T meta, T composite) {
		return unIndex(getMetaComponentsDependencies(), meta, composite);
	}

	private boolean unIndexBySuper(T superT, T composite) {
		return unIndex(getSuperComponentsDependencies(), superT, composite);
	}

	private static <T> T index(Dependencies<T> dependencies, T dependency) {
		return dependencies.set(dependency);
	}

	private static <T> boolean unIndex(Dependencies<T> dependencies, T dependency) {
		return dependencies.remove(dependency);
	}

	@Override
	public Snapshot<T> getInstances() {
		return getInstancesDependencies();
	}

	@Override
	public Snapshot<T> getInheritings() {
		return getInheritingsDependencies();
	}

	private T indexInstance(T instance) {
		return index(getInstancesDependencies(), instance);
	}

	private T indexInheriting(T inheriting) {
		return index(getInheritingsDependencies(), inheriting);
	}

	private boolean unIndexInstance(T instance) {
		return unIndex(getInstancesDependencies(), instance);
	}

	private boolean unIndexInheriting(T inheriting) {
		return unIndex(getInheritingsDependencies(), inheriting);
	}

	boolean equalsRegardlessSupers(IVertex<?, ?> meta, Serializable value, List<? extends IVertex<?, ?>> composites) {
		return (isRoot() || getMeta().equals(meta)) && Objects.equals(getValue(), value) && getComposites().equals(composites.stream().map(NULL_TO_THIS).collect(Collectors.toList()));
	}

	// TODO clean
	@SuppressWarnings("unchecked")
	T getMap() {
		return getRoot().getMetaAttribute().getDirectInstance(SystemMap.class, Collections.singletonList((T) getRoot()));
	}

	public static class SystemMap {
	}

	protected boolean equals(ISignature<?> meta, List<? extends ISignature<?>> supers, Serializable value, List<? extends ISignature<?>> composites) {
		return (isRoot() || getMeta().equals(meta)) && Objects.equals(getValue(), value) && getComposites().equals(composites.stream().map(NULL_TO_THIS).collect(Collectors.toList())) && getSupers().equals(supers);
	}

	protected Stream<T> getKeys() {
		T map = getMap();
		return map != null ? getAttributes(map).stream() : Stream.empty();
	}

	Optional<T> getKey(AxedPropertyClass property) {
		return getKeys().filter(x -> Objects.equals(x.getValue(), property)).findFirst();
	}

	void checkSystemConstraints(CheckingType checkingType, boolean isFlushTime) {
		checkDependsMetaComposites();
		checkSupers();
		checkDependsSuperComposites();
		checkLevel();
		checkLevelComposites();
		for (Class<? extends Constraint> constraintClass : DefaultRoot.SYSTEM_CONSTRAINTS)
			try {
				Constraint constraint = constraintClass.newInstance();
				if (isCheckable(constraint, checkingType, isFlushTime))
					constraint.check(this, this);
			} catch (InstantiationException | IllegalAccessException | ConstraintViolationException e) {
				getRoot().discardWithException(e);
			}
	}

	private void checkDependsMetaComposites() {
		if (!(getMeta().compositesDepends(getComposites(), getMeta().getComposites())))
			getRoot().discardWithException(new ConsistencyConstraintViolationException("Inconsistant components : " + getComposites() + " " + getMeta().getComposites()));
	}

	private void checkLevelComposites() {
		if (getComposites().stream().anyMatch(composite -> composite.getLevel() > getLevel()))
			getRoot().discardWithException(new ConsistencyConstraintViolationException("Inconsistant level link between components : level " + getLevel() + " and another"));
	}

	private void checkLevel() {
		if (getLevel() > Statics.CONCRETE)
			getRoot().discardWithException(new ConsistencyConstraintViolationException("Unable to instanciate generic : " + getMeta() + " because it is already concrete."));
	}

	private void checkSupers() {
		supers.forEach(AbstractVertex::checkIsAlive);
		if (!supers.stream().allMatch(superVertex -> superVertex.getLevel() == getLevel()))
			getRoot().discardWithException(new IllegalStateException("Inconsistant supers : " + supers));
		if (!supers.stream().allMatch(superVertex -> getMeta().inheritsFrom(superVertex.getMeta())))
			getRoot().discardWithException(new IllegalStateException("Inconsistant supers : " + supers));
		if (!supers.stream().noneMatch(this::equals))
			getRoot().discardWithException(new IllegalStateException("Supers loop detected : " + info()));
		if (supers.stream().anyMatch(superVertex -> Objects.equals(superVertex.getValue(), getValue()) && superVertex.getComposites().equals(getComposites()) && getMeta().inheritsFrom(superVertex.getMeta())))
			getRoot().discardWithException(new IllegalStateException("Collision detected : " + info()));
	}

	private void checkDependsSuperComposites() {
		getSupers().forEach(superVertex -> {
			if (!superVertex.isSuperOf(getMeta(), supers, getValue(), getComposites()))
				getRoot().discardWithException(new IllegalStateException("Inconsistant composites : " + getComposites()));
		});
	}

	void checkConstraints(CheckingType checkingType, boolean isFlushTime) {
		for (T constraintHolder : getActivedConstraints())
			try {
				Constraint constraint = newConstraint(constraintHolder);
				if (isCheckable(constraint, checkingType, isFlushTime)) {
					int axe = ((AxedPropertyClass) constraintHolder.getValue()).getAxe();
					constraint.check(this, getHolders(constraintHolder).iterator().next().getComposites().get(Statics.BASE_POSITION));
				}
			} catch (ConstraintViolationException e) {
				getRoot().discardWithException(e);
			}
	}

	private List<T> getActivedConstraints() {
		return getKeys().filter(x -> x.getValue() instanceof AxedPropertyClass && Constraint.class.isAssignableFrom(((AxedPropertyClass) x.getValue()).getClazz())).filter(x -> {
			Iterator<T> holders = getHolders(x).iterator();
			return holders.hasNext() && !holders.next().getValue().equals(Boolean.FALSE);
		}).sorted(priorityConstraintComparator).collect(Collectors.toList());
	}

	private Constraint newConstraint(T constraintHolder) {
		try {
			return (Constraint) ((AxedPropertyClass) constraintHolder.getValue()).getClazz().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			getRoot().discardWithException(e);
		}
		return null;
	}

	private boolean isCheckable(Constraint constraint, CheckingType checkingType, boolean isFlushTime) {
		return (isFlushTime || constraint.isImmediatelyCheckable()) && constraint.isCheckedAt(this, checkingType);
	}

	void checkConsistency(CheckingType checkingType, boolean isFlushTime) {
		// TODO impl
	}

	private final Comparator<T> priorityConstraintComparator = new Comparator<T>() {

		@SuppressWarnings("unchecked")
		@Override
		public int compare(T constraintHolder, T compareConstraintHolder) {
			return Constraint.getPriorityOf((Class<Constraint>) ((AxedPropertyClass) constraintHolder.getValue()).getClazz()) < Constraint.getPriorityOf((Class<Constraint>) ((AxedPropertyClass) compareConstraintHolder.getValue()).getClazz()) ? -1 : 1;
		}
	};

}
