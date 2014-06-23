package org.genericsystem.kernel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.genericsystem.kernel.annotations.Components;
import org.genericsystem.kernel.annotations.Extends;
import org.genericsystem.kernel.annotations.Meta;
import org.genericsystem.kernel.annotations.value.AxedConstraintValue;
import org.genericsystem.kernel.annotations.value.BooleanValue;
import org.genericsystem.kernel.annotations.value.IntValue;
import org.genericsystem.kernel.annotations.value.StringValue;

public class Root extends Vertex implements RootService<Vertex> {

	public Root() {
		this(Statics.ENGINE_VALUE);
	}

	public Root(Serializable value) {
		init(null, Collections.emptyList(), value, Collections.emptyList());
	}

	@Override
	public Vertex getRoot() {
		return this;
	}

	@Override
	public Vertex getAlive() {
		return this;
	}

	private final Map<Class<?>, Vertex> systemCache = new HashMap<Class<?>, Vertex>();

	public Vertex find(Class<?> clazz) {
		Vertex result = systemCache.get(clazz);
		if (result == null) {
			ClassFinder classFinder = new ClassFinder(clazz);
			result = classFinder.find();
			systemCache.put(clazz, result);
		}
		return result;
	}

	private class ClassFinder {

		private Class<?> clazz;

		public ClassFinder(Class<?> clazz) {
			this.clazz = clazz;
		}

		public Vertex find() {
			return findMeta().setInstance(findOverrides(), findValue(), findComponents());
		}

		Vertex findMeta() {
			Meta meta = clazz.getAnnotation(Meta.class);
			return (new ClassFinder(meta.value())).find();
		}

		List<Vertex> findOverrides() {
			Extends overrides = clazz.getAnnotation(Extends.class);
			List<Class<?>> overridesLst = Arrays.asList(overrides.value());
			List<Vertex> overridesVertices = new ArrayList<Vertex>(overridesLst.size());
			for (Class<?> clazz : overridesLst) {
				overridesVertices.add(new ClassFinder(clazz).find());
			}
			return overridesVertices;
		}

		Serializable findValue() {
			Serializable result = null;
			AxedConstraintValue axedConstraintValue = clazz.getAnnotation(AxedConstraintValue.class);
			if (axedConstraintValue != null) {
				Class<?> constraintClazz = axedConstraintValue.value();
				// Vertex constraint = new ClassFinder(constraintClazz).find();
				int axe = axedConstraintValue.axe();
				result = "Constraint = " + constraintClazz + ", Axe = " + axe;
			}

			BooleanValue booleanValue = clazz.getAnnotation(BooleanValue.class);
			if (booleanValue != null) {
				boolean boolVal = booleanValue.value();
				result = boolVal;
			}

			IntValue intValue = clazz.getAnnotation(IntValue.class);
			if (intValue != null) {
				int intVal = intValue.value();
				result = intVal;
			}

			StringValue stringValue = clazz.getAnnotation(StringValue.class);
			if (stringValue != null) {
				String stringVal = stringValue.value();
				result = stringVal;
			}
			return result;
		}

		Vertex[] findComponents() {
			Components components = clazz.getAnnotation(Components.class);
			Class<?> componentsLst[] = components.value();
			Vertex componentsVertices[] = new Vertex[componentsLst.length];
			int index = 0;
			for (Class<?> clazz : componentsLst) {
				componentsVertices[index] = new ClassFinder(clazz).find();
				++index;
			}
			return componentsVertices;
		}

		public Class<?> getClazz() {
			return clazz;
		}

		public void setClazz(Class<?> clazz) {
			this.clazz = clazz;
		}
	}

}
