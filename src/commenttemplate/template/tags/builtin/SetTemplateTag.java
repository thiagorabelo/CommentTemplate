package commenttemplate.template.tags.builtin;

import commenttemplate.template.tags.TemplateTag;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.writer.TemplateWriter;
import commenttemplate.context.Context;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class SetTemplateTag extends TemplateTag {
	
	private Exp varName;
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

		String name = this.varName.eval(context).toString();
		
		if (name != null && !name.equals("")) {
			Object result;

			if (attr != null) {
				result = attr.eval(context);
				context.put(name, result);
			} else if (getNextInner() != null){
				TemplateWriter tw = new TemplateWriter();
				evalBody(context, tw);

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

	public Exp getVarName() {
		return varName;
	}

	public void setVarName(Exp varName) {
		this.varName = varName;
	}

	public Exp getValue() {
		return value;
	}

	public void setValue(Exp value) {
		this.value = value;
	}
}
