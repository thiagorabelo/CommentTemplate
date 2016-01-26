package commenttemplate.template.nodes;

import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.exceptions.TemplateException;
import commenttemplate.context.Context;
import commenttemplate.expressions.tree.Literal;
import commenttemplate.template.TemplateParser;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class TextNode extends AbstractNode {
	
	public static class PlainText extends Literal {
		public PlainText(String text) {
			super(text);
		}

		@Override
		public String toString() {
			return getVal().toString();
		}
	};
	
	private Exp []content;
	
	public TextNode() {
	}
	
	public Exp []getContent() {
		return content;
	}

	public void setContent(String content) throws TemplateException {
		this.content = TemplateParser.getContent(content);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (Object ob : content) {
			if (ob instanceof PlainText) {
				sb.append(ob);
			} else {
				sb.append("${");
				((Exp)ob).toString(sb);
				sb.append("}");
			}
		}

		return sb.toString();
	}
	
	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.toString());
	}

	// Falta ajeitar essa onça
	@Override
	public void render(Context context, Writer sb) {
		for (Exp exp : content) {
			sb.append(exp.eval(context));
		}
	}
}
