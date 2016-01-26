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
import commenttemplate.expressions.parser.Parser;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.expressions.tree.Literal;
import commenttemplate.util.Utils;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author thiago
 */
public class Iterable extends Function {

	private class NumericalListIterator implements Iterator<Integer> {
		private final Exp start;
		private final Exp length;
		private final Exp step;

		private int i_start;
		private int i_length;
		private int i_step;
		private int i_size;

		private int current;
		private boolean started;
		private final boolean upward; // ascendente

		public NumericalListIterator(Context context, Exp start, Exp length, Exp step) {
			this.start = start;
			this.length = length;
			this.step = step;

			i_init(context);

			current = i_start;
			started = false;

			upward = i_length >= i_start;
		}

		private void i_init(Context context) {
			i_start = ((Number)start.eval(context)).intValue();
			i_length = ((Number)length.eval(context)).intValue();
			i_step = step == null ? 1 : ((Number)step.eval(context)).intValue();
			i_size = Math.abs(i_length - i_start);

			i_step = i_length >= i_start ? Math.abs(i_step) : -Math.abs(i_step);
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

	@Override
	public java.lang.Iterable<Integer> eval(Context context) {
		Exp start;
		Exp length;
		Exp step;

		List<Exp> args = getArgs();

		if (args.size() >= 2) {
			start = args.get(0);
			length = args.get(1);
			step =  args.size() > 2 ? args.get(2) : new Literal(1);

			return new java.lang.Iterable<Integer>() {
				@Override
				public Iterator<Integer> iterator() {
					return new NumericalListIterator(context, start, length, step);
				}
			};
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
