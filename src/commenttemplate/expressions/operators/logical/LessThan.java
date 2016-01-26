package commenttemplate.expressions.operators.logical;

import commenttemplate.expressions.operators.core.BinaryOperator;
import commenttemplate.expressions.primitivehandle.Infinity;
import commenttemplate.expressions.primitivehandle.NumHandle;
import commenttemplate.context.Context;


/**
 *
 * @author thiago
 */
public class LessThan extends BinaryOperator {
	
	protected static boolean lessThan(Object l, Object r) {
		Number [] nums = NumHandle.toLongOrDouble(l, r);
		
		if (NumHandle.isDouble(nums[0])) {
			return ((Double)nums[0]) < ((Double)nums[1]);
		} else { // Long
			return ((Long)nums[0]) < ((Long)nums[1]);
		}
	}
	
	protected static boolean _lessThan(Number l, Number r) {
		if (NumHandle.isInfinity(l)) {
			if (NumHandle.isInfinity(r)) {
				return l.equals(r)
					? false
					: ((Infinity)l).isNegative();
			} else {
				return ((Infinity)l).isNegative();
			}
		} else if (NumHandle.isInfinity(r)) {
			return !((Infinity)r).isNegative();
		} else {
			return lessThan(l, r);
		}
	}
	
	/*
	 *  null <  null - false
	 *  null < !null - true
	 * 
	 *  Num  <  Num  - TEST
	 * !Num  <  Num  - false
	 * !Num  < !Num  - false
	 *  Num  < !Num  - false
	 */
	public static boolean execute(Object l, Object r) {
		if (l == null) {
			return r != null;
		}

		if (isNum(l) && isNum(r)) {
			return _lessThan((Number) l, (Number) r);
		} else {
			return false;
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
		return "<";
	}
}
