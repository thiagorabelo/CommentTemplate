/*
 * Copyright (C) 2015 thiago.
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
package commenttemplate.util.joiner;

import commenttemplate.util.Wrap;
import java.util.Iterator;

/**
 *
 * @author thiago
 */
// @TODO: Adicionar a opção "flatten"
public abstract class Joiner {
	
	protected String joiner;
	protected String onNull = "null";
	protected boolean skipNulls = false;
	protected StringBuilder sb = new StringBuilder();
	protected int appended = 0;
	
	public Joiner(String joiner) {
		this.joiner = joiner;
	}

	public abstract Joiner these(Iterable<?> parts);

	public abstract Joiner these(Iterator<?> parts);

	public abstract Joiner these(Object[] parts);

	public abstract Joiner these(int beginIndex, int length, Object[] parts);

	public abstract Joiner these(Object first, Object second, Object ...others);

	public Joiner skipNulls() {
		skipNulls = true;
		return this;
	}
	
	public Joiner useNulls() {
		skipNulls = false;
		onNull = "null";
		return this;
	}

	public Joiner useNulls(String onNull) {
		useNulls();
		return insteadNull(onNull);
	}

	public Joiner insteadNull(String onNull) {
		if (onNull != null) {
			this.onNull = onNull;
			return this;
		}

		throw new IllegalArgumentException("Can't be null");
	}
	
	protected boolean isArray(Object obj) {
		return obj != null && obj.getClass().isArray();
	}
	
	protected String skip(Wrap<Boolean> skiped, Object o) {
		if (o == null) {
			if (skipNulls) {
				skiped.setValue(true);
				return "";
			} else {
				appended++;
				skiped.setValue(false);
				return onNull;
			}
		}

		appended++;
		skiped.setValue(false);

		return o.toString();
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}

	public String s() {
		return sb.toString();
	}
}
