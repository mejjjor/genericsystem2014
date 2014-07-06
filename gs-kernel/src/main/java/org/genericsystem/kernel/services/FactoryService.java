package org.genericsystem.kernel.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import org.genericsystem.kernel.Dependencies;
import org.genericsystem.kernel.DependenciesImpl;
import org.genericsystem.kernel.ExtendedSignature;
import org.genericsystem.kernel.Signature;
import org.genericsystem.kernel.SupersComputer;

public interface FactoryService<T extends FactoryService<T>> extends DependenciesService<T> {

	T buildInstance();

	// TODO : add a convenience method isMetaAttribut.

	@SuppressWarnings({ "unchecked", "rawtypes" })
	default T buildInstance(List<T> overrides, Serializable value, List<T> components) {
		int level = getLevel() == 0 && Objects.equals(getValue(), getRoot().getValue()) && getComponentsStream().allMatch(c -> c.isRoot()) && Objects.equals(value, getRoot().getValue()) && components.stream().allMatch(c -> c.isRoot()) ? 0 : getLevel() + 1;
		overrides.forEach(x -> ((Signature) x).checkIsAlive());
		components.forEach(x -> ((Signature) x).checkIsAlive());
		List<T> supers = new ArrayList<T>(new SupersComputer(level, this, overrides, value, components));
		checkOverridesAreReached(overrides, supers);
		return (T) ((ExtendedSignature) buildInstance().init((T) this, supers, value, components));
	}

	default boolean allOverridesAreReached(List<T> overrides, List<T> supers) {
		return overrides.stream().allMatch(override -> supers.stream().anyMatch(superVertex -> superVertex.inheritsFrom(override)));
	}

	default void checkOverridesAreReached(List<T> overrides, List<T> supers) {
		if (!allOverridesAreReached(overrides, supers))
			rollbackAndThrowException(new IllegalStateException("Unable to reach overrides : " + overrides + " with computed supers : " + supers));
	}

	T init(T meta, List<T> overrides, Serializable value, List<T> components);

	default <U extends T> Dependencies<U> buildDependencies(Supplier<Iterator<T>> subDependenciesSupplier) {
		return new DependenciesImpl<U>();
	}

}
