package commenttemplate.expressions.function;

import commenttemplate.expressions.operators.numerical.Power;
import commenttemplate.context.Context;

/**
 *
 * @author thiago
 */
public class Pow extends ExecuteFunction {

	public Object execute(Context context, Number a, Number b) {		
		return Power.execute(a, b);
	}
}
