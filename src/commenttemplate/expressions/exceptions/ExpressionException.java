package commenttemplate.expressions.exceptions;

import commenttemplate.util.Utils;

/**
 * An exception super class inherited for exceptions thrown by classes packed
 * in expressions package.
 *
 * @author thiago
 */
public abstract class ExpressionException extends Exception {

	/**
	 * Concatenate objects into a single string.
	 * 
	 * @param objs A list of object to be converted into string and be
	 * concatenated
	 * @return Result of concatenated objects converted into a String.
	 */
	public static String concat(Object ...objs) {
		return Utils.concat(objs);
	}

	/**
	 * The position in the string that represents the expression where the error
	 * occurs.
	 */
	private int index = -1;
	
	/**
	 * The String that represents the expression.
	 */
	private String expression;
	
	/**
	 * Cache to String builded by {@code ExpressionException.show()} method that
	 * that points to the error in the String that represents the expression.
	 * 
	 * @see commenttemplate.expressions.exceptions.ExpressionException#show()
	 */
	private String pointError;
	
	/**
	 * Create exeception with full information about the error.
	 * 
	 * @param expression The expression that error occurs.
	 * @param index The index position where error occurs in the expression.
	 * @param msg A custom error mensage.
	 */
	public ExpressionException(String expression, int index, String msg) {
		this(msg);
		this.expression = expression;
		this.index = index;
	}
	
	/**
	 * Default constructor with no one information.
	 */
	public ExpressionException() {
		super();
	}

	/**
	 * Justa a custom mensage.
	 * 
	 * @param msg A custom mensage.
	 */
	public ExpressionException(String msg) {
		super(msg);
	}

	/**
	 * Return a string that points to the error in the expression.
	 * Ex:
	 * <pre>
	 * a + b 2
	 *     ^
	 * </pre>
	 * 
	 * @return A string that points to the error in the expression
	 */
	public String show() {
		
		if (pointError == null && index >= 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(expression).append("\n");
			for (int i = 0; i < index; i++) {
				sb.append(" ");
			}
			
			pointError = sb.append("^\n").toString();
		}

		return pointError;
	}
}
