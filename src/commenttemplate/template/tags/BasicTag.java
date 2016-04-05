package commenttemplate.template.tags;

import commenttemplate.template.tags.consequence.ConsequenceImplementation;
import commenttemplate.context.Context;
import commenttemplate.template.nodes.AbstractNode;
import commenttemplate.template.nodes.Node;
import commenttemplate.template.writer.Writer;
import commenttemplate.util.Join;
import commenttemplate.util.Utils;
import java.util.Arrays;
import java.util.Iterator;
import commenttemplate.template.tags.consequence.Consequence;

/**
 *
 * @author thiago
 */
public abstract class BasicTag extends AbstractNode implements Tag {
	
	public static final Consequence SKIP_BODY = ConsequenceImplementation.SKIP_BODY;
	public static final Consequence EVAL_BODY = ConsequenceImplementation.EVAL_BODY;
	public static final Consequence EVAL_ELSE = ConsequenceImplementation.EVAL_ELSE;

	private String tagName;

	public BasicTag() {
	}

	@Override
	public void start(Context context, Writer sb) {
	}

	@Override
	public void end(Context context, Writer sb) {
	}

	@Override
	public abstract void eval(Context context, Writer sb);

	@Override
	public void render(Context context, Writer sb) {
		start(context, sb);
		eval(context, sb);
		end(context, sb);
	}
	
	@Override
	public String getTagName() {
		return tagName;
	}

	@Override
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	@Override
	public String paramsToString() {
		String []params = TagFactoryContainer.instance().getByTagName(tagName).getParams();

		return Join.with(" ").skipNulls().these(new Iterator<String>() {

			private final Iterator<String> it = Arrays.asList(params).iterator();

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public String next() {
				String p = it.next();
				Object param = Utils.getProperty(BasicTag.this, p, true);
				return param != null ? Utils.concat(p, "=\"", param.toString(), '"') : null;
			}

		}).toString();
	}
	
	@Override
	public void toString(StringBuilder sb) {
		sb.append("<!--").append(tagName).append(" ").append(paramsToString()).append("-->");
		
		Node []l;
		
		if ((l = getNodeList()) != null) {
			for (Node n : l) {
				n.toString(sb);
			}
		}
		
		if ((l = getNodeListElse()) != null) {
			sb.append("<!--else-->");
			for (Node n : l) {
				n.toString(sb);
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
