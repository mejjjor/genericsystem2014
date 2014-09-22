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
import org.genericsystem.kernel.IRoot;

public abstract class AbstractGeneric<T extends AbstractGeneric<T, U, V, W>, U extends IEngine<T, U>, V extends AbstractVertex<V, W>, W extends IRoot<V, W>> extends AbstractVertex<T, U> implements IGeneric<T, U> {

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
		if (getClass().getAnnotation(SystemGeneric.class) != null)
			getRoot().discardWithException(new IllegalAccessException("SystemGeneric Annoted class can't be deleted"));
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
			vertex.addInstance(getSupersStream().map(T::unwrap).collect(Collectors.toList()), getValue(), vertex.coerceToTArray(getComponentsStream().map(T::unwrap).toArray()));
		else
			vertex.setInstance(getSupersStream().map(T::unwrap).collect(Collectors.toList()), getValue(), vertex.coerceToTArray(getComponentsStream().map(T::unwrap).toArray()));
		return (T) this;
	}

	@Override
	protected boolean unplug() {
		unwrap().remove();
		return true;
	}

	@SuppressWarnings("unchecked")
	protected T wrap(V vertex) {
		if (vertex.isRoot())
			return (T) getRoot();
		V alive = vertex.getAlive();
		return newT(null, alive.isThrowExistException(), wrap(alive.getMeta()), alive.getSupersStream().map(this::wrap).collect(Collectors.toList()), alive.getValue(), alive.getComponentsStream().map(this::wrap).collect(Collectors.toList()));
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

	@Override
	public Snapshot<T> getComponents() {
		return () -> unwrap().getComponents().stream().map(this::wrap).iterator();
	}

	@Override
	public Snapshot<T> getMetaComposites(T meta) {
		return () -> unwrap().getMetaComposites(meta.unwrap()).stream().map(this::wrap).iterator();
	}

	@Override
	public Snapshot<T> getSuperComposites(T superT) {
		return () -> unwrap().getSuperComposites(superT.unwrap()).stream().map(this::wrap).iterator();
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
	protected Dependencies<DependenciesEntry<T>> getMetaComposites() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Dependencies<DependenciesEntry<T>> getSuperComposites() {
		throw new UnsupportedOperationException();
	}

	// @Override
	// protected T bindInstance(Class<?> clazz, boolean throwExistException, List<T> overrides, Serializable value, List<T> components) {
	// return super.bindInstance(clazz, throwExistException, overrides, value, components);
	// }

}
