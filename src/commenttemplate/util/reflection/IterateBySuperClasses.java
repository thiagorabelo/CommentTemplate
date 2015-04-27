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
public class IterateBySuperClasses implements Iterable<Class> {
	private final Class klass;

	public IterateBySuperClasses(Class klass) {
		this.klass = klass;
	}

	private class ClassIterator implements Iterator<Class> {
		private Class current = klass;

		@Override
		public boolean hasNext() {
			return current.getSuperclass() != null;
		}

		@Override
		public Class next() {
			try {
				Class next = current;
				current = current.getSuperclass();
				return next;
			} catch (NullPointerException ex) {
				throw new NoSuchElementException("There is no more super classes.");
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Not supported.");
		}
	}

	@Override
	public Iterator<Class> iterator() {
		return new ClassIterator();
	}
}
