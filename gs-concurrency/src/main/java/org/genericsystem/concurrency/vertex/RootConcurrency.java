package org.genericsystem.concurrency.vertex;

import java.io.Serializable;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

import org.genericsystem.kernel.Root;
import org.genericsystem.kernel.Statics;

public class RootConcurrency extends Root {

	private final TsGenerator generator = new TsGenerator();

	public RootConcurrency() {
		this(Statics.ENGINE_VALUE);
	}

	public RootConcurrency(Serializable value) {
		init(null, Collections.emptyList(), value, Collections.emptyList());
	}

	public long pickNewTs() {
		return generator.pickNewTs();
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
				if (nanoTs > current)
					if (lastTime.compareAndSet(current, nanoTs))
						return nanoTs;
			}
		}
	}

}
