package org.genericsystem.kernel.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.genericsystem.kernel.exceptions.NotFoundException;

public interface UpdatableService<T extends UpdatableService<T>> extends BindingService<T> {

	default T setValue(Serializable value) {
		T meta = getMeta();
		return rebuildAll(() -> buildInstance().init(meta.getLevel() + 1, meta, getSupers(), value, getComponents()).plug());
	}

	default T addSuper(T superToAdd) {
		return rebuildAll(() -> getMeta().buildInstance(new Supers<T>(getSupers(), superToAdd), getValue(), getComponents()).plug());
	}

	default T replaceComponent(T source, T target) {
		return rebuildAll(() -> getMeta().buildInstance(getSupers(), getValue(), replaceInComponents(source, target)).plug());
	}

	default T update(List<T> supers, Serializable newValue, List<T> newComponents) {
		T meta = getMeta();
		return rebuildAll(() -> buildInstance().init(meta.getLevel() + 1, meta, new Supers<T>(getSupers(), supers), newValue, newComponents).plug());
	}

	@FunctionalInterface
	interface Rebuilder<T> {
		T rebuild();
	}

	@SuppressWarnings("unchecked")
	default T rebuildAll(Rebuilder<T> rebuilder) {
		Map<T, T> convertMap = new HashMap<T, T>();
		LinkedHashSet<T> dependenciesToRebuild = this.computeAllDependencies();
		dependenciesToRebuild.forEach(UpdatableService::unplug);

		T build = rebuilder.rebuild();
		dependenciesToRebuild.remove(this);
		convertMap.put((T) this, build);

		dependenciesToRebuild.forEach(x -> x.getOrBuild(convertMap));
		return build;
	}

	@SuppressWarnings("unchecked")
	default T getOrBuild(Map<T, T> convertMap) {
		if (this.isAlive())
			return (T) this;
		T newDependency = convertMap.get(this);
		if (newDependency == null)
			convertMap.put((T) this, newDependency = this.build(convertMap));
		return newDependency;
	}

	@SuppressWarnings("unchecked")
	default T build(Map<T, T> convertMap) {
		T meta = (this == getMeta()) ? (T) this : getMeta().getOrBuild(convertMap);
		return meta.buildInstance(getSupersStream().map(x -> x.getOrBuild(convertMap)).collect(Collectors.toList()), getValue(), getComponentsStream().map(x -> x.equals(this) ? null : x.getOrBuild(convertMap)).collect(Collectors.toList())).plug();
	}

	default List<T> replaceInComponents(T source, T target) {
		List<T> newComponents = getComponents();
		boolean hasBeenModified = false;
		for (int i = 0; i < newComponents.size(); i++)
			if (source.equiv(newComponents.get(i))) {
				newComponents.set(i, target);
				hasBeenModified = true;
			}
		if (!hasBeenModified)
			rollbackAndThrowException(new NotFoundException("Component : " + source.info() + " not found in component list : " + newComponents.toString() + " for " + this.info() + "when modifying componentList."));
		return newComponents;
	}

	public static class Supers<T extends UpdatableService<T>> extends ArrayList<T> {
		private static final long serialVersionUID = 6163099887384346235L;

		public Supers(List<T> adds) {
			for (T add : adds)
				add(add);
		}

		public Supers(List<T> adds, T lastAdd) {
			this(adds);
			add(lastAdd);
		}

		public Supers(List<T> adds, List<T> lastAdds) {
			this(adds);
			lastAdds.forEach(this::add);
		}

		@Override
		public boolean add(T candidate) {
			for (T element : this)
				if (element.inheritsFrom(candidate))
					return false;
			Iterator<T> it = iterator();
			while (it.hasNext())
				if (candidate.inheritsFrom(it.next()))
					it.remove();
			return super.add(candidate);
		}
	}

}