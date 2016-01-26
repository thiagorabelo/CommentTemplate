package commenttemplate.loader;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import commenttemplate.template.exceptions.TemplateException;
import commenttemplate.context.Context;
import commenttemplate.template.nodes.RootNode;
import commenttemplate.template.tags.TagInitializer;
import commenttemplate.util.retrieve.RecursiveRetrieveDataMap;

/**
 *
 * @author thiago
 */
public class TemplateLoader {
	
	static {
		Init.config();
		TagInitializer.config();
	}
	
	public static synchronized void loadtTemplate(Serializable key) throws TemplateException {
		TemplateStream ts = Loader.instance().source(key);
		TemplateCache.loadtTemplate(ts);
	}

	public static RootNode get(Serializable key) throws TemplateException {
		RootNode t = TemplateCache.getTemplateBlock(key);

		if (t == null) {
			loadtTemplate(key);
			t = TemplateCache.getTemplateBlock(key);
		}

		return t;
	}
	
	public static String render(Serializable key, Map<String, Object> params, boolean recursive) throws TemplateException {
		RootNode t = get(key);

		String templateOutput;
		if (!recursive) {
			templateOutput = t.render(params != null ? params : new HashMap<String, Object>());
		} else {
			Context context = new Context(params, new RecursiveRetrieveDataMap());
			templateOutput = t.render(context);
		}

		return templateOutput;
	}
	
	public static String render(Serializable key, Map<String, Object> params) throws TemplateException {
		return render(key, params, false);
	}

	public static String render(String chave) throws TemplateException {
		return render(chave, null, false);
	}
}
