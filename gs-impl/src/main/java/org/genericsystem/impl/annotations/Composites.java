package org.genericsystem.impl.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The components of a generic.
 * 
 * @author Nicolas Feybesse
 * @author Michael Ory
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Composites {

	/**
	 * Returns the class of the components.
	 * 
	 * @return An array of class of the components.
	 */
	Class<?>[] value();
}
