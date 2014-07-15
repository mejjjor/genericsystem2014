package org.genericsystem.kernel.services;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import org.genericsystem.kernel.Snapshot;
import org.genericsystem.kernel.Statics;

public interface DependenciesService<T extends VertexService<T>> extends ApiService<T> {

	@Override
	default boolean isAncestorOf(T dependency) {
		return equiv(dependency) || (!dependency.isRoot() && isAncestorOf(dependency.getMeta())) || dependency.getSupersStream().anyMatch(this::isAncestorOf)
				|| dependency.getComponentsStream().filter(component -> !dependency.equals(component)).anyMatch(this::isAncestorOf);
	}

	default boolean isAncestorOf(T meta, List<T> overrides, Serializable value, List<T> components) {
		// perhaps we have to adjust meta here
		return isSuperOf(meta, overrides, value, components) || isAncestorOf(meta) || components.stream().filter(component -> component != null).anyMatch(component -> this.isAncestorOf(component));
	}

	@SuppressWarnings("unchecked")
	default Stream<T> select() {
		return Stream.of((T) this);
	}

	@Override
	default Snapshot<T> getAllInheritings() {
		return () -> (Stream.concat(select(), Statics.concat(getInheritings().stream(), inheriting -> inheriting.getAllInheritings().stream()).distinct())).iterator();
	}

	@Override
	default Snapshot<T> getAllInstances() {
		return () -> getAllInheritings().stream().map(inheriting -> ((DependenciesService<T>) inheriting).getInstances().stream()).flatMap(x -> x).iterator();// .reduce(Stream.empty(), Stream::concat);
	}
}
