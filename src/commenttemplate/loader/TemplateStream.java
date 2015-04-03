package commenttemplate.loader;

import java.io.Serializable;

/**
 *
 * @author thiago
 */
public interface TemplateStream {
	public Serializable getKey();
	public String getSource();
}
