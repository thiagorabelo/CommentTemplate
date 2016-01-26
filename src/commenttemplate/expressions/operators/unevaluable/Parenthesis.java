package commenttemplate.expressions.operators.unevaluable;

import commenttemplate.context.Context;
import commenttemplate.expressions.operators.core.Operator;

/**
 *
 * @author thiago
 */
public abstract class Parenthesis extends Operator {

	@Override
	public Object eval(Context context) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
