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
public abstract class Tag extends AbstractNode {
	
	public static final EvalType SKIP_BODY = EvalTypeImplementation.SKIP_BODY;
	public static final EvalType EVAL_BODY = EvalTypeImplementation.EVAL_BODY;
	public static final EvalType EVAL_ELSE = EvalTypeImplementation.EVAL_ELSE;

	private String tagName;

	public Tag() {
	}
	
	public void start(Context context, Writer sb) {
	}

	public void end(Context context, Writer sb) {
	}

	@Override
	public abstract void eval(Context context, Writer sb);

	protected void loopBlockList(Node []blockList, Context context, Writer sb) {
		for (Node t : blockList) {
			t.eval(context, sb);
		}
	}
	
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
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
