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

	private static Pattern l_brackets = Pattern.compile("^\\[");
	private static Pattern r_brackets = Pattern.compile("\\]$");

	public IfTemplateTag() {
		super("if");
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
	public Exp evalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected, FunctionDoesNotExists {
		String ps = l_brackets.matcher(expression).replaceFirst("");
		ps = r_brackets.matcher(ps).replaceFirst("");

		return defaultEvalExpression(ps);
	}

	@Override
	public int evalParams(AbstractTemplateBlock block, Context context, Writer sb) {
		TemplateBlock actualBlock = (TemplateBlock) block;

		Exp exp = actualBlock.getParams();

		Object t = exp.eval(context);

		boolean test = (t != null) && (isBool(t) ? (Boolean)t :!((isNumber(t) && NumHandle.isZero((Number)t)) ^ (isString(t) && isEmptyStr((String)t))));

		return test ? EVAL_BODY : EVAL_ELSE;
	}

	@Override
	public TemplateTag getNewInstance() {
		return this;
	}

	@Override
	public boolean hasOwnContext() {
		return false;
	}
}
