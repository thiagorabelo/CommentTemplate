package commenttemplate.loader;

import commenttemplate.loader.db.TemplateDAO;

/**
 *
 * @author thiago
 */
public abstract class TemplateLoaderConfig {
	
	private LoaderTypeInterface type;
	private TemplateDAO dao;
	private String [] foldersPath;
	
	public TemplateLoaderConfig() {
	}

	public void setType(LoaderType type) {
		this.type = type;
	}

	public LoaderTypeInterface getType() {
		return type;
	}

	public TemplateDAO getDao() {
		return dao;
	}

	public void setDao(TemplateDAO dao) {
		this.dao = dao;
	}

	public String [] getFolderPath() {
		return foldersPath;
	}

	public String [] getPaths() {
		return foldersPath;
	}

	public void setFolderPath(String ...foldersPath) {
		this.foldersPath = foldersPath;
	}

	public abstract void init() throws Exception ;
}
