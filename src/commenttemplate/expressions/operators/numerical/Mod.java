package commenttemplate.expressions.operators.numerical;

import commenttemplate.expressions.operators.core.BinaryOperator;
import commenttemplate.expressions.primitivehandle.Const;
import commenttemplate.expressions.primitivehandle.NumHandle;
import commenttemplate.context.Context;


/**
 *
 * @author thiago
 */
public class Mod extends BinaryOperator {
	
	private static Number execute(Object l, Object r) {
		if (isNum(l) && isNum(r)) {
			if (NumHandle.isInfinity(l)) {
				return Const.NAN;
			} else if (NumHandle.isInfinity(r)) {
				return (Number) l;
			} else {
				return mod(l, r);
			}
		} else {
			return Const.NAN;
		}
	}
	
	protected static Number mod(Object l, Object r) {
		Number [] nums = NumHandle.toLongOrDouble(l, r);
		
		if (NumHandle.isDouble(nums[0])) {
			return ((Double)nums[0]) % ((Double)nums[1]);
		} else {
			return ((Long)nums[0]) % ((Long)nums[1]);
		}
	}

	@Override
	public Object eval(Context context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);

		return execute(left, right);
	}

	@Override
	public String toString() {
		return "%";
	}
}
