package commenttemplate.expressions.operators.core;

import commenttemplate.expressions.tree.Exp;

/**
 *
 * @author thiago
 */
public abstract class BinaryOperator extends Operator {
	private Exp left;
	private Exp right;

	public BinaryOperator() {
		super();
	}

	public Exp getLeft() {
		return left;
	}

	public void setLeft(Exp left) {
		this.left = left;
	}

	public Exp getRight() {
		return right;
	}

	public void setRight(Exp right) {
		this.right = right;
	}
	
	@Override
	public void toString(StringBuilder sb) {
		Exp l = left;
		Exp r = right;
		
		sb.append("(");
		l.toString(sb);
		sb.append(" ").append(this.getRepr()).append(" ");
		r.toString(sb);
		sb.append(")");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString(sb);
		return sb.toString();
	}
}
