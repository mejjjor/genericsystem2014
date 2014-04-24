package org.genericsystem.kernel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import org.genericsystem.kernel.Engine.ValueCache;
import org.genericsystem.kernel.services.AncestorsService;
import org.genericsystem.kernel.services.BindingService;
import org.genericsystem.kernel.services.CompositesInheritanceService;
import org.genericsystem.kernel.services.DependenciesService;
import org.genericsystem.kernel.services.DisplayService;
import org.genericsystem.kernel.services.FactoryService;
import org.genericsystem.kernel.services.InheritanceService;
import org.genericsystem.kernel.services.SystemPropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vertex implements AncestorsService, DependenciesService, InheritanceService, BindingService, CompositesInheritanceService, FactoryService, DisplayService, SystemPropertiesService {

	protected static Logger log = LoggerFactory.getLogger(Vertex.class);

	private final Serializable value;
	private final Vertex meta;
	private final Vertex[] components;
	private final Dependencies<Vertex> instances;
	private final Dependencies<Vertex> inheritings;
	private final CompositesDependencies<Vertex> metaComposites;
	private final CompositesDependencies<Vertex> superComposites;
	private final Vertex[] supers;

	// Engine constructor
	protected Vertex(Factory factory) {
		((Engine) this).valueCache = new ValueCache();
		((Engine) this).factory = factory;
		meta = this;
		value = ((Engine) this).getCachedValue(Statics.ENGINE_VALUE);
		components = Statics.EMPTY_VERTICES;
		instances = getFactory().buildDependency(this);
		inheritings = getFactory().buildDependency(this);
		metaComposites = getFactory().buildComponentDependency(this);
		superComposites = getFactory().buildComponentDependency(this);
		supers = Statics.EMPTY_VERTICES;
	}

	protected Vertex(Vertex meta, Vertex[] overrides, Serializable value, Vertex[] components) {
		this.meta = isEngine() ? (Vertex) this : meta;
		this.value = getEngine().getCachedValue(value);
		this.components = components;
		instances = getFactory().buildDependency(this);
		inheritings = getFactory().buildDependency(this);
		metaComposites = getFactory().buildComponentDependency(this);
		superComposites = getFactory().buildComponentDependency(this);
		supers = getSupers(overrides);
		checkOverrides(overrides);
		checkSupers();
	}

	@Override
	public Vertex getMeta() {
		return meta;
	}

	@Override
	public Vertex[] getComponents() {
		return components;
	}

	@Override
	public Serializable getValue() {
		return value;
	}

	@Override
	public Dependencies<Vertex> getInstances() {
		return instances;
	}

	@Override
	public Dependencies<Vertex> getInheritings() {
		return inheritings;
	}

	@Override
	public Snapshot<Vertex> getMetaComposites(Vertex meta) {
		return metaComposites.getByIndex(meta);
	}

	@Override
	public Snapshot<Vertex> getSuperComposites(Vertex superVertex) {
		return superComposites.getByIndex(superVertex);
	}

	@Override
	public CompositesDependencies<Vertex> getMetaComposites() {
		return metaComposites;
	}

	@Override
	public CompositesDependencies<Vertex> getSuperComposites() {
		return superComposites;
	}

	@Override
	public Stream<Vertex> getSupersStream() {
		return Arrays.stream(supers);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getValue());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Vertex))
			return false;
		Vertex vertex = (Vertex) o;
		return equals(vertex.getMeta(), vertex.getValue(), vertex.getComponents());
	}

	@Override
	public String toString() {
		return Objects.toString(getValue());
	}

}
