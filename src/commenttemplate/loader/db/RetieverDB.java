package commenttemplate.loader.db;

import java.io.Serializable;
import commenttemplate.loader.TemplateRetrieve;
import commenttemplate.loader.TemplateStream;

/**
 *
 * @author thiago
 */
public class RetieverDB implements TemplateRetrieve {

	private TemplateDAO dao;

	public RetieverDB(TemplateDAO dao) {
		this.dao = dao;
	}

	@Override
	public TemplateStream get(Serializable key) {
		TemplateEntity t = dao.getByKey(key);

		// @TODO: FALTA VER CASO DO TEMPLATE NÃO ENCONTRADO

		return t;
	}
}
