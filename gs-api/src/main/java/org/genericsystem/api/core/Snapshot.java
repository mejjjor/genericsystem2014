package org.genericsystem.api.core;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Snapshot<T> extends Iterable<T> {

	default int size() {
		Iterator<T> iterator = iterator();
		int size = 0;
		while (iterator.hasNext()) {
			iterator.next();
			size++;
		}
		return size;
	}

	default boolean isEmpty() {
		return !iterator().hasNext();
	}

	default boolean contains(Object o) {
		Iterator<T> it = iterator();
		while (it.hasNext())
			if (o.equals(it.next()))
				return true;
		return false;
	}

	default boolean containsAll(Collection<?> c) {
		for (Object e : c)
			if (!contains(e))
				return false;
		return true;
	}

	default T get(T o) {
		Iterator<T> it = iterator();
		while (it.hasNext()) {
			T next = it.next();
			if (o.equals(next))
				return next;
		}
		return null;
	}

	default Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	default String info() {
		return stream().collect(Collectors.toList()).toString();
	}

	public static abstract class AbstractSnapshot<T> implements Snapshot<T> {

		public static <T> Snapshot<T> emptySnapshot() {
			return new AbstractSnapshot<T>() {
				@Override
				public Iterator<T> iterator() {
					return Collections.emptyIterator();
				}

			};
		}

		@Override
		public String toString() {
			Iterator<T> it = iterator();
			if (!it.hasNext())
				return "[]";
			StringBuilder sb = new StringBuilder();
			sb.append('[');
			for (;;) {
				T e = it.next();
				sb.append(e == this ? "(this Collection)" : e);
				if (!it.hasNext())
					return sb.append(']').toString();
				sb.append(',').append(' ');
			}
		}

		@Override
		@SuppressWarnings("rawtypes")
		public boolean equals(Object o) {
			if (o == this)
				return true;
			if (!(o instanceof Snapshot))
				return false;
			Snapshot s = (Snapshot) o;
			Iterator<T> e1 = iterator();
			Iterator e2 = s.iterator();
			while (e1.hasNext() && e2.hasNext()) {
				T o1 = e1.next();
				Object o2 = e2.next();
				if (!(Objects.equals(o1, o2)))
					return false;
			}
			return !(e1.hasNext() || e2.hasNext());
		}

		@Override
		public int hashCode() {
			int hashCode = 1;
			for (T e : this)
				hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
			return hashCode;
		}

	}

}
