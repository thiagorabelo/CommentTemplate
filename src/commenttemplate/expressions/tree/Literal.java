package commenttemplate.expressions.tree;

import commenttemplate.context.Context;

/**
 *
 * @author thiago
 */
public class Literal implements Token {
	Object val;
	
	public Literal() {
	}

	public Literal(Object val) {
		this.val = val;
	}

	public Object getVal() {
		return val;
	}

	public void setVal(Object val) {
		this.val = val;
	}
	
	@Override
	public Object eval(Context context) {
		return val;
	}
	
	@Override
	public String toString() {
		StringBuilder sb;
		toString(sb = new StringBuilder());
		return sb.toString();
	}

	@Override
	public void toString(StringBuilder sb) {
		if (val instanceof String) {
			sb.append("'").append(val).append("'");
		} else {
			sb.append(val);
		}
	}
}
