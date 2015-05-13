package commenttemplate.expressions.exceptions;

import commenttemplate.util.Utils;

/**
 *
 * @author thiago
 */
public abstract class ExpressionException extends Exception {

	public static String concat(Object ...objs) {
		return Utils.concat(objs);
	}

	private int index;
	private String expression;
	private String pointError;
	
	public ExpressionException(String expression, int index, String msg) {
		this(msg);
		this.expression = expression;
		this.index = index;
	}
	
	public ExpressionException() {
		super();
	}

	public ExpressionException(String msg) {
		super(msg);
	}

	public String show() {
		
		if (pointError == null) {
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
