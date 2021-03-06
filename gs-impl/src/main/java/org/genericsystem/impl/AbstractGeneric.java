package org.genericsystem.impl;

import java.util.Objects;
import java.util.stream.Collectors;

import org.genericsystem.api.core.ISignature;
import org.genericsystem.api.core.Snapshot;
import org.genericsystem.impl.annotations.InstanceClass;
import org.genericsystem.impl.annotations.SystemGeneric;
import org.genericsystem.kernel.AbstractVertex;
import org.genericsystem.kernel.Dependencies;
import org.genericsystem.kernel.Dependencies.DependenciesEntry;
import org.genericsystem.kernel.systemproperty.constraints.Constraint.CheckingType;
import org.genericsystem.kernel.DefaultRoot;

public abstract class AbstractGeneric<T extends AbstractGeneric<T, U, V, W>, U extends IEngine<T, U>, V extends AbstractVertex<V, W>, W extends DefaultRoot<V, W>> extends AbstractVertex<T, U> implements IGeneric<T, U> {

	@SuppressWarnings("unchecked")
	@Override
	protected T newT(Class<?> clazz) {
		InstanceClass metaAnnotation = getClass().getAnnotation(InstanceClass.class);
		if (metaAnnotation != null)
			if (clazz == null || clazz.isAssignableFrom(metaAnnotation.value()))
				clazz = metaAnnotation.value();
			else if (!metaAnnotation.value().isAssignableFrom(clazz))
				getRoot().discardWithException(new InstantiationException(clazz + " must extends " + metaAnnotation.value()));
		T newT = newT();// Instantiates T in all cases...

		if (clazz == null || clazz.isAssignableFrom(newT.getClass()))
			return newT;
		if (newT.getClass().isAssignableFrom(clazz))
			try {
				return (T) clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
				getRoot().discardWithException(e);
			}
		else
			getRoot().discardWithException(new InstantiationException(clazz + " must extends " + newT.getClass()));
		return null; // Not reached
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ISignature<?>))
			return false;
		ISignature<?> service = (ISignature<?>) obj;
		return equals(service.getMeta(), service.getSupers(), service.getValue(), service.getComposites());
	}

	@Override
	public void remove() {
		// TODO KK this verification must go in simpleRemove....
		if (getClass().getAnnotation(SystemGeneric.class) != null)
			getRoot().discardWithException(new IllegalAccessException("@SystemGeneric annoted generic can't be removed"));
		super.remove();
	}

	@Override
	public int hashCode() {
		// TODO introduce : meta and components length
		return Objects.hashCode(getValue());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected T plug() {
		V vertex = getMeta().unwrap();
		if (isThrowExistException())
			vertex.addInstance(getSupers().stream().map(T::unwrap).collect(Collectors.toList()), getValue(), vertex.coerceToTArray(getComposites().stream().map(T::unwrap).toArray()));
		else
			vertex.setInstance(getSupers().stream().map(T::unwrap).collect(Collectors.toList()), getValue(), vertex.coerceToTArray(getComposites().stream().map(T::unwrap).toArray()));
		getRoot().check(CheckingType.CHECK_ON_ADD, true, (T) this);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean unplug() {
		getRoot().check(CheckingType.CHECK_ON_REMOVE, true, (T) this);
		unwrap().remove();
		return true;
	}

	@SuppressWarnings("unchecked")
	protected T wrap(V vertex) {
		if (vertex.isRoot())
			return (T) getRoot();
		V alive = vertex.getAlive();
		return newT(null, alive.isThrowExistException(), wrap(alive.getMeta()), alive.getSupers().stream().map(this::wrap).collect(Collectors.toList()), alive.getValue(), alive.getComposites().stream().map(this::wrap).collect(Collectors.toList()));
	}

	protected V unwrap() {
		V metaVertex = getMeta().unwrap();
		if (metaVertex == null)
			return null;
		for (V instance : metaVertex.getInstances())
			if (equals(instance))
				return instance;
		return null;
	}

	@Override
	public Snapshot<T> getInstances() {
		return () -> unwrap().getInstances().stream().map(this::wrap).iterator();
	}

	@Override
	public Snapshot<T> getInheritings() {
		return () -> unwrap().getInheritings().stream().map(this::wrap).iterator();
	}

	// TODO remove this method ?
	@Override
	public Snapshot<T> getComponents() {
		return () -> unwrap().getComponents().stream().map(this::wrap).iterator();
	}

	@Override
	public Snapshot<T> getMetaComponents(T meta) {
		return () -> unwrap().getMetaComponents(meta.unwrap()).stream().map(this::wrap).iterator();
	}

	@Override
	public Snapshot<T> getSuperComponents(T superT) {
		return () -> unwrap().getSuperComponents(superT.unwrap()).stream().map(this::wrap).iterator();
	}

	@Override
	protected Dependencies<T> getInheritingsDependencies() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Dependencies<T> getInstancesDependencies() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Dependencies<DependenciesEntry<T>> getMetaComponentsDependencies() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Dependencies<DependenciesEntry<T>> getSuperComponentsDependencies() {
		throw new UnsupportedOperationException();
	}

}
