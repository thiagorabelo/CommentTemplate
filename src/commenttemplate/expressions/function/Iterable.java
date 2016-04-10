/*
 * Copyright (C) 2016 thiago.
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
package commenttemplate.expressions.function;

import commenttemplate.context.Context;
import commenttemplate.util.Utils;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author thiago
 */
public class Iterable extends ExecuteFunction {

	private static class NumericalListIterator implements Iterator<Integer> {
		private int start;
		private int length;
		private int step;
		private int size;

		private int current;
		private boolean started;
		private final boolean upward; // ascendente

		public NumericalListIterator(Number start, Number length, Number step) {
			this.start = start.intValue();
			this.length = length.intValue();
			this.step = step == null ? 1 : step.intValue();

			init2();

			upward = this.length >= this.start;
		}

		private void init2() {
			size = Math.abs(length - start);
			step = length >= start ? Math.abs(step) : -Math.abs(step);

			current = start;
			started = false;
		}

		@Override
		public boolean hasNext() {
			return (size > 0) && (!started || (upward ? current + step < length : current + step > length));
		}

		@Override
		public Integer next() {
			if (hasNext()) {
				if (started) {
					current += step;
				} else {
					started = true;
				}

				return current;
			}

			throw new NoSuchElementException(Utils.concat("Iterating over the limit of list [", start, "..", length, "]"));
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Not supported.");
		}
	}

	private static class InnerIterable implements java.lang.Iterable<Integer> {
		Number start;
		Number length;
		Number step;

		public InnerIterable(Number start, Number length, Number step) {
			this.start = start;
			this.length = length;
			this.step = step;
		}

		@Override
		public Iterator<Integer> iterator() {
			return new NumericalListIterator(start, length, step);
		}
	}

	public java.lang.Iterable<Integer> execute(Context context, Number start, Number length, Number step) {
		if (length != null) {
			return new InnerIterable(start, length, step);
		}

		if (start != null) {
			return new InnerIterable(0, start, step);
		}

		return new java.lang.Iterable<Integer>() {
			@Override
			public Iterator<Integer> iterator() {
				return new Iterator<Integer>() {
					@Override
					public boolean hasNext() {
						return false;
					}

					@Override
					public Integer next() {
						throw new NoSuchElementException("It is empty");
					}
				};
			}
		};
	}
}
