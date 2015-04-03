package commenttemplate.expressions.tree;

import commenttemplate.context.Context;

/**
 *
 * @author thiago
 */
public interface Exp {

	public Object eval(Context context);

	public void toString(StringBuilder sb);
}
