package commenttemplate.context;

import commenttemplate.context.Context;
import commenttemplate.template.writer.TemplateWriter;
import commenttemplate.template.writer.Writer;
import java.util.HashMap;

/**
 *
 * @author thiago
 */
public class ContextWriterMap extends Context {
	
	public static enum Mode {
		STORE {
			@Override
			public Writer getWriter(ContextWriterMap context, String blockName) {
				Writer w = new TemplateWriter();
				context.writerMap.put(blockName, w);
				return w;
			}
		},
		RENDER {
			@Override
			public Writer getWriter(ContextWriterMap context, String blockName) {
				return context.writerMap.get(blockName);
			}
		};

		public Writer getWriter(ContextWriterMap context, String blockName) {
			throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
		}
	};


	private final HashMap<String, Writer> writerMap = new HashMap();
	private Mode mode = Mode.RENDER;
	
	public ContextWriterMap(Context context) {
		super(context);
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public Writer getWriter(String blockName) {
		return mode.getWriter(this, blockName);
	}
}
