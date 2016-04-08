package commenttemplate.expressions.exceptions;

/**
 *
 * @author thiago
 */
// @TODO: RuntimeException?
public class FunctionWithSameNameAlreadyExistsException extends RuntimeException {
	
	/**
	 * Justa a custom mensage.
	 * 
	 * @param msg A custom mensage.
	 */
	public FunctionWithSameNameAlreadyExistsException(String msg) {
		super(msg);
	}
}
