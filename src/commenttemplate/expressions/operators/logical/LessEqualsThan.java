package commenttemplate.expressions.operators.logical;

import commenttemplate.expressions.operators.core.BinaryOperator;
import commenttemplate.context.Context;

/**
 *
 * @author thiago
 */
public class LessEqualsThan extends BinaryOperator {
	
	public static boolean execute(Object l, Object r) {
		return LessThan.execute(l, r) || Equals.execute(l, r);
	}

	@Override
	public Object eval(Context context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);

		return execute(left, right);
	}

	@Override
	public String getRepr() {
		return "<=";
	}
}
