package commenttemplate.template.tags.builtin.extend;

import commenttemplate.expressions.exceptions.BadExpression;
import commenttemplate.expressions.exceptions.ExpectedExpression;
import commenttemplate.expressions.exceptions.ExpectedOperator;
import commenttemplate.expressions.exceptions.FunctionDoesNotExists;
import commenttemplate.expressions.exceptions.Unexpected;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.loader.TemplateLoader;
import commenttemplate.template.AbstractTemplateBlock;
import commenttemplate.template.TemplateBlock;
import commenttemplate.template.TemplateBlockBase;
import commenttemplate.template.exceptions.TemplateException;
import commenttemplate.context.Context;
import commenttemplate.template.tags.TemplateTag;
import commenttemplate.context.ContextWriterMap;
import commenttemplate.template.writer.VoidWriter;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class ExtendsTemplateTag extends TemplateTag {
	
	public ExtendsTemplateTag() {
		super("extends");
	}

	@Override
	public Exp evalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected, FunctionDoesNotExists {
		return defaultEvalExpression(expression);
	}

	@Override
	public TemplateTag getNewInstance() {
		return this;
	}

	@Override
	public boolean hasOwnContext() {
		return true;
	}

	/*
	
	*/
	@Override
	public int evalParams(AbstractTemplateBlock block, Context context, Writer sb) {
		return EVAL_BODY;
	}

	/*
	
	*/
	@Override
	public void eval(AbstractTemplateBlock block, Context context, Writer sb) {
		try {
			TemplateBlock actualBlock = (TemplateBlock) block;
			Exp exp = actualBlock.getParams();

			String templateName = exp.eval(context).toString();
			TemplateBlockBase base = TemplateLoader.get(templateName);

			AbstractTemplateBlock inner = block.getNextInner();
			VoidWriter vw = new VoidWriter();
			ContextWriterMap cwm = new ContextWriterMap(context);

			cwm.setMode(ContextWriterMap.Mode.STORE);

			if (inner != null) {
				inner.eval(cwm, vw);
			}

			cwm.setMode(ContextWriterMap.Mode.RENDER);
			base.eval(cwm, sb);

			AbstractTemplateBlock next = block.getNext();
			if (next != null) {
				next.eval(context, sb);
			}
		} catch (TemplateException ex) {
			// @TODO: fazer o quê?
			throw new RuntimeException(ex);
		}
	}
}
