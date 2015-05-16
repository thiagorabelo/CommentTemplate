package commenttemplate.template.tags;

import commenttemplate.template.TemplateBlock;
import commenttemplate.context.Context;
import commenttemplate.template.writer.Writer;
import commenttemplate.util.Join;
import commenttemplate.util.Utils;
import java.util.ArrayList;

/**
 *
 * @author thiago
 */
public abstract class Tag extends TemplateBlock {

	public static enum TypeEvalTag implements TypeEval {

		SKIP_BODY {
			@Override
			public void doEval(Tag tag, Context context, Writer sb) {
				// do nothing
			}
		},
		EVAL_BODY {
			@Override
			public void doEval(Tag tag, Context context, Writer sb) {
				TemplateBlock []blockList;

				if ((blockList = tag.getBlockList()) != null) {
					tag.start(context, sb);
					tag.loopBlockList(blockList, context, sb);
					tag.end(context, sb);
				}
			}
		},
		EVAL_ELSE {
			@Override
			public void doEval(Tag tag, Context context, Writer sb) {
				TemplateBlock []blockListElse;

				if ((blockListElse = tag.getBlockListElse()) != null) {
					tag.start(context, sb);
					tag.loopBlockList(blockListElse, context, sb);
					tag.end(context, sb);
				}
			}
		}
		;

		@Override
		public void doEval(Tag tag, Context context, Writer sb) {
			throw new UnsupportedOperationException("Not supported.");
		}
		
	}
	
	public static final TypeEval SKIP_BODY = TypeEvalTag.SKIP_BODY;
	public static final TypeEval EVAL_BODY = TypeEvalTag.EVAL_BODY;
	public static final TypeEval EVAL_ELSE = TypeEvalTag.EVAL_ELSE;
	
	private String tagName;

	public Tag() {
	}
	
	public void start(Context context, Writer sb) {
	}

	public void end(Context context, Writer sb) {
	}

	public abstract TypeEval evalParams(Context context, Writer sb);
	
	protected void loopBlockList(TemplateBlock []blockList, Context context, Writer sb) {
		for (TemplateBlock t : blockList) {
			t.eval(context, sb);
		}
	}

	@Override
	public void eval(Context context, Writer sb) {
		TypeEval type = evalParams(context, sb);
		type.doEval(this, context, sb);
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	public String paramsToString() {
		String []params = TagContainer.instance().getByTagName(tagName).getParams();
		ArrayList<String> l = new ArrayList<String>();
		
		for (String p : params) {
			Object param = Utils.getProperty(this, p, true);
			if (param != null) {
				l.add(Utils.concat(p, "=\"", param.toString(), '"'));
			}
		}
		
		return Join.with(" ").join(l).toString();
	}
	
	@Override
	public void toString(StringBuilder sb) {
		sb.append("<!--").append(tagName).append(" ").append(paramsToString()).append("-->");
		
		TemplateBlock []l;
		
		if ((l = getBlockList()) != null) {
			for (TemplateBlock t : l) {
				t.toString(sb);
			}
		}
		
		if ((l = getBlockListElse()) != null) {
			sb.append("<!--else-->");
			for (TemplateBlock t : l) {
				t.toString(sb);
			}
		}

		sb.append("<!--end").append(tagName).append("-->");
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString(sb);
		return sb.toString();
	}
}
