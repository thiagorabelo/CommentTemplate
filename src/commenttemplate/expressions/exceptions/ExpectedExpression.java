package commenttemplate.expressions.exceptions;

/**
 *
 * @author thiago
 */
public class ExpectedExpression extends ExpressionException {

	/**
	 * Justa a custom mensage.
	 * 
	 * @param msg A custom mensage.
	 */
	public ExpectedExpression(String msg) {
		super(msg);
	}
	
	/**
	 * Create exeception with full information about the error.
	 * 
	 * @param expression The expression that error occurs.
	 * @param index The index position where error occurs in the expression.
	 * @param msg A custom error mensage.
	 */
	public ExpectedExpression(String expression, int index, Object ...msg) {
		super(expression, index, concat(msg));
	}
}
