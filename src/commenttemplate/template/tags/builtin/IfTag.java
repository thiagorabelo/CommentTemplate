package commenttemplate.template.tags.builtin;

import commenttemplate.template.tags.Tag;
import commenttemplate.expressions.primitivehandle.NumHandle;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.context.Context;
import commenttemplate.template.tags.TypeEval;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class IfTag extends Tag {

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

	@Override
	public TypeEval evalParams(Context context, Writer sb) {
		Exp exp = test;

		Object t = exp.eval(context);

		boolean b_test = (t != null) && (isBool(t) ? (Boolean)t :!((isNumber(t) && NumHandle.isZero((Number)t)) ^ (isString(t) && isEmptyStr((String)t))));

		return b_test ? EVAL_BODY : EVAL_ELSE;
	}
}
