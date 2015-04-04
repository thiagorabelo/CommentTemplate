package commenttemplate.template.tags;

import commenttemplate.template.AbstractTemplateBlock;
import commenttemplate.context.Context;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public abstract class TemplateTag extends AbstractTemplateBlock {

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

	@Override
	public void eval(Context context, Writer sb) {
		int whomEvaluate = evalParams(context, sb);
		AbstractTemplateBlock inner = getNextInner();
		AbstractTemplateBlock innerElse = getNextInnerElse();

		switch (whomEvaluate) {
			case EVAL_BODY:
				if (inner != null) {
					start(context, sb);

					evalBody(context, sb);

					end(context, sb);
				}
				break;

			case EVAL_ELSE:
				if (innerElse != null) {
					start(context, sb);

					evalElse(context, sb);

					end(context, sb);
				}
				break;

			case SKIP_BODY:
				break;

			default:
				break;
		}

		AbstractTemplateBlock next = getNext();

		if (next != null) {
			next.eval(context, sb);
		}
	}

	protected void evalBody(Context context, Writer sb) {
		AbstractTemplateBlock inner = getNextInner();

		if (inner != null) {
			inner.eval(context, sb);
		}
	}

	protected void evalElse(Context context, Writer sb) {
		AbstractTemplateBlock innerElse = getNextInnerElse();

		if (innerElse != null) {
			innerElse.eval(context, sb);
		}
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	public String paramsToString() {
		return "";
	}
	
	@Override
	public void toString(StringBuilder sb) {
		if (getNextInner() != null) {
			sb.append("<!--").append(tagName).append(" ").append(paramsToString()).append("-->");
			getNextInner().toString(sb);

			if (getNextInnerElse() != null) {
				sb.append("<!--else-->");
				getNextInnerElse().toString(sb);
			}

			sb.append("<!--end").append(tagName).append("-->");
		}

		if (getNext() != null) {
			getNext().toString(sb);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString(sb);
		return sb.toString();
	}
}
