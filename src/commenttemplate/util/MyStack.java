package commenttemplate.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author thiago
 * @param <T>
 */
public class MyStack<T> implements Iterable<T> {
	
	public static interface Node<T> {
		public T getItem();
		public Node<T> previus();
	}
	
	private class StackNode implements Node<T> {
		private T item;
		private StackNode previus;

		public StackNode(T item) {
			this.item = item;
		}

		public StackNode(T item, StackNode previus) {
			this(item);
			this.previus = previus;
		}

		@Override
		public T getItem() {
			return item;
		}

		@Override
		public Node<T> previus() {
			return previus;
		}
	}
	
	private StackNode top = null;
	private int size = 0;

	public MyStack() {
	}

	public MyStack(MyStack other) {
		copyStack(other);
	}

//	public MyStack(List<T> list) {
//		stack = new ArrayList<>(list);
//	}
	
	private void copyStack(MyStack<T> source) {
		if (source != null && source.top != null) {
			StackNode topSrc = source.top;
			StackNode t;
			StackNode temp = top;

			top = t = new StackNode(topSrc.item);

			while (topSrc.previus != null) {
				t.previus = new StackNode(topSrc.previus.item);
				t = t.previus;
				topSrc = topSrc.previus;
			}

			t.previus = temp;

			size += source.size;
		}
	}

	// @TODO: Lançar alguma exceção?
	public T pop() {
		if (top != null) {
			StackNode t = top;
			top = top.previus;
			size--;

			return t.item;
		}

		return null;
	}

	public T peek() {
		return top != null ? top.item : null;
	}

	public T replaceTop(T newTop) {
		if (top != null) {
			T old = top.item;
			top.item = newTop;
			return old;
		}

		return null;
	}

	public MyStack<T> push(T item) {
		top = new StackNode(item, top);
		size++;
		return this;
	}

	public void pushAll(Collection<T> c) {
		for (T t : c) {
			push(t);
		}
	}

	public void pushAll(MyStack<T> s) {
		copyStack(s);
	}

	public int size() {
		return size;
	}

	public void clear() {
		StackNode t;

		while (top.previus != null) {
			t = top.previus;
			top.previus = null;
			top = t;
		}

		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}
	
	public MyStack.Node<T> topNode() {
		return top;
	}
	
	private class StackNodeIterator implements Iterator<T> {
		
		private StackNode current = top;

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			if (current != null) {
				StackNode t = current;
				current = t.previus;

				return t.item;
			}

			throw new NoSuchElementException("There is no more elements.");
		}
		
	}
	
	@Override
	public Iterator<T> iterator() {
		return new StackNodeIterator();
	}

	public T[] toArray(T[] array) {
		int maxUntil = array.length;
		
		if (array.length > size) {
			maxUntil = size;
		}

		for (StackNode current = this.top; (current != null && maxUntil > 0); current = current.previus) {
			array[--maxUntil] = current.item;
		}

		return array;
	}

	public Object[] toArray() {
		return toArray((T[]) new Object[size]);
	}

//	public List<T> getList() {
//		return stack;
//	}
}
