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
public class JoinPath extends DefaultJoiner {
	
	public JoinPath(String joiner) {
		super(joiner);
		onNull = "";
	}
	
	// TODO: Ver os casos onde parts[i] pode ser iterado
	@Override
	public Joiner join(Iterator iterator) {
		Wrap<Boolean> skiped = new Wrap(false);
		Object part;
		boolean last = false;

		while (iterator.hasNext()) {
			part = iterator.next();
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
					last = true;
				} else {
					last = false;
				}

			} else {
				if (append.endsWith(joiner)) {
					append = append.substring(0, append.length() - joiner.length());
				}
			}

			sb.append(append);
		}
		
		if (last) {
			sb.append(joiner);
		}
		
		return this;
	}

	// TODO: Ver os casos onde parts[i] pode ser iterado
	@Override
	public Joiner join(Object[] parts) {
		Wrap<Boolean> skiped = new Wrap(false);
		boolean last = false;

		for (int i = 0, len = parts.length; i < len; i++) {
			Object part = parts[i];
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
					last = true;
				} else {
					last = false;
				}

			} else {
				if (append.endsWith(joiner)) {
					append = append.substring(0, append.length() - joiner.length());
				}
			}

			sb.append(append);
		}
		
		if (last) {
			sb.append(joiner);
		}
		
		return this;
	}
}
