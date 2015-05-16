package commenttemplate.template.tags.builtin;

import commenttemplate.template.tags.Tag;
import java.util.Iterator;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.context.Context;
import commenttemplate.template.TemplateBlock;
import commenttemplate.template.writer.Writer;
import commenttemplate.util.Utils;
import java.lang.reflect.Array;
import java.util.NoSuchElementException;

/**
 *
 * @author thiago
 */
public class ForTag extends Tag {
	
	private Exp list;
	private Exp var;
	private Exp step;
	private Exp counter;
	
	
	// TODO: length não esta sendo "length" e sim um "end" e aberto.
	// Fazer com que o length trabalhe com o tamanho da "lista" e não
	// indicar o fim da lista.
	public class NumericalList implements Iterable<Integer>, Exp {

		private final Exp start;
		private final Exp length;

		private int i_start;
		private int i_length;
		private int i_step;
		private int i_size;


		private class NumericalListIterator implements Iterator<Integer> {

			private int current;
			private boolean started;
			private final boolean upward; // ascendente

			public NumericalListIterator() {
				current = i_start;
				started = false;

				upward = i_length >= i_start;
			}

			@Override
			public boolean hasNext() {
				return (i_size > 0) && (!started || (upward ? current < (i_length - 1) : current > (i_length + 1)));
			}

			@Override
			public Integer next() {
				if (hasNext()) {
					if (started) {
						current += i_step;
					} else {
						started = true;
					}

					return current;
				}

				throw new NoSuchElementException(Utils.concat("Iterating over the limit of list [", i_start, "..", i_length, "]"));
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Not supported.");
			}
		}


		public NumericalList(Exp start, Exp length) {
			this.start = start;
			this.length = length;
		}

		@Override
		public Iterable<Integer> eval(Context context) {
			i_start = ((Number)start.eval(context)).intValue();
			i_length = ((Number)length.eval(context)).intValue();
			i_step = step == null ? 1 : ((Number)step.eval(context)).intValue();
			i_size = Math.abs(i_length - i_start);

			i_step = i_length >= i_start ? Math.abs(i_step) : -Math.abs(i_step);

			return this;
		}

		@Override
		public Iterator<Integer> iterator() {
			return new NumericalListIterator();
		}

		@Override
		public String toString() {
			return Utils.concat(start, "..", length);
		}

		@Override
		public void toString(StringBuilder sb) {
			sb.append(toString());
		}
	}
	
	
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

	protected int fromBuildingArray(Context context, NumericalList nlist, Writer sb) {
		int iterations = 0;

		Object v = null;
		if (var != null && (v = var.eval(context)) == null) {
			v = var.toString();
		}

		Object c = null;
		if (counter != null && (c = counter.eval(context)) == null) {
			c = counter.toString();
		}
		
		TemplateBlock []blockList = getBlockList();

		for (int el : nlist.eval(context)) {
			if (v != null) {
				context.put(v.toString(), el);
			}
			if (c != null) {
				context.put(c.toString(), iterations);
			}

			//evalBody(context, sb);
			if (blockList != null) {
				loopBlockList(blockList, context, sb);
			}

			iterations++;
		}

		return iterations;
	}

	protected int fromList(Context context, Object iterable, Writer sb) {
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
				
				TemplateBlock []blockList = getBlockList();

				while (it.hasNext()) {
					Object el = it.next();

					if (v != null) {
						context.put(v.toString(), el);
					}
					if (c != null) {
						context.put(c.toString(), i);
					}

					if (blockList != null) {
						loopBlockList(blockList, context, sb);
					}

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
			iterations = fromList(context, result.eval(context), sb);
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
