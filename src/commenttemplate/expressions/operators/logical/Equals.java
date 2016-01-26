package commenttemplate.expressions.operators.logical;

import commenttemplate.expressions.operators.core.BinaryOperator;
import commenttemplate.expressions.primitivehandle.Const;
import commenttemplate.expressions.primitivehandle.NumHandle;
import commenttemplate.context.Context;

/**
 *
 * @author thiago
 */
public class Equals extends BinaryOperator {
	
	@Override
	public String getRepr() {
		return "==";
	}
	
	public static boolean execute(Object left, Object right) {
		if (!eqNull(left)) {
			if (!eqNull(right)) {
				if (isNum(left) && isNum(right)) {
					Number [] nums = NumHandle.toLongOrDouble(left, right);

					return nums[0].equals(nums[1]);
				} else {
					return left.equals(right);
				}
			} else {
				return false;
			}
		} else {
			return eqNull(right);
		}
	}

	@Override
	public Object eval(Context context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);

		return execute(left, right);
	}
}
