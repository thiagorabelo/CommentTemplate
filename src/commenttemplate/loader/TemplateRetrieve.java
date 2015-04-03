package commenttemplate.loader;

import java.io.Serializable;

/**
 *
 * @author thiago
 */
public interface TemplateRetrieve {

	// @TODO: VER CASO DE TEMPLATE NÃO ENCONTRADO
	public TemplateStream get(Serializable key);
}
