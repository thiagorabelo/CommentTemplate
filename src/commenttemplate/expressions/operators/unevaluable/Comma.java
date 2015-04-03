package commenttemplate.expressions.operators.unevaluable;

import commenttemplate.expressions.operators.core.Operator;
import commenttemplate.context.Context;

/**
 *
 * @author thiago
 */
public class Comma extends Operator {
	
	@Override
	public String toString() {
		return ",";
	}

	@Override
	public void toString(StringBuilder sb) {
	}
	
	@Override
	public Object eval(Context context) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
