package commenttemplate.template.tags;

import commenttemplate.template.tags.builtin.IfTag;
import commenttemplate.template.tags.builtin.SetTag;
import commenttemplate.template.tags.builtin.WithTag;
import commenttemplate.template.tags.builtin.extend.ExtendsTag;
import commenttemplate.template.tags.builtin.extend.BlockTag;
import commenttemplate.template.tags.builtin.customcomponent.ForComponent;
import commenttemplate.template.tags.builtin.customcomponent.WithComponent;
import commenttemplate.util.Utils;

/**
 *
 * @author thiago
 */
public class TagInitializer {
	
	private static Boolean configured = false;
	
	static {
		instance = new TagInitializer();
	}
	
	public static void config() {
		synchronized (configured) {
			if (!configured) {
				instance().initBuildin();
				configured = true;
			}
		}
	}
	
	private static final TagInitializer instance;
	
	public static TagInitializer getInstance() {
		return instance;
	}

	public static TagInitializer instance() {
		return instance;
	}
	
	private TagInitializer() {
	}
	
	
	private void initBuildin() {
		TagContainer.instance().addBuiltinTag(new ForComponent());
		TagContainer.instance().addBuiltinTag(new WithComponent());
		TagContainer.instance().addBuiltinTag(new TagComponent("if", IfTag.class, "!test"));
		TagContainer.instance().addBuiltinTag(new TagComponent("set", SetTag.class, "!var", "value"));
//		TemplateTag.addBuiltinTag(new ValueFormatterTemplateTag());
		TagContainer.instance().addBuiltinTag(new TagComponent("extends", ExtendsTag.class,  "!name"));
		TagContainer.instance().addBuiltinTag(new TagComponent("block", BlockTag.class, "!name"));
	}
	
	public void addTag(TagComponent component) {
		if (component != null && !Utils.empty(component.getName()) && component.getTagClass() != null) {
			TagContainer.instance().addCustomTag(component);
		}
	}
}
