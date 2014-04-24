package org.genericsystem.kernel;

import java.util.Iterator;

import org.genericsystem.kernel.iterator.AbstractGeneralAwareIterator;
import org.genericsystem.kernel.services.DependenciesService.Dependencies;

public class DependenciesImpl extends Dependencies {

	private Node head = null;
	private Node tail = null;

	@Override
	protected void add(Vertex element) {
		assert !this.contains(element);
		assert element != null;
		Node newNode = new Node(element);
		if (head == null)
			head = newNode;
		else
			tail.next = newNode;
		tail = newNode;
	}

	@Override
	protected boolean remove(Vertex element) {
		Iterator<Vertex> iterator = iterator();
		while (iterator.hasNext())
			if (element.equals(iterator.next())) {
				iterator.remove();
				return true;
			}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return head == null;
	}

	@Override
	public Iterator<Vertex> iterator() {
		return new InternalIterator();
	}

	private class InternalIterator extends AbstractGeneralAwareIterator<Node, Vertex> implements Iterator<Vertex> {

		private Node last;

		@Override
		protected void advance() {
			last = next;
			next = next == null ? head : next.next;
		}

		@Override
		public Vertex project() {
			return next.content;
		}

		@Override
		public void remove() {
			if (next == null)
				throw new IllegalStateException();
			if (last == null) {
				head = next.next;
				return;
			}
			last.next = next.next;
			if (next.next == null)
				tail = last;
		}
	}

	private static class Node {
		final Vertex content;
		Node next;

		private Node(Vertex content) {
			this.content = content;
		}
	}
}
