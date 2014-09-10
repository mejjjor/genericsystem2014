package org.genericsystem.kernel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Signature<T extends Signature<T, U>, U extends IRoot<T, U>> implements IVertex<T, U> {

	private T meta;
	private List<T> components;
	private Serializable value;
	protected boolean throwExistException;

	@SuppressWarnings("unchecked")
	protected T init(boolean throwExistException, T meta, Serializable value, List<T> components) {
		this.throwExistException = throwExistException;
		if (meta != null) {
			meta.checkIsAlive();
			this.meta = meta;
		} else
			this.meta = (T) this;
		this.value = value;
		this.components = new ArrayList<>(components);
		for (int i = 0; i < components.size(); i++) {
			T component = components.get(i);
			if (component != null) {
				component.checkIsAlive();
				this.components.set(i, component);
			} else
				this.components.set(i, (T) this);
		}
		return (T) this;
	}

	public boolean isThrowExistException() {
		return throwExistException;
	}

	@Override
	public T getMeta() {
		return meta;
	}

	@Override
	public List<T> getComponents() {
		return components;
	}

	@Override
	public Serializable getValue() {
		return value;
	}

	@Override
	public String toString() {
		return Objects.toString(getValue());
	}

	@Override
	public int getLevel() {
		return (isRoot() || components.stream().allMatch(c -> c.isRoot()) && Objects.equals(getValue(), getRoot().getValue())) ? 0 : meta.getLevel() + 1;
	}

}
