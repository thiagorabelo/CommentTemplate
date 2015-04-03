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
public class Positive extends UnaryOperator {

	public static Object execute(Object param) {
		if (isNum(param)) {
			return !NumHandle.isInfinity(param)
				? positive(param)
				: (Infinity)param;
		} else {
			return Const.NAN;
		}
	}

	protected static Number positive(Object param) {
		Number n = NumHandle.toLongOrDouble(param);
		
		if (NumHandle.isDouble(n)) {
			return +((Double)n);
		} else { // Long
			return +((Long)n);
		}
	}

	@Override
	public Object eval(Context context) {
		Object param = getParam().eval(context);
		
		return execute(param);
	}
	
	@Override
	public String toString() {
		return "+";
	}
}
