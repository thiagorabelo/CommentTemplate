package commenttemplate.template.tags.tags;

import commenttemplate.expressions.tree.Exp;
import commenttemplate.loader.TemplateLoader;
import commenttemplate.template.exceptions.TemplateException;
import commenttemplate.context.Context;
import commenttemplate.context.ContextWriterMap;
import commenttemplate.template.tags.BasicTag;
import commenttemplate.template.tags.MountingHelper;
import commenttemplate.template.nodes.Node;
import commenttemplate.template.nodes.RootNode;
import commenttemplate.template.tags.adaptor.TagAdaptor;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class ExtendsTag extends BasicTag {
	
	private Exp name;
	
	@Override
	public void eval(Context context, Writer sb) {
		try {
			Exp exp = name;

			String templateName = exp.eval(context).toString();
			RootNode base = TemplateLoader.get(templateName);

			ContextWriterMap cwm = new ContextWriterMap(context);

			cwm.setMode(ContextWriterMap.Mode.STORE);

			EVAL_BODY.doEval(this, cwm, sb);

			cwm.setMode(ContextWriterMap.Mode.RENDER);
			base.render(cwm, sb);

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
	
	private class MountingExtendsHelper extends MountingHelper {

		public MountingExtendsHelper(Node b) {
			super(b);
		}

		@Override
		public void append(Node other) {
			if (other instanceof BlockTag || (other instanceof TagAdaptor && ((TagAdaptor)other).isAssignableOf(BlockTag.class))) {
				super.append(other);
			}
		}
	}

	@Override
	public MountingHelper createMountingHelper() {
		return createMountingHelper(this);
	}

	@Override
	public MountingHelper createMountingHelper(Node node) {
		return new MountingExtendsHelper(node);
	}
}
