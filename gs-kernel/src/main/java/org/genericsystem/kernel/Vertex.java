package org.genericsystem.kernel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

import org.genericsystem.kernel.Dependencies.CompositesDependencies;
import org.genericsystem.kernel.services.AncestorsService;
import org.genericsystem.kernel.services.BindingService;
import org.genericsystem.kernel.services.CompositesInheritanceService;
import org.genericsystem.kernel.services.DependenciesService;
import org.genericsystem.kernel.services.DisplayService;
import org.genericsystem.kernel.services.ExceptionAdviserService;
import org.genericsystem.kernel.services.FactoryService;
import org.genericsystem.kernel.services.InheritanceService;
import org.genericsystem.kernel.services.SystemPropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vertex extends ExtendedSignature<Vertex> implements AncestorsService<Vertex>, DependenciesService<Vertex>, InheritanceService<Vertex>, BindingService<Vertex>, CompositesInheritanceService<Vertex>, FactoryService<Vertex>,
		DisplayService<Vertex>, SystemPropertiesService, ExceptionAdviserService<Vertex> {
	protected static Logger log = LoggerFactory.getLogger(Vertex.class);
	protected static final Vertex[] EMPTY_VERTICES = new Vertex[] {};

	private final Dependencies<Vertex> instances = buildDependencies();
	private final Dependencies<Vertex> inheritings = buildDependencies();
	private final CompositesDependencies<Vertex> superComposites = buildCompositeDependencies();
	private final CompositesDependencies<Vertex> metaComposites = buildCompositeDependencies();

	@Override
	public Vertex build() {
		return new Vertex();
	}

	@Override
	public Vertex initFromOverrides(Vertex meta, Stream<Vertex> overrides, Serializable value, Stream<Vertex> components) {
		return super.initFromOverrides(meta, overrides.toArray(Vertex[]::new), value, components.toArray(Vertex[]::new));
	}

	@Override
	public Vertex initFromSupers(Vertex meta, Stream<Vertex> supers, Serializable value, Stream<Vertex> components) {
		return super.initFromSupers(meta, supers.toArray(Vertex[]::new), value, components.toArray(Vertex[]::new));
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
	public Vertex getInstance(Serializable value, Vertex... components) {
		return build().initFromOverrides(this, Arrays.stream(getEmptyArray()), value, Arrays.stream(components)).getAlive();
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
	public Vertex[] getEmptyArray() {
		return EMPTY_VERTICES;
	}

	@Override
	public Vertex[] computeSupers(Vertex[] overrides) {
		return computeSupersStream(overrides).toArray(Vertex[]::new);
	}

}
