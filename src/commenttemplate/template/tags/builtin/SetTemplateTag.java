package commenttemplate.template.tags.builtin;

import commenttemplate.template.tags.TemplateTag;
import commenttemplate.expressions.exceptions.BadExpression;
import commenttemplate.expressions.exceptions.ExpectedExpression;
import commenttemplate.expressions.exceptions.ExpectedOperator;
import commenttemplate.expressions.exceptions.FunctionDoesNotExists;
import commenttemplate.expressions.exceptions.Unexpected;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.AbstractTemplateBlock;
import commenttemplate.template.TemplateBlock;
import commenttemplate.template.writer.TemplateWriter;
import commenttemplate.template.tags.customtagparams.SetTagParams;
import commenttemplate.context.Context;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class SetTemplateTag extends TemplateTag {
	
	public SetTemplateTag() {
		super("set");
	}

	@Override
	public TemplateTag getNewInstance() {
		return this;
	}

	@Override
	public boolean hasOwnContext() {
		return false;
	}

	@Override
	public int evalParams(AbstractTemplateBlock block, Context context, Writer sb) {
		TemplateBlock actualBlock = (TemplateBlock) block;
		SetTagParams params = (SetTagParams)actualBlock.getParams();
		
		Exp attr = params.getAttribute();
		
		String varName = params.getVarName();
		
		if (varName != null && !varName.equals("")) {
			Object result;

			if (attr != null) {
				result = attr.eval(context);
				context.put(varName, result);
			} else if (actualBlock.getNextInner() != null){
				TemplateWriter tw = new TemplateWriter();
				evalBody(block, context, tw);

				if (!tw.isEmpty()) {
					context.put(varName, tw.toString());
				} else {
					context.put(varName, null);
				}
			} else {
				context.put(varName, null);
			}
		}
		
		return SKIP_BODY;
	}

	@Override
	public Exp evalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected, FunctionDoesNotExists {
		String [] attrs = splitParams(expression);
		
		if (attrs != null) {
			if (attrs.length >= 2) {
				return new SetTagParams(attrs[0], defaultEvalExpression(attrs[1]));
			} else {
				return new SetTagParams(attrs[0], null);
			}
		} else {
			return new SetTagParams(null, null);
		}
	}
}
