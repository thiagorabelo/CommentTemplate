package commenttemplate.template.tags.builtin.extend;

import commenttemplate.expressions.exceptions.BadExpression;
import commenttemplate.expressions.exceptions.ExpectedExpression;
import commenttemplate.expressions.exceptions.ExpectedOperator;
import commenttemplate.expressions.exceptions.FunctionDoesNotExists;
import commenttemplate.expressions.exceptions.Unexpected;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.AbstractTemplateBlock;
import commenttemplate.context.Context;
import commenttemplate.context.ContextWriterMap;
import commenttemplate.template.TemplateBlock;
import commenttemplate.template.tags.TemplateTag;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class BlockTemplateTag extends TemplateTag {
	
	public static final int EVAL_WRITER = 4;
	public static final int EVAL_BODY_WITH_MAPPED_WRITER = 5;
	
	public BlockTemplateTag() {
		super("block");
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

	@Override
	public int evalParams(AbstractTemplateBlock block, Context context, Writer sb) {
		ContextWriterMap cwm = (ContextWriterMap)context;
		
		if (cwm.getMode() == ContextWriterMap.Mode.STORE) {
			return EVAL_BODY_WITH_MAPPED_WRITER;
		} else if (sb != null && !sb.isEmpty()) {
			return EVAL_WRITER;
		}
		
		return EVAL_BODY;
	}
	
	@Override
	public void eval(AbstractTemplateBlock block, Context context, Writer sb) {
		if (context instanceof ContextWriterMap) {
			ContextWriterMap cwm = (ContextWriterMap)context;
			TemplateBlock actualBlock = (TemplateBlock) block;
			Exp exp = actualBlock.getParams();
			
			String blockName = exp.eval(context).toString();
			Writer w = cwm.getWriter(blockName);
			
			AbstractTemplateBlock inner;
			int whomEvaluate = evalParams(block, cwm, w);
			
			switch (whomEvaluate) {
				case EVAL_BODY_WITH_MAPPED_WRITER:
					inner = block.getNextInner();
					if (inner != null) {
						boolean hasOwnContext;

						if (hasOwnContext = hasOwnContext()) {
							context.push();
						}

						evalBody(block, cwm, w);

						if (hasOwnContext) {
							context.pop();
						}
					}
					break;

				case EVAL_WRITER:
					sb.append(w.toString());
					break;

				case EVAL_BODY:
					inner = block.getNextInner();
					if (inner != null) {
						boolean hasOwnContext;

						if (hasOwnContext = hasOwnContext()) {
							context.push();
						}

						evalBody(block, cwm, sb);

						if (hasOwnContext) {
							context.pop();
						}
					}
					break;

				default:
					break;
			}
			
			AbstractTemplateBlock next = block.getNext();

			if (next != null) {
				next.eval(context, sb);
			}
		}
	}
}
