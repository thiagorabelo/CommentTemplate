package commenttemplate.expressions.operators.unevaluable;

import java.util.Map;
import commenttemplate.expressions.operators.core.Operator;
import commenttemplate.context.Context;


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
