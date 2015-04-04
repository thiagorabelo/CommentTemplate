package commenttemplate.template.tags.builtin;

import commenttemplate.template.tags.TemplateTag;
import java.util.Iterator;
import java.util.List;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.tags.builtin.foreachhandles.NumericalList;
import commenttemplate.template.tags.builtin.foreachhandles.ObjectArrayIterator;
import commenttemplate.context.Context;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class ForEachTemplateTag extends TemplateTag {
	
	private Exp list;
	private Exp var;
	private Exp step;
	private Exp counter;

	public ForEachTemplateTag() {
	}

	protected int fromBuildingArray(Context context, NumericalList nlist, Writer sb) {
		int iterations = 0;

		for (int el : nlist) {
			if (var != null) {
				context.put(var.eval(context).toString(), el);
			}
			if (counter != null) {
				context.put(counter.eval(context).toString(), iterations);
			}

			evalBody(context, sb);

			iterations++;
		}

		return iterations;
	}

	protected int fromList(Context context, Object iterable, Writer sb) {
		if (iterable != null) {

			Iterator it = null;

			if (iterable.getClass().isArray()) {
				it = new ObjectArrayIterator(iterable);
			} else if (iterable instanceof List) {
				it = ((List)iterable).iterator();
			}

			if (it != null) {
				int i = 0;
				while (it.hasNext()) {
					Object el = it.next();

					if (var != null) {
						context.put(var.eval(context).toString(), el);
					}
					if (counter != null) {
						context.put(counter.eval(context).toString(), i);
					}

					evalBody(context, sb);

					i += 1;
				}

				return i;
			}
		}

		return 0;
	}

	@Override
	public int evalParams(Context context, Writer sb) {
		Exp result = list;
		int iterations;

		if (result instanceof NumericalList) {
			iterations = fromBuildingArray(context, (NumericalList)result, sb);
		} else {
			iterations = fromList(context, result, sb);
		}

		return iterations > 0 ? SKIP_BODY : EVAL_ELSE;
	}

	@Override
	public void start(Context context, Writer sb) {
		context.push();
	}

	@Override
	public void end(Context context, Writer sb) {
		context.pop();
	}

	public Exp getList() {
		return list;
	}

	public void setList(Exp list) {
		this.list = list;
	}

	public Exp getVar() {
		return var;
	}

	public void setVar(Exp var) {
		this.var = var;
	}

	public Exp getStep() {
		return step;
	}

	public void setStep(Exp step) {
		this.step = step;
	}

	public Exp getCounter() {
		return counter;
	}

	public void setCounter(Exp counter) {
		this.counter = counter;
	}
}
