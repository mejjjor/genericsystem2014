package org.genericsystem.api.exception;

/**
 * @author middleware
 */
public class AliveConstraintViolationException extends ConstraintViolationException {

	private static final long serialVersionUID = -4066409595001566155L;

	public AliveConstraintViolationException(String message) {
		super(message);
	}

}
