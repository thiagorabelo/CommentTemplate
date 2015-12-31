package commenttemplate.template.tags.tags;

import commenttemplate.expressions.tree.Exp;
import commenttemplate.context.Context;
import commenttemplate.context.ContextWriterMap;
import commenttemplate.template.tags.Tag;
import static commenttemplate.template.tags.Tag.EVAL_BODY;
import commenttemplate.template.tags.TypeEval;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class BlockTag extends Tag {
	
	private  enum TypeEvalBlock implements TypeEval {

		EVAL_WRITER,
		EVAL_BODY_WITH_MAPPED_WRITER,
		EVAL_BODY
		;

		@Override
		public void doEval(Tag tag, Context context, Writer sb) {
			EVAL_BODY.doEval(tag, context, sb);
		}
	}

	private Exp name;

	public BlockTag() {
	}

	private TypeEvalBlock params(Context context, Writer sb) {
		ContextWriterMap cwm = (ContextWriterMap)context;

		if (cwm.getMode() == ContextWriterMap.Mode.STORE) {
			return TypeEvalBlock.EVAL_BODY_WITH_MAPPED_WRITER;
		} else if (sb != null && !sb.isEmpty()) {
			return TypeEvalBlock.EVAL_WRITER;
		}

		return TypeEvalBlock.EVAL_BODY;
	}
	
	@Override
	public void eval(Context context, Writer sb) {
		if (context instanceof ContextWriterMap) {
			ContextWriterMap cwm = (ContextWriterMap)context;
			String blockName = name.eval(context).toString();
			Writer w = cwm.getWriter(blockName);

			TypeEvalBlock type = params(cwm, w);
			
			switch (type) {
				case EVAL_BODY_WITH_MAPPED_WRITER:
					EVAL_BODY.doEval(this, cwm, w);
					break;

				case EVAL_WRITER:
					sb.append(w.toString());
					break;

				case EVAL_BODY:
					EVAL_BODY.doEval(this, cwm, sb);
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
