package commenttemplate.expressions.operators.unevaluable;

/**
 *
 * @author thiago
 */
public class RParenthesis extends Parenthesis {

	@Override
	public String toString() {
		return ")";
	}

	@Override
	public String getRepr() {
		return ")";
	}

	@Override
	public void toString(StringBuilder sb) {
	}
}
