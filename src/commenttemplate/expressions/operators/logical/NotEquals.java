package commenttemplate.expressions.operators.logical;

import commenttemplate.expressions.operators.core.BinaryOperator;
import commenttemplate.context.Context;


/**
 *
 * @author thiago
 */
public class NotEquals extends BinaryOperator {

	public static boolean execute(Object l, Object r) {
		return !Equals.execute(l, r);
	}

	@Override
	public Object eval(Context context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);

		return execute(left, right);
	}

	@Override
	public String getRepr() {
		return "!=";
	}
}
