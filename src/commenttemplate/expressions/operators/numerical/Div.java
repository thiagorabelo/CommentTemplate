package commenttemplate.expressions.operators.numerical;

import commenttemplate.expressions.operators.core.BinaryOperator;
import commenttemplate.expressions.primitivehandle.Const;
import commenttemplate.expressions.primitivehandle.NumHandle;
import commenttemplate.context.Context;

/**
 *
 * @author thiago
 */
public class Div extends BinaryOperator {
	
	public static Number execute(Object l, Object r) {
		if (isNum(l) && isNum(r)) {
			if (NumHandle.isInfinity(l)) {
				return NumHandle.isInfinity(r) ? Const.NAN : (Number) l;
			} else if (NumHandle.isInfinity(r)) {
				return 0;
			} else {
				try {
					Number n = div(l, r);

					if (!(NumHandle.isDouble(r) && Double.isInfinite((Double)n))) {
						return n;
					} else {
						return isNegative(l) ? Const._INFINITY : Const.INFINITY;
					}
				} catch (ArithmeticException ex) {
					return isNegative(l) ? Const._INFINITY : Const.INFINITY;
				}
			}
		} else {
			return Const.NAN;
		}
	}

	protected static Number div(Object l, Object r) {
		Number [] nums = NumHandle.toLongOrDouble(l, r);
		
		if (NumHandle.isDouble(nums[0])) {
			return ((Double)nums[0]) / ((Double)nums[1]);
		} else {
			return ((Long)nums[0]) / ((Long)nums[1]);
		}
	}

	@Override
	public Object eval(Context context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);

		return execute(left, right);
	}

	@Override
	public String getRepr() {
		return "/";
	}
}
