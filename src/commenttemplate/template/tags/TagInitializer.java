package commenttemplate.template.tags;

import commenttemplate.template.tags.factory.TagFactory;
import commenttemplate.template.tags.factory.MappableTagFactory;
import commenttemplate.template.tags.tags.IfTag;
import commenttemplate.template.tags.tags.IncludeTag;
import commenttemplate.template.tags.tags.Literal;
import commenttemplate.template.tags.tags.SetTag;
import commenttemplate.template.tags.tags.WithTag;
import commenttemplate.template.tags.tags.ExtendsTag;
import commenttemplate.template.tags.tags.BlockTag;
import commenttemplate.template.tags.factory.ForTagFactory;
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
		TagFactoryContainer.instance().addBuiltinTag(new ForTagFactory());
//		TagContainer.instance().addBuiltinTag(new WithComponent());
		TagFactoryContainer.instance().addBuiltinTag(new MappableTagFactory("with", WithTag.class));
		TagFactoryContainer.instance().addBuiltinTag(new MappableTagFactory("include", IncludeTag.class,  "!template"));
		TagFactoryContainer.instance().addBuiltinTag(new TagFactory("if", IfTag.class, "!test"));
		TagFactoryContainer.instance().addBuiltinTag(new TagFactory("set", SetTag.class, "!var", "value"));
		TagFactoryContainer.instance().addBuiltinTag(new TagFactory("literal", Literal.class));
//		TemplateTag.addBuiltinTag(new ValueFormatterTemplateTag());
		TagFactoryContainer.instance().addBuiltinTag(new TagFactory("extends", ExtendsTag.class,  "!name"));
		TagFactoryContainer.instance().addBuiltinTag(new TagFactory("block", BlockTag.class, "!name"));
	}
	
	public void addTag(TagFactory component) {
		if (component != null && !Utils.empty(component.getName()) && component.getTagClass() != null) {
			TagFactoryContainer.instance().addCustomTag(component);
		}
	}
}
