package org.genericsystem.api;

import java.lang.reflect.InvocationTargetException;

/**
 * <tt>Engine</tt> is the initial point of access, where is instantiated the model and then the data. Engine is as much a service than a data. It is represented by a Generic and an interface.
 * 
 * <tt>Engine</tt> has two modes :
 * <ul>
 * <li>persistent : runs the system by registering physically the information,</li>
 * <li>in-memory : runs the system memory without a persistence mechanism.</li>
 * </ul>
 * To start <tt>Engine</tt> in a persistent mode, the directory used to store and retrieve your information must be specified.
 * <p>
 * When creating a new <tt>Engine</tt>, a <tt>Cache</tt> is started.
 * </p>
 * 
 * @see Cache
 * @see Generic
 */
public interface Engine extends Generic {

	/**
	 * Creates a root to the graph as a new engine in a persistent mode. Starts a cache.
	 * 
	 * @param directoryPath
	 *            the directoryPath where the model and data will be stored.
	 * @return a new engine stored on the directory path specified.
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * 
	 * @see Cache
	 */
	static Engine newRoot(String directoryPath) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		return ((Class<Engine>) Class.forName("org.genericsystem.core.EngineImpl")).getConstructor(Class[].class).newInstance().newEngine(directoryPath);
	}

	/**
	 * Creates a root to the graph as a new engine in a in-memory mode. Starts a cache.
	 * 
	 * @return a new engine used in-memory.
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * 
	 * @see Cache
	 */
	static Engine newInMemoryRoot() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		return ((Class<Engine>) Class.forName("org.genericsystem.core.EngineImpl")).getConstructor(Class[].class).newInstance().newInMemoryEngine();
	}

	/**
	 * Creates a new engine in a persistent mode and starts a cache.
	 * 
	 * @param directoryPath
	 *            the directoryPath where the model and data will be stored.
	 * @return a new engine stored on the directory path specified.
	 * 
	 * @see Cache
	 */
	Engine newEngine(String directoryPath);

	/**
	 * Creates a new engine in a in-memory mode and starts a cache.
	 * 
	 * @return a new engine used in-memory.
	 * 
	 * @see Cache
	 */
	Engine newInMemoryEngine();

	/**
	 * This method can be used to :
	 * <ul>
	 * <li>an in-memory engine to make it persistent,</li>
	 * <li>an engine already persistent to move the directory path.</li>
	 * </ul>
	 * 
	 * @param directoryPath
	 *            the directory path where the model and data will be stored
	 * @return engine with a new directoryPath
	 */
	Engine setDirectoryPath(String directoryPath);

	// close => detail
	/**
	 * Closes engine and does a last commit if engine is run as persistent.
	 */
	void close();

	/**
	 * Mounts a new cache encapsulated on the current cache.
	 * 
	 * @return a new cache encapsulated on the current cache.
	 */
	Cache mountNewCache();

	/**
	 * Get the current cache.
	 * 
	 * @return the current cache.
	 */
	// @throws CacheAwareException if no current cache.
	Cache getCurrentCache() /* throws CacheAwareException */;

	// FIXME : switchCache ?
}
