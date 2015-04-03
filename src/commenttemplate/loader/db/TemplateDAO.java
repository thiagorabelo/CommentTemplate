package commenttemplate.loader.db;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author thiago
 */
public interface TemplateDAO<T extends Serializable> {

	public TemplateEntity getByKey(T key);
	
	public List<? extends TemplateEntity> list();
}
