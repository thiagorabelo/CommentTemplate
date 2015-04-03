package commenttemplate.expressions.operators.logical;

import commenttemplate.expressions.operators.core.UnaryOperator;
import commenttemplate.context.Context;


/**
 *
 * @author thiago
 */
public class Not extends UnaryOperator {
	
	public static Object boolCase(Object bool) {
		return !(Boolean)bool;
	}

	public static Object objectCase(Object object) {
		return eqNull(object);
	}
	
	public static Object execute(Object param) {
		return isBool(param) ? boolCase(param) : objectCase(param);
	}

	@Override
	public Object eval(Context context) {
		Object param = getParam().eval(context);

		return execute(param);
	}
	
	@Override
	public String toString() {
		return "!";
	}
}
