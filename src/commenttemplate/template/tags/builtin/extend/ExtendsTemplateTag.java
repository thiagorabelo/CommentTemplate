package commenttemplate.template.tags.builtin.extend;

import commenttemplate.expressions.tree.Exp;
import commenttemplate.loader.TemplateLoader;
import commenttemplate.template.AbstractTemplateBlock;
import commenttemplate.template.TemplateBlockBase;
import commenttemplate.template.exceptions.TemplateException;
import commenttemplate.context.Context;
import commenttemplate.template.tags.TemplateTag;
import commenttemplate.context.ContextWriterMap;
import commenttemplate.template.writer.VoidWriter;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class ExtendsTemplateTag extends TemplateTag {
	
	private Exp name;
	
	public ExtendsTemplateTag() {
	}

	/*
	
	*/
	@Override
	public int evalParams(Context context, Writer sb) {
		return EVAL_BODY;
	}

	/*
	
	*/
	@Override
	public void eval(Context context, Writer sb) {
		try {
			Exp exp = name;

			String templateName = exp.eval(context).toString();
			TemplateBlockBase base = TemplateLoader.get(templateName);

			AbstractTemplateBlock inner = getNextInner();
			VoidWriter vw = new VoidWriter();
			ContextWriterMap cwm = new ContextWriterMap(context);

			cwm.setMode(ContextWriterMap.Mode.STORE);

			if (inner != null) {
				inner.eval(cwm, vw);
			}

			cwm.setMode(ContextWriterMap.Mode.RENDER);
			base.eval(cwm, sb);

			AbstractTemplateBlock next = getNext();
			if (next != null) {
				next.eval(context, sb);
			}
		} catch (TemplateException ex) {
			// @TODO: fazer o quê?
			throw new RuntimeException(ex);
		}
	}

	public Exp getName() {
		return name;
	}

	public void setName(Exp name) {
		this.name = name;
	}
}
