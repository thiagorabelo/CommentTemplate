package commenttemplate.template.tags.tags;

import commenttemplate.template.tags.AbstractTag;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.writer.TemplateWriter;
import commenttemplate.context.Context;
import commenttemplate.expressions.tree.Identifier;
import commenttemplate.template.nodes.Node;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class SetTag extends AbstractTag {
	
	private Exp var;
	private Exp value;
	
	public SetTag() {
	}

//	@Override
//	public void start(Context context, Writer sb) {
//		context.push();
//	}
//
//	@Override
//	public void end(Context context, Writer sb) {
//		context.pop();
//	}

	@Override
	public void eval(Context context, Writer sb) {
		Exp attr = value;

		Object n = var.eval(context);
		String name = n != null
			? n.toString()
			: var instanceof Identifier && ((Identifier)var).getKeys().length == 1
				? var.toString()
				: null
		;
		
		if (name != null && !name.equals("")) {
			Object result;
			Node []nodeList;

			if (attr != null) {
				result = attr.eval(context);
				context.put(name, result);
			} else if ((nodeList = getNodeList()) != null){
				TemplateWriter tw = new TemplateWriter();
				loopNodeList(nodeList, context, tw);

				if (!tw.isEmpty()) {
					context.put(name, tw.toString());
				} else {
					context.put(name, null);
				}
			} else {
				context.put(name, null);
			}
		}
	}

	public Exp getVar() {
		return var;
	}

	public void setVar(Exp var) {
		this.var = var;
	}

	public Exp getValue() {
		return value;
	}

	public void setValue(Exp value) {
		this.value = value;
	}
}
