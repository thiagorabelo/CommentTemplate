package commenttemplate.expressions.operators.logical;

import commenttemplate.expressions.operators.core.BinaryOperator;
import commenttemplate.context.Context;
import commenttemplate.expressions.primitivehandle.NumHandle;

/**
 *
 * @author thiago
 */
public class Or extends BinaryOperator {
	
	public static Object bool_bool(Object l_boolean, Object r_boolean) {
		return ((Boolean)l_boolean) || ((Boolean)r_boolean);
	}

	public static Object bool_object(Object l_boolean, Object r_object) {
		return (l_boolean != null && (Boolean)l_boolean) ? l_boolean : r_object;
	}

	public static Object object_bool(Object l_object, Object r_boolean) {
		return (l_object != null) ? l_object : r_boolean;
	}

	public static Object object_object(Object l_object, Object r_object) {
		return (l_object != null) ? l_object : r_object;
	}
	
	public static Object execute(Object left, Object right) {
		if (!(NumHandle.isLongOrIntOrShortOrByte(left) && NumHandle.isLongOrIntOrShortOrByte(right))) {
			if (isBool(left)) {
				if (isBool(right)) {
					return bool_bool(left, right);
				} else {
					return bool_object(left, right);
				}
			} else {
				if (isBool(right)) {
					return object_bool(left, right);
				} else {
					return object_object(left, right);
				}
			}
		} else {
			return NumHandle.intShortByteToLong(left) | NumHandle.intShortByteToLong(right);
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
		return "||";
	}
}
