package commenttemplate.template.tags;

import commenttemplate.context.Context;
import commenttemplate.template.nodes.AbstractNode;
import commenttemplate.template.nodes.Node;
import commenttemplate.template.writer.Writer;
import commenttemplate.util.Join;
import commenttemplate.util.Utils;
import java.util.ArrayList;

/**
 *
 * @author thiago
 */
public abstract class AbstractTag extends AbstractNode implements Tag {
	
	public static final EvalType SKIP_BODY = EvalTypeImplementation.SKIP_BODY;
	public static final EvalType EVAL_BODY = EvalTypeImplementation.EVAL_BODY;
	public static final EvalType EVAL_ELSE = EvalTypeImplementation.EVAL_ELSE;

	private String tagName;

	public AbstractTag() {
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

	protected void loopNodeList(Node []blockList, Context context, Writer sb) {
		for (Node t : blockList) {
			t.render(context, sb);
		}
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
		ArrayList<String> l = new ArrayList<String>();
		
		for (String p : params) {
			Object param = Utils.getProperty(this, p, true);
			if (param != null) {
				l.add(Utils.concat(p, "=\"", param.toString(), '"'));
			}
		}
		
		return Join.with(" ").these(l).toString();
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
