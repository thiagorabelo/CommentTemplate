package commenttemplate.template.tags.builtin.extend;

import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.TemplateBlock;
import commenttemplate.context.Context;
import commenttemplate.context.ContextWriterMap;
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
			String blockName = name.eval(context).toString();
			Writer w = cwm.getWriter(blockName);
			
			TemplateBlock []blockList;
			int whomEvaluate = evalParams(cwm, w);
			
			switch (whomEvaluate) {
				case EVAL_BODY_WITH_MAPPED_WRITER:
					blockList = getBlockList();
					if (blockList != null) {
						context.push();
						loopBlockList(blockList, cwm, w);
						context.pop();
					}
					break;

				case EVAL_WRITER:
					sb.append(w.toString());
					break;

				case EVAL_BODY:
					blockList = getBlockList();
					if (blockList != null) {
						context.push();
						loopBlockList(blockList, cwm, sb);
						context.pop();
					}
					break;

				default:
					break;
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
