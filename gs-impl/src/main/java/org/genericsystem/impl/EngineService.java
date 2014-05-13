package org.genericsystem.impl;

import java.util.Objects;
import org.genericsystem.kernel.Root;
import org.genericsystem.kernel.services.AncestorsService;

public interface EngineService<T extends GenericService<T>> extends GenericService<T> {

	@Override
	default int getLevel() {
		return 0;
	}

	default Root buildVerticesRoot() {
		return new Root();
	}

	@Override
	default boolean isRoot() {
		return true;
	}

	@Override
	default boolean isAlive() {
		return equiv(getAlive());
	}

	@SuppressWarnings("unchecked")
	@Override
	default T getRoot() {
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	default T getMeta() {
		return (T) this;
	}

	@Override
	default boolean equiv(AncestorsService<?> service) {
		if (this == service)
			return true;
		return Objects.equals(getValue(), service.getValue()) && equivComponents(service);
	}
}