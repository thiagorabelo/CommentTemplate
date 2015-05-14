package commenttemplate.expressions.exceptions;

/**
 *
 * @author thiago
 */
public class ExpectedOperator extends ExpressionException {
	
	/**
	 * Justa a custom mensage.
	 * 
	 * @param msg A custom mensage.
	 */
	public ExpectedOperator(String msg) {
		super(msg);
	}
	
	/**
	 * Create exeception with full information about the error.
	 * 
	 * @param expression The expression that error occurs.
	 * @param index The index position where error occurs in the expression.
	 * @param msg A custom error mensage.
	 */
	public ExpectedOperator(String expression, int index, Object ...msg) {
		super(expression, index, concat(msg));
	}
}
