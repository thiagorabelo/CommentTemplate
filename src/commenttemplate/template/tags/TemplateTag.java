package commenttemplate.template.tags;

import commenttemplate.template.TemplateBlock;
import commenttemplate.context.Context;
import commenttemplate.template.writer.Writer;
import commenttemplate.util.Join;
import commenttemplate.util.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thiago
 */
public abstract class TemplateTag extends TemplateBlock {

	public static final int SKIP_BODY = 0;
	public static final int EVAL_BODY = 1;
	public static final int EVAL_ELSE = 2;
	
	/* Futuramente usar esta enum em vez de inteiros
	protected static enum OnBody {
		SKIP_BODY,
		EVAL_BODY,
		EVAL_ELSE
	}
	*/
	
	private String tagName;

	public TemplateTag() {
	}
	
	public void start(Context context, Writer sb) {
	}

	public void end(Context context, Writer sb) {
	}

	public abstract int evalParams(Context context, Writer sb);
	
	protected void loopBlockList(TemplateBlock []blockList, Context context, Writer sb) {
		for (TemplateBlock t : blockList) {
			t.eval(context, sb);
		}
	}

	@Override
	public void eval(Context context, Writer sb) {
		int whomEvaluate = evalParams(context, sb);
		TemplateBlock []blockList = getBlockList();
		TemplateBlock []blockListElse = getBlockListElse();

		switch (whomEvaluate) {
			case EVAL_BODY:
				if (blockList != null) {
					start(context, sb);

					loopBlockList(blockList, context, sb);

					end(context, sb);
				}
				break;

			case EVAL_ELSE:
				if (blockListElse != null) {
					start(context, sb);

					loopBlockList(blockListElse, context, sb);

					end(context, sb);
				}
				break;

			case SKIP_BODY:
				break;

			default:
				break;
		}
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
