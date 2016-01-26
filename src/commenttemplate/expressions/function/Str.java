package commenttemplate.expressions.function;

import java.util.List;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.context.Context;

/**
 *
 * @author thiago
 */
public class Str extends Function {

	@Override
	public Object eval(Context context) {
		List<Exp> args = getArgs();
		StringBuilder sb = new StringBuilder();

		for (Exp arg : args) {
			Object evaluated = arg.eval(context);
			sb.append(evaluated.toString());
		}

		return sb.toString();
	}
}
