package commenttemplate.template.tags.tags;

import commenttemplate.expressions.primitivehandle.NumHandle;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.context.Context;
import commenttemplate.template.tags.ConditionalTag;
import static commenttemplate.template.tags.AbstractTag.EVAL_BODY;
import static commenttemplate.template.tags.AbstractTag.EVAL_ELSE;
import commenttemplate.template.writer.Writer;
import commenttemplate.template.tags.EvalType;

/**
 *
 * @author thiago
 */
public class IfTag extends ConditionalTag {

	private Exp test;

	public IfTag() {
	}

	protected boolean isBool(Object ob) {
		return ob instanceof Boolean;
	}

	protected boolean isNumber(Object obj) {
		return obj instanceof Number;
	}

	protected boolean isString(Object obj) {
		return obj instanceof String;
	}

	protected boolean isEmptyStr(String obj) {
		return ((String)obj).isEmpty();
	}
	
	public void setTest(Exp test) {
		this.test = test;
	}

	@Override
	public EvalType evalParams(Context context, Writer sb) {
		Exp exp = test;

		Object t = exp.eval(context);

		boolean b_test = (t != null) && (isBool(t) ? (Boolean)t :!((isNumber(t) && NumHandle.isZero((Number)t)) ^ (isString(t) && isEmptyStr((String)t))));

		return b_test ? EVAL_BODY : EVAL_ELSE;
	}
}
