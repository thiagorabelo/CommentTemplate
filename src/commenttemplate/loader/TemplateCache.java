package commenttemplate.loader;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import commenttemplate.template.TemplateBlockBase;
import commenttemplate.template.TemplateParser;
import commenttemplate.template.exceptions.TemplateException;
import commenttemplate.util.Utils;

/**
 *
 * @author thiago
 */
public class TemplateCache {
	
	private static Map<Serializable, TemplateBlockBase> templates = new HashMap<Serializable, TemplateBlockBase>();
	
	public static synchronized void loadTemplates(List<TemplateStream> lstTmpl) throws TemplateException {
		for (TemplateStream t : lstTmpl) {
			templates.put(t.getKey(), TemplateParser.compile(t.getSource()));
		}
	}
	
	public static synchronized void loadtTemplate(TemplateStream template) throws TemplateException {
		if (template != null && !Utils.empty(template.getKey().toString()) && !Utils.empty(template.getSource())) {
			try {
				templates.put(template.getKey(), TemplateParser.compile(template.getSource()));
			} catch (TemplateException ex) {
				throw new TemplateException(template.getKey().toString(), ex.getMsg(), ex.getLine(), ex.getError(), ex);
			}
		}
	}

	public static TemplateBlockBase getTemplateBlock(Serializable key) throws TemplateException {
		TemplateBlockBase t = templates.get(key);
		return t;
	}
}
