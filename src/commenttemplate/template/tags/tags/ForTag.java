package commenttemplate.template.tags.tags;

import commenttemplate.template.tags.AbstractTag;
import java.util.Iterator;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.context.Context;
import commenttemplate.template.nodes.Node;
import commenttemplate.template.writer.Writer;
import java.lang.reflect.Array;

/**
 *
 * @author thiago
 */
public class ForTag extends AbstractTag {
	
	private Exp list;
	private Exp var;
//	private Exp step;
	private Exp counter;
	
	private class ObjectArrayIterator implements Iterator<Object> {
	
		private final Object array;
		private final int size;
		private int index;
		private boolean started;

		public ObjectArrayIterator(Object array) {
			if (array.getClass().isArray()) {
				this.array = array;
				size = Array.getLength(array);
			} else {
				this.array = null;
				size = 0;
			}

			started = false;
			index = 0;
		}

		@Override
		public boolean hasNext() {
			return (size > 0) && (!started || index < (size - 1));
		}

		@Override
		public Object next() {
			if (hasNext()) {
				if (started) {
					index += 1;
				} else {
					started = true;
				}

				return Array.get(array, index);
			}

			throw new RuntimeException("Iterating over the limit of array");
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Not supported.");
		}
	}
	
	
	public ForTag() {
	}

	protected int iterable(Context context, Object iterable, Writer sb) {
		if (iterable != null) {

			Iterator it = null;

			if (iterable.getClass().isArray()) {
				it = new ObjectArrayIterator(iterable);
			} else if (iterable instanceof Iterable) {
				it = ((Iterable)iterable).iterator();
			}

			if (it != null) {
				int i = 0;

				Object v = null;
				if (var != null && (v = var.eval(context)) == null) {
					v = var.toString();
				}

				Object c = null;
				if (counter != null && (c = counter.eval(context)) == null) {
					c = counter.toString();
				}

				Node []nodeList = getNodeList();

				while (it.hasNext()) {
					Object el = it.next();

					if (v != null) {
						context.put(v.toString(), el);
					}
					if (c != null) {
						context.put(c.toString(), i);
					}

					if (nodeList != null) {
						loopNodeList(nodeList, context, sb);
					}

					i += 1;
				}

				return i;
			}
		}

		return 0;
	}

	@Override
	public void eval(Context context, Writer sb) {
		Exp result = list;
		int iterations;

		iterations = iterable(context, result.eval(context), sb);

		if (!(iterations > 0)) {
			EVAL_ELSE.doEval(this, context, sb);
		}
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

//	public Exp getStep() {
//		return step;
//	}
//
//	public void setStep(Exp step) {
//		this.step = step;
//	}

	public Exp getCounter() {
		return counter;
	}

	public void setCounter(Exp counter) {
		this.counter = counter;
	}
}
