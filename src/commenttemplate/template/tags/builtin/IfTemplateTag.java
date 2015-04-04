package commenttemplate.template.tags.builtin;

import commenttemplate.template.tags.TemplateTag;
import java.util.regex.Pattern;
import commenttemplate.expressions.exceptions.BadExpression;
import commenttemplate.expressions.exceptions.ExpectedExpression;
import commenttemplate.expressions.exceptions.ExpectedOperator;
import commenttemplate.expressions.exceptions.FunctionDoesNotExists;
import commenttemplate.expressions.exceptions.Unexpected;
import commenttemplate.expressions.primitivehandle.NumHandle;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.AbstractTemplateBlock;
import commenttemplate.template.TemplateBlock;
import commenttemplate.context.Context;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class IfTemplateTag extends TemplateTag {

	private Exp test;

	public IfTemplateTag() {
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
	public int evalParams(Context context, Writer sb) {
		Exp exp = test;

		Object t = exp.eval(context);

		boolean test = (t != null) && (isBool(t) ? (Boolean)t :!((isNumber(t) && NumHandle.isZero((Number)t)) ^ (isString(t) && isEmptyStr((String)t))));

		return test ? EVAL_BODY : EVAL_ELSE;
	}
}
