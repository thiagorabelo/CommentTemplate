package commenttemplate.expressions.operators.logical;

import commenttemplate.expressions.operators.core.BinaryOperator;
import commenttemplate.context.Context;
import commenttemplate.expressions.primitivehandle.NumHandle;

/**
 *
 * @author thiago
 */
public class And extends BinaryOperator {

	public static Object bool_bool(Object l_boolean, Object r_boolean) {
		return ((Boolean)l_boolean) && ((Boolean)r_boolean);
	}

	public static Object bool_object(Object l_boolean, Object r_object) {
		if (l_boolean != null) {
			if ((Boolean)l_boolean) {
				return r_object != null ? r_object : null;
			}

			return false;
		}
		
		return null;
	}

	public static Object object_bool(Object l_object, Object r_boolean) {
		return (l_object != null && r_boolean != null) ? r_boolean : null;
	}

	public static Object object_object(Object l_object, Object r_object) {
		return (l_object != null && r_object != null) ? r_object : null;
	}
	
	public static Object execute(Object left, Object right) {
		if (!(NumHandle.isLongOrIntOrShortOrByte(left) && NumHandle.isLongOrIntOrShortOrByte(right))) {
			if (isBool(left)) {
				if (isBool(right)) {
					return bool_bool(left, right);
				} else {
					return bool_object(left, right);
				}
			} else { // isBool(left) -> false
				if (isBool(right)) {
					return object_bool(left, right);
				} else {
					return object_object(left, right);
				}
			}
		} else {
			return NumHandle.intShortByteToLong(left) & NumHandle.intShortByteToLong(right);
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
		return "&&";
	}
}
