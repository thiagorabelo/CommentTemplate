package commenttemplate.template.tags;

import commenttemplate.template.tags.builtin.ForEachTemplateTag;
import commenttemplate.template.tags.builtin.IfTemplateTag;
import commenttemplate.template.tags.builtin.SetTemplateTag;
import commenttemplate.template.tags.builtin.extend.ExtendsTemplateTag;
import commenttemplate.template.tags.builtin.extend.BlockTemplateTag;
import commenttemplate.template.tags.builtin.foreachhandles.ForEachComponent;
import commenttemplate.util.Utils;

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
		TagContainer.instance().addBuiltinTag(new ForEachComponent());
		TagContainer.instance().addBuiltinTag(new TagComponent("if", IfTemplateTag.class));
		TagContainer.instance().addBuiltinTag(new TagComponent("set", SetTemplateTag.class));
//		TemplateTag.addBuiltinTag(new ValueFormatterTemplateTag());
		TagContainer.instance().addBuiltinTag(new TagComponent("extends", ExtendsTemplateTag.class));
		TagContainer.instance().addBuiltinTag(new TagComponent("block", BlockTemplateTag.class));
	}
	
	public void addTag(TagComponent component) {
		if (component != null && !Utils.empty(component.getName()) && component.getTagClass() != null) {
			TagContainer.instance().addCustomTag(component);
		}
	}
}
