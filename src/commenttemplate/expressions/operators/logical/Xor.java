package commenttemplate.expressions.operators.logical;

import commenttemplate.expressions.operators.core.BinaryOperator;
import commenttemplate.context.Context;
import commenttemplate.expressions.primitivehandle.NumHandle;


/**
 *
 * @author thiago
 */
public class Xor extends BinaryOperator {

	@Override
	public Object eval(Context context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);
		
		return execute(left, right);
	}

	public static Object execute(Object left, Object right) {
		if (isBool(left) && isBool(right)) {
				return ((Boolean)left) ^ ((Boolean)right);
		} if (NumHandle.isLongOrIntOrShortOrByte(left) && NumHandle.isLongOrIntOrShortOrByte(right)) {
			return NumHandle.intShortByteToLong(left) ^ NumHandle.intShortByteToLong(right);
		}else {
			Object l = And.execute(left, Not.execute(right));
			Object r = And.execute(Not.execute(left), right);

			return Or.execute(l, r);
		}
	}

	@Override
	public String toString() {
		return "^";
	}
}
