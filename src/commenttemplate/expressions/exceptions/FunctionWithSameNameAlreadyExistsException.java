package commenttemplate.expressions.exceptions;

/**
 *
 * @author thiago
 */
public class FunctionWithSameNameAlreadyExistsException extends RuntimeException {
	
	public FunctionWithSameNameAlreadyExistsException(String msg) {
		super(msg);
	}
}
