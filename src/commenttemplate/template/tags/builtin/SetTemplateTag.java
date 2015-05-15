package commenttemplate.template.tags.builtin;

import commenttemplate.template.tags.TemplateTag;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.writer.TemplateWriter;
import commenttemplate.context.Context;
import commenttemplate.expressions.tree.Identifier;
import commenttemplate.template.TemplateBlock;
import commenttemplate.template.writer.Writer;
import java.util.List;

/**
 *
 * @author thiago
 */
public class SetTemplateTag extends TemplateTag {
	
	private Exp var;
	private Exp value;
	
	public SetTemplateTag() {
	}

	@Override
	public void start(Context context, Writer sb) {
		context.push();
	}

	@Override
	public void end(Context context, Writer sb) {
		context.pop();
	}

	@Override
	public int evalParams(Context context, Writer sb) {
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
			TemplateBlock []blockList;

			if (attr != null) {
				result = attr.eval(context);
				context.put(name, result);
			} else if ((blockList = getBlockList()) != null){
				TemplateWriter tw = new TemplateWriter();
				loopBlockList(blockList, context, sb);

				if (!tw.isEmpty()) {
					context.put(name, tw.toString());
				} else {
					context.put(name, null);
				}
			} else {
				context.put(name, null);
			}
		}
		
		return SKIP_BODY;
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
