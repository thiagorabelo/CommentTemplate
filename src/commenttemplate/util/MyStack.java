package commenttemplate.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thiago
 * @param <T>
 */
public class MyStack<T> {
	
	private class StackNode {
		private T item;
		private StackNode ancestral;

		public StackNode(T item) {
			this.item = item;
		}

		public StackNode(T item, StackNode ancestral) {
			this(item);
			this.ancestral = ancestral;
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
			StackNode top = source.top;
			StackNode t = new StackNode(top.item);
			size++;

			while (top.ancestral != null) {
				t.ancestral = new StackNode(top.ancestral.item);
				t = t.ancestral;
				top = top.ancestral;
				size++;
			}
		}
	}

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

	public int size() {
		return size;
	}

	public void clear() {
//		stack.clear();
	}

	public boolean isEmpty() {
		return size > 0;
	}

//	public List<T> getList() {
//		return stack;
//	}
}
