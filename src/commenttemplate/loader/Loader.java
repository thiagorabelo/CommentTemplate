package commenttemplate.loader;

import java.io.Serializable;

/**
 *
 * @author thiago
 */
public class Loader {
	
	private static Loader instance;
	
	public static void initLoader(TemplateRetrieve retriever) {
		instance = new Loader(retriever);
	}
	
	public static Loader instance() {
		if (instance != null) {
			return instance;
		}
		
		throw new ExceptionInInitializerError("Loader not initialized");
	}

//--------------------------------------------------------------------------------------------------
	
	private TemplateRetrieve retriever;
	
	// @TODO: FAZER USO DO CACHE
	
	protected Loader(TemplateRetrieve retriever) {
		this.retriever = retriever;
	}
	
	public TemplateStream source(Serializable key) {		
		return retriever.get(key);
	}
}
