package commenttemplate.expressions.tree;

import commenttemplate.context.Context;
import commenttemplate.util.Join;
import java.util.regex.Pattern;

/**
 *
 * @author thiago
 */
public class Identifier implements Token {
	// @TODO: Não usar mais regex. Usar algum Tokenizer.
	private static final Pattern splitter = Pattern.compile("\\s*\\.\\s*");
	
	private String [] keys;
	
	public Identifier() {
	}

	public Identifier(String id) {
		setId(id);
	}

	public final void setId(String id) {
		keys = splitter.split(id);
	}

	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	@Override
	public Object eval(Context context) {
		return context.getValue(keys);
	}

	@Override
	public String toString() {
		return Join.with(".").these(keys).toString();
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(Join.with(".").these(keys).toString());
	}
}