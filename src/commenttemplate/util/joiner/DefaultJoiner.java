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
	public Joiner join(Iterable parts) {
		return join(parts.iterator());
	}

	// TODO: Ver os casos onde part pode ser iterado
	@Override
	public Joiner join(Iterator iterator) {
		Wrap<Boolean> skiped = new Wrap(false);
		
		Object part;

		while (iterator.hasNext()) {
			part = iterator.next();
			String append = skip(skiped, part);
			if (appended > 1 && !skiped.getValue()) {
				sb.append(joiner);
			}
			sb.append(append);
		}
		
		return this;
	}
	
	@Override
	public Joiner join(Object[] parts) {
		return join(0, parts.length, parts);
	}

	@Override
	public Joiner join(int beginIndex, int length, Object[] parts) {
		Wrap<Boolean> skiped = new Wrap(false);

		for (int i = 0; i < length; i++) {
			Object part = parts[i];
			
			if (part != null) {
				if (part instanceof Iterable) {
					join((Iterable)part);
				} else if (part instanceof Iterator) {
					join((Iterator)part);
				} else if (part.getClass().isArray()) {
					join((Object[])part);
				} else {
					String append = skip(skiped, part);
					if (appended > 1 && !skiped.getValue()) {
						sb.append(joiner);
					}
					sb.append(append);
				}
			} else {
				String append = skip(skiped, part);
				if (appended > 1 && !skiped.getValue()) {
					sb.append(joiner);
				}
				sb.append(append);
			}
		}
		
		return this;
	}

	@Override
	public Joiner join(Object first, Object second, Object ...others) {
		if (first != null) {
			if (first instanceof Iterable) {
				join((Iterable)first);
			} else if (first instanceof Iterator) {
				join((Iterator)first);
			} else if (first.getClass().isArray()) {
				join((Object[])first);
			} else {
				join(new Object[]{first});
			}
		} else {
			join(new Object[]{first});
		}

		if (second != null) {
			if (second instanceof Iterable) {
				join((Iterable)second);
			} else if (second instanceof Iterator) {
				join((Iterator)second);
			} else if (second.getClass().isArray()) {
				join((Object[])second);
			} else {
				join(new Object[]{second});
			}
		} else {
			join(new Object[]{second});
		}

		join(others);

		return this;
	}
}
