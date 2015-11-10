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
public class DefaultJoiner extends Joiner {
	
	public DefaultJoiner(String joiner) {
		super(joiner);
	}

	@Override
	public Joiner these(Iterable parts) {
		return these(parts.iterator());
	}
	
	private void theseAux(Object part, Wrap<Boolean> skiped) {
		String append = skip(skiped, part);
		if (appended > 1 && !skiped.getValue()) {
			sb.append(joiner);
		}
		sb.append(append);
	}

	@Override
	public Joiner these(Iterator iterator) {
		Wrap<Boolean> skiped = new Wrap(false);
		
		Object part;

		while (iterator.hasNext()) {
			part = iterator.next();
			
			if (part instanceof Iterable) {
				these((Iterable)part);
			} else if (part instanceof Iterator) {
				these((Iterator)part);
			} else if (isArray(part)) {
				these((Object[])part);
			} else {
				theseAux(part, skiped);
			}
		}
		
		return this;
	}
	
	@Override
	public Joiner these(Object[] parts) {
		return these(0, parts.length, parts);
	}

	@Override
	public Joiner these(int beginIndex, int length, Object[] parts) {
		Wrap<Boolean> skiped = new Wrap(false);

		for (int i = 0; i < length; i++) {
			Object part = parts[i];

			if (part instanceof Iterable) {
				these((Iterable)part);
			} else if (part instanceof Iterator) {
				these((Iterator)part);
			} else if (isArray(part)) {
				these((Object[])part);
			} else {
				theseAux(part, skiped);
			}
		}
		
		return this;
	}

	@Override
	public Joiner these(Object first, Object second, Object ...others) {
		if (first instanceof Iterable) {
			these((Iterable)first);
		} else if (first instanceof Iterator) {
			these((Iterator)first);
		} else if (isArray(first)) {
			these((Object[])first);
		} else {
			these(new Object[]{first});
		}

		if (second instanceof Iterable) {
			these((Iterable)second);
		} else if (second instanceof Iterator) {
			these((Iterator)second);
		} else if (isArray(second)) {
			these((Object[])second);
		} else {
			these(new Object[]{second});
		}

		these(others);

		return this;
	}
}
