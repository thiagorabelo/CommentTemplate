package commenttemplate.template.tags;

import java.util.HashMap;
import java.util.regex.Pattern;
import commenttemplate.expressions.exceptions.BadExpression;
import commenttemplate.expressions.exceptions.ExpectedExpression;
import commenttemplate.expressions.exceptions.ExpectedOperator;
import commenttemplate.expressions.exceptions.FunctionDoesNotExists;
import commenttemplate.expressions.exceptions.Unexpected;
import commenttemplate.expressions.parser.Parser;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.AbstractTemplateBlock;
import commenttemplate.template.exceptions.TemplateTagDoesNotExists;
import commenttemplate.template.exceptions.TemplateWithSameNameAlreadyExistsException;
import commenttemplate.context.Context;
import commenttemplate.template.writer.Writer;
import static commenttemplate.util.Utils.concat;

/**
 *
 * @author thiago
 */
public abstract class TemplateTag {

	protected static final HashMap<String, TemplateTag> builtInTags = new HashMap<String, TemplateTag>();
	protected static final HashMap<String, TemplateTag> customTags = new HashMap<String, TemplateTag>();

	// Split as strings pelo caractere |. Se houver ||, não vai fazer o split neste ponto.
	private static Pattern splitParams = Pattern.compile("(?<!\\|)\\|(?!\\|)");

	public static final int SKIP_BODY = 0;
	public static final int EVAL_BODY = 1;
	public static final int EVAL_ELSE = 2;
	
	/* Futuramente usar esta enum em vez de inteiros
	protected static enum OnBody {
		SKIP_BODY,
		EVAL_BODY,
		EVAL_ELSE
	}
	*/

	protected static synchronized void addBuiltinTag(TemplateTag tag) {
		if (!builtInTags.containsKey(tag.getTagName())) {
			builtInTags.put(tag.getTagName(), tag);
		} else {
			String className = builtInTags.get(tag.getTagName()).getClass().getName();
			throw new TemplateWithSameNameAlreadyExistsException(concat("A built in TemplateTag whith the same name (", tag.getTagName(), ") already exists: ", className));
		}
	}

	public static synchronized void addCustomTag(TemplateTag tag) {
		if (builtInTags.containsKey(tag.getTagName())) {
			String className = builtInTags.get(tag.getTagName()).getClass().getName();
			throw new TemplateWithSameNameAlreadyExistsException(concat("A built in TemplateTag whith the same name (", tag.getTagName(), ") already exists: [", className, "]"));
		} else if (customTags.containsKey(tag.getTagName())) {
			String className = customTags.get(tag.getTagName()).getClass().getName();
			throw new TemplateWithSameNameAlreadyExistsException(concat("A custom TemplateTag whith the same name (", tag.getTagName(), ") already exists: [", className, "]"));
		}

		customTags.put(tag.getTagName(), tag);
	}

	public static TemplateTag getByTagName(String tagName) {
		TemplateTag tag;
		if (((tag = builtInTags.get(tagName)) != null) || ((tag = customTags.get(tagName)) != null)) {
			return tag;
		}

		throw new TemplateTagDoesNotExists(concat("There is no ", tagName, " registred."));
	}

	public static Exp defaultEvalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected, FunctionDoesNotExists {
		Parser p = new Parser(expression);
		return p.parse();
	}

	public static String []splitParams(String str) {
		String []params = splitParams.split(str);

		for (int i = 0, len = params.length; i < len; i++) {
			params[i] = params[i].trim();
		}

		return params;
	}

	private final String tagName;

	public TemplateTag(String tagName) {
		this.tagName = tagName;
	}

	public String getTagName() {
		return tagName;
	}

	public abstract Exp evalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected, FunctionDoesNotExists;

	public abstract TemplateTag getNewInstance();

	public abstract boolean hasOwnContext();

	public abstract int evalParams(AbstractTemplateBlock block, Context context, Writer sb);

	public void eval(AbstractTemplateBlock block, Context context, Writer sb) {
		int whomEvaluate = evalParams(block, context, sb);
		AbstractTemplateBlock inner = block.getNextInner();
		AbstractTemplateBlock innerElse = block.getNextInnerElse();

		switch (whomEvaluate) {
			case EVAL_BODY:
				if (inner != null) {
					boolean hasOwnContext;

					if (hasOwnContext = hasOwnContext()) {
						context.push();
					}

					evalBody(block, context, sb);

					if (hasOwnContext) {
						context.pop();
					}
				}
				break;

			case EVAL_ELSE:
				if (innerElse != null) {
					boolean hasOwnContext;

					if (hasOwnContext = hasOwnContext()) {
						context.push();
					}

					evalElse(block, context, sb);

					if (hasOwnContext) {
						context.pop();
					}
				}
				break;

			case SKIP_BODY:
				break;

			default:
				break;
		}

		AbstractTemplateBlock next = block.getNext();

		if (next != null) {
			next.eval(context, sb);
		}
	}

	protected void evalBody(AbstractTemplateBlock block, Context context, Writer sb) {
		AbstractTemplateBlock inner = block.getNextInner();

		if (inner != null) {
			inner.eval(context, sb);
		}
	}

	protected void evalElse(AbstractTemplateBlock block, Context context, Writer sb) {
		AbstractTemplateBlock innerElse = block.getNextInnerElse();

		if (innerElse != null) {
			innerElse.eval(context, sb);
		}
	}
}
