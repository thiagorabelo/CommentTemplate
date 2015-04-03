package commenttemplate.expressions.function;

import java.util.List;
import commenttemplate.expressions.operators.numerical.Power;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.context.Context;

/**
 *
 * @author thiago
 */
public class Pow extends Function {

	@Override
	public Object eval(Context context) {
		List<Exp> args = getArgs();
		Exp a = args.get(0), b = args.get(1);
		
		return Power.execute(a.eval(context), b.eval(context));
	}
}
