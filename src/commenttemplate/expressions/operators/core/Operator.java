package commenttemplate.expressions.operators.core;

import commenttemplate.expressions.primitivehandle.Const;
import commenttemplate.expressions.primitivehandle.Infinity;
import commenttemplate.expressions.primitivehandle.NaN;
import commenttemplate.expressions.primitivehandle.NumHandle;
import commenttemplate.expressions.tree.Exp;


/**
 *
 * @author thiago
 * 
 */
public abstract class Operator implements Exp {
	
	public static boolean eqNull(Object o) {
		return o == null || o == Const.NULL;
	}
	
	public static boolean isNull(Object o) {
		return o == null;
	}
	
	public static boolean isBool(Object o) {
		return o instanceof Boolean;
	}

	public static boolean isNum(Object o) {
		return o instanceof Number && !(o instanceof NaN);
	}
	
	public static boolean isString(Object o) {
		return o instanceof String;
	}

	public static boolean isNegative(Object o) {
		if (!NumHandle.isInfinity(o)) {
			if (NumHandle.isDoubleOrFloat(o)) {
				Double d = NumHandle.floatToDouble(o);
				
				return d < 0;
			} else {
				Long l = NumHandle.intShortByteToLong(o);
				
				return l < 0;
			}
		} else {
			return ((Infinity)o).isNegative();
		}
	}
	
	public Operator () {
	}
	
	@Override
	public abstract String toString();
	
	@Override
	public abstract void toString(StringBuilder sb);

	public abstract String getRepr();
}
