package commenttemplate.expressions.operators.numerical;

import commenttemplate.expressions.operators.core.UnaryOperator;
import commenttemplate.expressions.primitivehandle.Const;
import commenttemplate.expressions.primitivehandle.Infinity;
import commenttemplate.expressions.primitivehandle.NumHandle;
import commenttemplate.context.Context;


/**
 *
 * @author thiago
 */
public class Negative extends UnaryOperator {

	public static Object execute(Object param) {
		if (isNum(param)) {
			return !NumHandle.isInfinity(param)
				? negative(param)
				: ((Infinity)param).isNegative() ? Const.INFINITY : Const._INFINITY;
		} else {
			return Const.NAN;
		}
	}

	protected static Number negative(Object param) {
		Number n = NumHandle.toLongOrDouble(param);
		
		if (NumHandle.isDouble(n)) {
			return -((Double)n);
		} else { // Long
			return -((Long)n);
		}
	}

	@Override
	public Object eval(Context context) {
		Object param = getParam().eval(context);
		
		return execute(param);
	}
	
	@Override
	public String toString() {
		return "-";
	}
}
