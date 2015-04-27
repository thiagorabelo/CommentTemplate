/*
 * Copyright (C) 2015 Thiago Rabelo.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package commenttemplate.util.reflection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author thiago
 */
public abstract class IterateByProperties<T> implements Iterable<T> {
	private final Class klass;

	public IterateByProperties(Class klass) {
		this.klass = klass;
	}

	protected abstract T [] getDeclaredProp(Class klass);

	private class PropIterator implements Iterator<T> {
		private Class current = klass;
		private Class nextClass = klass.getSuperclass();
		private T [] props = getDeclaredProp(current);
		private T [] nextProps;
		private int i = 0;

		private boolean checkSuper() {
			// 1 tem mais classe?
				// 2 - tem mais métodos?
					// 3 - retorne verdadeiro
				// volte para 1
			// 4 - retorne falso

			while (nextClass != null) {
				if ((nextProps != null || (nextProps = getDeclaredProp(nextClass)) != null) && nextProps.length > 0) {
					return true;
				}
				nextClass = nextClass.getSuperclass();
				nextProps = null;
			}
			return false;
		}

		@Override
		public boolean hasNext() {
			return i < props.length || checkSuper();
		}

		private boolean needChange() {
			return i >= props.length;
		}

		@Override
		public T next() {
			if (needChange()) {
				if (checkSuper()) {
					i = 0;
					current = nextClass;
					props = getDeclaredProp(current);
					nextClass = current.getSuperclass();
					nextProps = null;
				} else {
					throw new NoSuchElementException("There is no more methods.");
				}
			}

			return props[i++];
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new PropIterator();
	}	
}
