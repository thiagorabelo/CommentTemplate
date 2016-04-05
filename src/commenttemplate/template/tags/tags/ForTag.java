package commenttemplate.template.tags.tags;

import commenttemplate.context.Context;
import java.util.Iterator;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.tags.LoopTag;
import commenttemplate.template.tags.annotations.Instantiable;
import commenttemplate.template.tags.consequence.Consequence;
import commenttemplate.template.writer.Writer;
import java.lang.reflect.Array;
import java.util.NoSuchElementException;

/**
 *
 * @author thiago
 */
@Instantiable
public class ForTag extends LoopTag {
	
	private Exp list;
	private Exp var;
//	private Exp step;
	private Exp index;

	private Iterator<Object> iterator;
	private int iterationCounter;
	private String varName = null;
	private String counterName = null;
	
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

	@Override
	public void init(Context context, Writer sb) {
		iterator = getIterator(list.eval(context));
		iterationCounter = 0;

		if (var != null) {
			varName = var.eval(context).toString();
		}

		if (index != null) {
			counterName = index.eval(context).toString();
		}
	}

	@Override
	public Consequence doTest(Context context, Writer sb) {
		if (iterator.hasNext()) {
			Object el = iterator.next();

			if (varName != null) {
				context.put(varName, el);
			}
			if (counterName != null) {
				context.put(counterName, iterationCounter);
			}

			iterationCounter += 1;
			
			return EVAL_BODY;

		} else if (iterationCounter == 0) {
			return EVAL_ELSE;
		}

		return SKIP_BODY;
	}
	

	protected Iterator<Object> getIterator(Object iterable) {
		Iterator it = null;

		if (iterable != null) {
			if (iterable.getClass().isArray()) {
				it = new ForTag.ObjectArrayIterator(iterable);
			} else if (iterable instanceof Iterable) {
				it = ((Iterable)iterable).iterator();
			}

			return it;
		}
		
		return new Iterator<Object>() {
			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public Object next() {
				throw new NoSuchElementException("There is no elements to iterate over.");
			}
		};
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

	public Exp getIndex() {
		return index;
	}

	public void setIndex(Exp index) {
		this.index = index;
	}
}
