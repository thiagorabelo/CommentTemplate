package commenttemplate.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thiago
 * @param <T>
 */
public class MyStack<T> {

	private final List<T> stack;

	public MyStack() {
		stack = new ArrayList<>();
	}

	public MyStack(MyStack other) {
		stack = new ArrayList<>(other.stack);
	}

	public MyStack(List<T> list) {
		stack = new ArrayList<>(list);
	}

	public T pop() {
		int top = stack.size() - 1;
		return (top >= 0) ? stack.remove(top) : null;
	}

	public T peek() {
		int top = stack.size() - 1;
		return (top >= 0) ? stack.get(top) : null;
	}

	public T replaceTop(T newTop) {
		int top = stack.size() - 1;

		if (top >= 0) {
			return stack.set(top, newTop);
		}

		stack.add(newTop);

		return null;
	}

	public MyStack<T> push(T item) {
		stack.add(item);
		return this;
	}

	public int size() {
		return stack.size();
	}

	public void clear() {
		stack.clear();
	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}

	public List<T> getList() {
		return stack;
	}
}
