package commenttemplate.expressions.operators.logical;

import commenttemplate.expressions.operators.core.BinaryOperator;
import commenttemplate.context.Context;

/**
 *
 * @author thiago
 */
public class GreaterThan extends BinaryOperator {
	
	@Override
	public String getRepr() {
		return ">";
	}

	@Override
	public Object eval(Context context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);

		return !LessEqualsThan.execute(left, right);
	}
}
