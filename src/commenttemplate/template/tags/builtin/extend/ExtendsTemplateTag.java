package commenttemplate.template.tags.builtin.extend;

import commenttemplate.expressions.tree.Exp;
import commenttemplate.loader.TemplateLoader;
import commenttemplate.template.TemplateBlock;
import commenttemplate.template.TemplateBlockBase;
import commenttemplate.template.exceptions.TemplateException;
import commenttemplate.context.Context;
import commenttemplate.template.tags.TemplateTag;
import commenttemplate.context.ContextWriterMap;
import commenttemplate.template.writer.Writer;
import java.util.ArrayList;
import java.util.List;

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
	
	@Override
	public void append(TemplateBlock other) {
		ArrayList<TemplateBlock> blockList;

		if ((blockList = getBlockList()) == null) {
			setBlockList(blockList = new ArrayList<>());
		}

		if (other instanceof BlockTemplateTag) {
			blockList.add(other);
		}
	}

	/*
	
	*/
	@Override
	public void eval(Context context, Writer sb) {
		try {
			Exp exp = name;

			String templateName = exp.eval(context).toString();
			TemplateBlockBase base = TemplateLoader.get(templateName);

			List<TemplateBlock> inner = getBlockList();
			ContextWriterMap cwm = new ContextWriterMap(context);

			cwm.setMode(ContextWriterMap.Mode.STORE);

			if (inner != null) {
				loopBlockList(inner, cwm, sb);
			}

			cwm.setMode(ContextWriterMap.Mode.RENDER);
			base.eval(cwm, sb);

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
