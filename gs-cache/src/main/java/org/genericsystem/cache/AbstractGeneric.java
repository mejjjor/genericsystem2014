package org.genericsystem.cache;

import java.util.LinkedHashSet;
import org.genericsystem.kernel.Snapshot;
import org.genericsystem.kernel.Vertex;

public abstract class AbstractGeneric<T extends AbstractGeneric<T>> extends org.genericsystem.impl.AbstractGeneric<T> implements GenericService<T> {
	@SuppressWarnings("unchecked")
	@Override
	public T plug() {
		return getCurrentCache().plug((T) this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean unplug() {
		return getCurrentCache().unplug((T) this);
	}

	@Override
	protected Vertex getVertex() {
		return super.getVertex();
	}

	@Override
	protected Vertex unwrap() {
		return super.unwrap();
	}

	@Override
	protected T wrap(Vertex vertex) {
		return super.wrap(vertex);
	}

	@Override
	protected void forceRemove() {
		super.forceRemove();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Snapshot<T> getInstances() {
		return getCurrentCache().getInstances((T) this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Snapshot<T> getInheritings() {
		return getCurrentCache().getInheritings((T) this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Snapshot<T> getMetaComposites(T meta) {
		return getCurrentCache().getMetaComposites((T) this, meta);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Snapshot<T> getSuperComposites(T superVertex) {
		return getCurrentCache().getSuperComposites((T) this, superVertex);
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public T getInstance(Serializable value, T... components) {
	// T nearestMeta = adjustMeta(Collections.emptyList(), value, Arrays.asList(components));
	// if (!equals(nearestMeta))
	// return nearestMeta.getInstance(value, components);
	// for (T instance : getCurrentCache().getInstances(nearestMeta))
	// if (instance.equiv(this, value, Arrays.asList(components)))
	// return instance;
	// return null;
	// }

	@SuppressWarnings("unchecked")
	@Override
	public Snapshot<T> getComposites() {
		return getCurrentCache().getComposites((T) this);
	}

	@Override
	protected LinkedHashSet<T> computeAllDependencies() {
		return super.computeAllDependencies();
	}

}
