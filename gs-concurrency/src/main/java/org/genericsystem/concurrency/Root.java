package org.genericsystem.concurrency;

import java.io.Serializable;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

import org.genericsystem.kernel.Statics;

public class Root extends Vertex implements IRoot<Vertex, Root> {

	private final TsGenerator generator = new TsGenerator();

	private final IEngine<?, ?, Vertex, Root> engine;

	private final GarbageCollector<Vertex, Root> garbageCollector;

	Root(IEngine<?, ?, Vertex, Root> engine, Serializable value) {
		init(false, null, Collections.emptyList(), value, Collections.emptyList());
		this.engine = engine;
		long ts = pickNewTs();
		restore(ts, 0L, 0L, Long.MAX_VALUE);
		garbageCollector = new GarbageCollector<>(this);
	}

	@Override
	public Root getRoot() {
		return this;
	}

	@Override
	public Root getAlive() {
		return this;
	}

	@Override
	public boolean isRoot() {
		return true;
	}

	@Override
	public long pickNewTs() {
		return generator.pickNewTs();
	}

	@Override
	public IEngine<?, ?, Vertex, Root> getEngine() {
		return engine;
	}

	@Override
	public GarbageCollector<Vertex, Root> getGarbageCollector() {
		return garbageCollector;
	}

	static class TsGenerator {
		private final long startTime = System.currentTimeMillis() * Statics.MILLI_TO_NANOSECONDS - System.nanoTime();
		private final AtomicLong lastTime = new AtomicLong(0L);

		long pickNewTs() {
			long nanoTs;
			long current;
			for (;;) {
				nanoTs = startTime + System.nanoTime();
				current = lastTime.get();
				if (nanoTs - current > 0)
					if (lastTime.compareAndSet(current, nanoTs))
						return nanoTs;
			}
		}
	}

}
