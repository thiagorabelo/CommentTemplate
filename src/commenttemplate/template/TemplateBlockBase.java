package commenttemplate.template;

import commenttemplate.template.writer.TemplateWriter;
import java.util.Map;
import commenttemplate.context.Context;
import commenttemplate.context.ContextPreprocessor;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class TemplateBlockBase extends TemplateBlock {

	private int initialBufferSize;

	public TemplateBlockBase() {
		initialBufferSize = 128;
	}

	public TemplateBlockBase(int initialBufferSize) {
		this.initialBufferSize = initialBufferSize;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		toString(sb);

		return sb.toString();
	}

	@Override
	public void toString(StringBuilder sb) {
		if (getNextInner() != null) {
			getNextInner().toString(sb);

			if (getNextInnerElse() != null) {
				getNextInnerElse().toString(sb);
			}
		}

		if (getNext() != null) {
			getNextInner().toString(sb);
		}
	}

	public String eval(Map<String, Object> params) {
		TemplateWriter sb = new TemplateWriter(initialBufferSize);

		if (params instanceof Context) {
			eval((Context)params, sb);
		} else {
			eval(params, sb);
		}

		return sb.toString();
	}

	public void eval(Map<String, Object> params, Writer sb) {
		Context context = new Context(params);
		eval(context, sb);
	}

	@Override
	public void eval(Context context, Writer sb) {
		
		// TODO: Ver o caso desta exceção
		// TODO: O uso das tags extends e block, estão causando múltiplas chamadas aos preprocessadores
		try {
			for (Class<? extends ContextPreprocessor> cls : ContextPreprocessor.preprocessors) {
				cls.newInstance().execute(context);
			}
		} catch (InstantiationException | IllegalAccessException ex) {}

		getNextInner().eval(context, sb);
	}
}
