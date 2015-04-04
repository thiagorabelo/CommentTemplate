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

	private Exp name;

	public BlockTemplateTag() {
	}

	@Override
	public int evalParams(Context context, Writer sb) {
		ContextWriterMap cwm = (ContextWriterMap)context;
		
		if (cwm.getMode() == ContextWriterMap.Mode.STORE) {
			return EVAL_BODY_WITH_MAPPED_WRITER;
		} else if (sb != null && !sb.isEmpty()) {
			return EVAL_WRITER;
		}
		
		return EVAL_BODY;
	}
	
	@Override
	public void eval(Context context, Writer sb) {
		if (context instanceof ContextWriterMap) {
			ContextWriterMap cwm = (ContextWriterMap)context;
			Exp exp = name;
			
			String blockName = exp.eval(context).toString();
			Writer w = cwm.getWriter(blockName);
			
			AbstractTemplateBlock inner;
			int whomEvaluate = evalParams(cwm, w);
			
			switch (whomEvaluate) {
				case EVAL_BODY_WITH_MAPPED_WRITER:
					inner = getNextInner();
					if (inner != null) {
						context.push();
						evalBody(cwm, w);
						context.pop();
					}
					break;

				case EVAL_WRITER:
					sb.append(w.toString());
					break;

				case EVAL_BODY:
					inner = getNextInner();
					if (inner != null) {
						context.push();
						evalBody(cwm, sb);
						context.pop();
					}
					break;

				default:
					break;
			}

			AbstractTemplateBlock next = getNext();

			if (next != null) {
				next.eval(context, sb);
			}
		}
	}

	public Exp getName() {
		return name;
	}

	public void setName(Exp name) {
		this.name = name;
	}
}
