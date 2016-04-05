package commenttemplate.template.tags.tags;

import commenttemplate.expressions.tree.Exp;
import commenttemplate.context.Context;
import commenttemplate.context.ContextWriterMap;
import commenttemplate.template.tags.BasicTag;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class BlockTag extends BasicTag {
	
	private  enum ConsequenceBlock {

		RENDER_FROM_CACHED_WRITER {
			@Override
			public void doEval(BasicTag tag, ContextWriterMap context, Writer writer, Writer cachedWriter) {
				writer.append(cachedWriter.toString());
			}
		},

		EVAL_BLOCK_BODY_WITH_MAPPED_WRITER {
			@Override
			public void doEval(BasicTag tag, ContextWriterMap context, Writer sb, Writer cachedWriter) {
				BasicTag.EVAL_BODY.doEval(tag, context, cachedWriter);
			}
		},

		EVAL_BLOCK_BODY
		;

		public void doEval(BasicTag tag, ContextWriterMap context, Writer sb, Writer cachedWriter) {
			BasicTag.EVAL_BODY.doEval(tag, context, sb);
		}
	}

	private Exp name;

	public BlockTag() {
	}

	private ConsequenceBlock getConsequence(ContextWriterMap context, Writer sb) {

		if (context.getMode() == ContextWriterMap.Mode.STORE) {
			return ConsequenceBlock.EVAL_BLOCK_BODY_WITH_MAPPED_WRITER;
		} else if (sb != null && !sb.isEmpty()) {
			return ConsequenceBlock.RENDER_FROM_CACHED_WRITER;
		}

		return ConsequenceBlock.EVAL_BLOCK_BODY;
	}
	
	@Override
	public void eval(Context context, Writer writer) {
		if (context instanceof ContextWriterMap) {
			ContextWriterMap cwm = (ContextWriterMap)context;
			String blockName = name.eval(context).toString();
			Writer cachedWriter = cwm.getWriter(blockName);

			ConsequenceBlock type = getConsequence(cwm, cachedWriter);

			type.doEval(this, cwm, writer, cachedWriter);
		}
	}

	public Exp getName() {
		return name;
	}

	public void setName(Exp name) {
		this.name = name;
	}
}
