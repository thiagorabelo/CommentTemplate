package commenttemplate.template;

import commenttemplate.template.writer.TemplateWriter;
import java.util.Map;
import commenttemplate.context.Context;
import commenttemplate.context.ContextPreprocessor;
import commenttemplate.context.preprocessor.PreprocessorCache;
import commenttemplate.template.writer.Writer;
import java.util.ArrayList;
import java.util.List;

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
		List<TemplateBlock> blockList = getBlockList();

		for (TemplateBlock t : blockList) {
			t.toString(sb);
		}
	}

	public void slimLists() {
		
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
		
		for (ContextPreprocessor p : PreprocessorCache.instance()) {
			p.before(context);
		}

		List<TemplateBlock> list = getBlockList();
		
		for (int i = 0, len = list.size(); i < len; i++) {
			list.get(i).eval(context, sb);
		}

		for (ContextPreprocessor p : PreprocessorCache.instance()) {
			p.after(sb, context);
		}
	}
}
