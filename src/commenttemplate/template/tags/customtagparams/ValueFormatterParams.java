package commenttemplate.template.tags.customtagparams;

import java.util.Map;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.context.Context;

/**
 *
 * @author thiago
 */
public class ValueFormatterParams implements TagParams {
	
	private Exp object;
	private Exp formatter;
	private Exp max;
	
	public ValueFormatterParams(Exp object, Exp formatter, Exp max) {
		this.object = object;
		this.formatter = formatter;
		this.max = max;
	}

	@Override
	public Object eval(Context context) {
		return this;
	}

	@Override
	public void toString(StringBuilder sb) {
		// @TODO: vai dar null pointer se um dos Exp for null;
		object.toString(sb);
		sb.append(" | ");
		formatter.toString(sb);
		sb.append(" | ");
		max.toString(sb);
	}

	public Exp getObject() {
		return object;
	}

	public Exp getFormatter() {
		return formatter;
	}

	public Exp getMax() {
		return max;
	}
}
