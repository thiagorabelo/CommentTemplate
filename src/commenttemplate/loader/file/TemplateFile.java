package commenttemplate.loader.file;

import commenttemplate.loader.TemplateStream;

/**
 *
 * @author thiago
 */
public class TemplateFile implements TemplateStream {
	
	private String key;
	
	private String source;
	
	public TemplateFile() {
	}
	
	public TemplateFile(String key, String source) {
		this.key = key;
		this.source = source;
	}
	
	public TemplateFile(String key) {
		this.key = key;
		
		// @TODO: Load Soruce
	}

	@Override
	public String getKey() {
		return key;
	}
	
	// @TODO: // implementar esta bagaça
	public String getPath() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
