package org.genericsystem.cache;

import java.io.Serializable;
import java.util.stream.Stream;
import org.genericsystem.impl.AbstractGeneric;

public class Generic extends AbstractGeneric<Generic> implements GenericService<Generic> {

	@Override
	public Generic build(Generic meta, Stream<Generic> overrides, Serializable value, Stream<Generic> components) {
		return new Generic().init(meta, overrides.toArray(Generic[]::new), value, components.toArray(Generic[]::new));
	}

	private final static Generic[] EMPTY_ARRAY = new Generic[] {};

	@Override
	public Generic[] getEmptyArray() {
		return EMPTY_ARRAY;
	}

}
