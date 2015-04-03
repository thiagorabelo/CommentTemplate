package commenttemplate.loader.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import commenttemplate.loader.TemplateRetrieve;
import commenttemplate.loader.TemplateStream;
import commenttemplate.util.Join;
import commenttemplate.util.Utils;

/**
 *
 * @author thiago
 */
public class RetrieverFile implements TemplateRetrieve {
	
//	private final static String sep = File.separator;

	private String [] paths;
	
	public RetrieverFile(String [] paths) {
		this.paths = paths;
	}
	
	// @TODO: REVER ESTA EXCEPTION
	@Override
	public TemplateStream get(Serializable key) {
		try {
			for (int i = 0, len = paths.length; i < len;) {
				String fullPath = Join.path().join(paths[i], key.toString()).toString();

				try {
					String content = Utils.getContent(fullPath);
					return new TemplateFile(key.toString(), content);
				} catch (FileNotFoundException ex) {
					i += i;
				}
			}

			throw new RuntimeException(new FileNotFoundException(Utils.concat("Template not found: ", key)));
		} catch (IOException ex) {
			throw new RuntimeException(new FileNotFoundException(Utils.concat("Template not found: ", key)));
		}
	}
}
