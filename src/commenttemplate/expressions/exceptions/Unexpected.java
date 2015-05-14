package commenttemplate.expressions.exceptions;

/**
 *
 * @author thiago
 */
public class Unexpected extends ExpressionException {

	/**
	 * Justa a custom mensage.
	 * 
	 * @param msg A custom mensage.
	 */
	public Unexpected(String msg) {
		super(msg);
	}
	
	/**
	 * Create exeception with full information about the error.
	 * 
	 * @param expression The expression that error occurs.
	 * @param index The index position where error occurs in the expression.
	 * @param msg A custom error mensage.
	 */
	public Unexpected(String expression, int index, Object ...msg) {
		super(expression, index, concat(msg));
	}
}
