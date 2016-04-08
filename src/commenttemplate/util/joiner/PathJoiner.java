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
public class PathJoiner extends DefaultJoiner {
	
	public PathJoiner(String joiner) {
		super(joiner);
		onNull = "";
	}
	
	private void theseAux(Object part, Wrap<Boolean> skiped, Wrap<Boolean> last) {
		String append = skip(skiped, part);
		if (appended > 1 && !skiped.getValue()) {
			sb.append(joiner);
		}

		if (appended > 1) {
			if (append.startsWith(joiner)) {
				append = append.substring(joiner.length());
			}
			if (append.endsWith(joiner)) {
				append = append.substring(0, append.length() - joiner.length());
				last.setValue(true);
			} else {
				last.setValue(false);
			}

		} else {
			if (append.endsWith(joiner)) {
				append = append.substring(0, append.length() - joiner.length());
			}
		}
		
		sb.append(append);
	}
	
	// @TODO: Ver os casos onde parts[i] pode ser iterado
	@Override
	public Joiner these(Iterator iterator) {
		Object part;
		Wrap<Boolean> skiped = new Wrap(false);
		Wrap<Boolean> last = new Wrap<>(false);

		while (iterator.hasNext()) {
			part = iterator.next();
			
			if (part instanceof Iterable) {
				these((Iterable)part);
			} else if (part instanceof Iterator) {
				these((Iterator)part);
			} else if (isArray(part)) {
				these((Object[])part);
			} else {
				theseAux(part, skiped, last);
			}
		}
		
		if (last.getValue()) {
			sb.append(joiner);
		}
		
		return this;
	}

	// @TODO: Ver os casos onde parts[i] pode ser iterado
	@Override
	public Joiner these(Object[] parts) {
		Object part;
		Wrap<Boolean> skiped = new Wrap(false);
		Wrap<Boolean> last = new Wrap<>(false);

		for (int i = 0, len = parts.length; i < len; i++) {
			part = parts[i];
			
			if (part instanceof Iterable) {
				these((Iterable)part);
			} else if (part instanceof Iterator) {
				these((Iterator)part);
			} else if (isArray(part)) {
				these((Object[])part);
			} else {
				theseAux(part, skiped, last);
			}
		}
		
		if (last.getValue()) {
			sb.append(joiner);
		}
		
		return this;
	}
}
