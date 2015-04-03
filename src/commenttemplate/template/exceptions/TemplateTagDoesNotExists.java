package commenttemplate.template.exceptions;

/**
 *
 * @author thiago
 */
public class TemplateTagDoesNotExists extends RuntimeException {
	public TemplateTagDoesNotExists(String msg) {
		super(msg);
	}
}
