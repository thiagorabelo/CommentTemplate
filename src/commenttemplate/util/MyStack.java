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
		public Node<T> getAncestral();
	}
	
	private class StackNode implements Node<T> {
		private T item;
		private StackNode ancestral;

		public StackNode(T item) {
			this.item = item;
		}

		public StackNode(T item, StackNode ancestral) {
			this(item);
			this.ancestral = ancestral;
		}

		@Override
		public T getItem() {
			return item;
		}

		@Override
		public Node<T> getAncestral() {
			return ancestral;
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

			while (topSrc.ancestral != null) {
				t.ancestral = new StackNode(topSrc.ancestral.item);
				t = t.ancestral;
				topSrc = topSrc.ancestral;
			}

			t.ancestral = temp;

			size += source.size;
		}
	}

	// TODO: Lançar alguma exceção?
	public T pop() {
		if (top != null) {
			StackNode t = top;
			top = top.ancestral;
			size--;

			return t.item;
		}

		return null;
	}

	public T peek() {
		if (top != null) {
			return top.item;
		}

		return null;
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

		while (top.ancestral != null) {
			t = top.ancestral;
			top.ancestral = null;
			top = t;
		}

		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}
	
	public MyStack.Node<T> getTopNode() {
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
				current = t.ancestral;

				return t.item;
			}

			throw new NoSuchElementException("There is no more elements.");
		}
		
	}
	
	@Override
	public Iterator<T> iterator() {
		return new StackNodeIterator();
	}

//	public List<T> getList() {
//		return stack;
//	}
}
