package commenttemplate.template.tags.tags;

import commenttemplate.expressions.tree.Exp;
import commenttemplate.loader.TemplateLoader;
import commenttemplate.template.exceptions.TemplateException;
import commenttemplate.context.Context;
import commenttemplate.context.ContextWriterMap;
import commenttemplate.template.tags.Tag;
import commenttemplate.template.tags.MountingHelper;
import commenttemplate.template.nodes.Node;
import commenttemplate.template.nodes.RootNode;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class ExtendsTag extends Tag {
	
	private Exp name;
	
	@Override
	public void eval(Context context, Writer sb) {
		try {
			Exp exp = name;

			String templateName = exp.eval(context).toString();
			RootNode base = TemplateLoader.get(templateName);

			Node []inner = getNodeList();
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
	
	private class MountingExtendsHelper extends MountingHelper {

		public MountingExtendsHelper(Node b) {
			super(b);
		}

		@Override
		public void append(Node other) {
			if (other instanceof BlockTag) {
				super.append(other);
			}
		}
	}

	@Override
	public MountingHelper createMountingHelper() {
		return new MountingExtendsHelper(this);
	}
}
