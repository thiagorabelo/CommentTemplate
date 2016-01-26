package commenttemplate.expressions.operators.core;

import commenttemplate.expressions.tree.Exp;

/**
 *
 * @author thiago
 */
public abstract class UnaryOperator extends Operator {
	private Exp param;

	public UnaryOperator() {
		super();
	}

	public Exp getParam() {
		return param;
	}

	public void setParam(Exp param) {
		this.param = param;
	}
	
	@Override
	public void toString(StringBuilder sb) {
		Exp p = param;
		
		sb.append(this.getRepr());
		sb.append("(");
		p.toString(sb);
		sb.append(")");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString(sb);
		return sb.toString();
	}
}
