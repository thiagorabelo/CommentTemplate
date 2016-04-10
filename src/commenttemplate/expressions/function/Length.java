package commenttemplate.expressions.function;

import java.lang.reflect.Array;
import java.util.Collection;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.context.Context;

/**
 *
 * @author thiago
 */
public class Length extends Function {
	
	protected int length(Object sizeable) {
		if (sizeable instanceof Collection) {
			return ((Collection)sizeable).size();
		}
		
		if (sizeable instanceof String) {
			return ((String)sizeable).length();
		}
		
		if (sizeable.getClass().isArray()) {
			return Array.getLength(sizeable);
		}

		return 0;
	}

	@Override
	public Object eval(Context context) {
		Exp []args = getArgs();
		int length = 0;

		for (Exp arg : args) {
			Object evaluated = arg.eval(context);
			length += length(evaluated);
		}

		return length;
	}
}
