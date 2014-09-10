package org.genericsystem.kernel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.genericsystem.kernel.services.IVertexBase;

public interface IDependencies<T extends AbstractVertex<T, U>, U extends IRoot<T, U>> extends IVertexBase<T, U> {

	@Override
	default boolean isAncestorOf(T dependency) {
		return equals(dependency) || (!dependency.isRoot() && isAncestorOf(dependency.getMeta())) || dependency.getSupersStream().anyMatch(this::isAncestorOf)
				|| dependency.getComponentsStream().filter(component -> !dependency.equals(component)).anyMatch(this::isAncestorOf);
	}

	@SuppressWarnings("unchecked")
	@Override
	default Snapshot<T> getAllInheritings() {
		return () -> (Stream.concat(Stream.of((T) this), Statics.concat(getInheritings().stream(), inheriting -> inheriting.getAllInheritings().stream()).distinct())).iterator();
	}

	@Override
	default Snapshot<T> getAllInstances() {
		return () -> getAllInheritings().stream().map(inheriting -> inheriting.getInstances().stream()).flatMap(x -> x).iterator();// .reduce(Stream.empty(), Stream::concat);
	}

	@Override
	default T getInstance(T superT, Serializable value, @SuppressWarnings("unchecked") T... components) {
		return getInstance(Collections.singletonList(superT), value, components);
	}

	@Override
	@SuppressWarnings("unchecked")
	default T getInstance(Serializable value, T... components) {
		return getInstance(Collections.emptyList(), value, components);
	}

	@Override
	@SuppressWarnings("unchecked")
	default T getInstance(List<T> overrides, Serializable value, T... components) {
		T meta = (T) ((AbstractVertex) this).adjustMeta(value, Arrays.asList(components));
		if (meta != this)
			return meta.getInstance(value, components);
		return (T) ((AbstractVertex) this).getDirectInstance(overrides, value, Arrays.asList(components));
	}

}
