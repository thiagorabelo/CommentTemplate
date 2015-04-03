package commenttemplate.template.tags;

import commenttemplate.template.tags.builtin.ForEachTemplateTag;
import commenttemplate.template.tags.builtin.IfTemplateTag;
import commenttemplate.template.tags.builtin.SetTemplateTag;
import commenttemplate.template.tags.builtin.extend.ExtendsTemplateTag;
import commenttemplate.template.tags.builtin.extend.BlockTemplateTag;

/**
 *
 * @author thiago
 */
public class TemplateTagInitializer {
	
	private static Boolean configured = false;
	
	static {
		instance = new TemplateTagInitializer();
	}
	
	public static void config() {
		synchronized (configured) {
			if (!configured) {
				instance().initBuildin();
				configured = true;
			}
		}
	}
	
	private static final TemplateTagInitializer instance;
	
	public static TemplateTagInitializer getInstance() {
		return instance;
	}

	public static TemplateTagInitializer instance() {
		return instance;
	}
	
	private TemplateTagInitializer() {
	}
	
	
	private void initBuildin() {
		TemplateTag.addBuiltinTag(new IfTemplateTag());
		TemplateTag.addBuiltinTag(new ForEachTemplateTag());
		TemplateTag.addBuiltinTag(new SetTemplateTag());
//		TemplateTag.addBuiltinTag(new ValueFormatterTemplateTag());
		TemplateTag.addBuiltinTag(new ExtendsTemplateTag());
		TemplateTag.addBuiltinTag(new BlockTemplateTag());
	}
	
	public void addTag(TemplateTag tag) {
		if (tag != null) {
			TemplateTag.addCustomTag(tag);
		}
	}
}
