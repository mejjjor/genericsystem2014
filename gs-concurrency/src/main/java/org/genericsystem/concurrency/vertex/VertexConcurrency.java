package org.genericsystem.concurrency.vertex;

import java.util.Iterator;

import org.genericsystem.kernel.Dependencies;
import org.genericsystem.kernel.Vertex;

public class VertexConcurrency extends Vertex implements VertexServiceConcurrency<Vertex> {

	// TODO KK DEBUT KK cf RootConcurrency
	private LifeManager lifeManager;

	void restore(Long designTs, long birthTs, long lastReadTs, long deathTs) {
		lifeManager = buildLifeManager(designTs, birthTs, lastReadTs, deathTs);
	}

	@Override
	public LifeManager getLifeManager() {
		return lifeManager;
	}

	public boolean isAlive(long ts) {
		return lifeManager.isAlive(ts);
	}

	// TODO KK FIN KK

	@Override
	public VertexConcurrency newT() {
		VertexConcurrency vertexConcurrency = new VertexConcurrency();
		vertexConcurrency.lifeManager = buildLifeManager();
		return vertexConcurrency;
	}

	@Override
	public Vertex[] newTArray(int dim) {
		return new VertexConcurrency[dim];
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <U> Dependencies<U> buildDependencies() {
		return (Dependencies<U>) new AbstractDependenciesConcurrency() {

			@Override
			public LifeManager getLifeManager() {
				return lifeManager;
			}

			@Override
			public Iterator<VertexConcurrency> iterator() {
				return iterator(0L);
			}
		};
	}

}
