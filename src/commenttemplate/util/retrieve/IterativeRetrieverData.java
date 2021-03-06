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
package commenttemplate.util.retrieve;

import commenttemplate.util.Utils;

/**
 *
 * @author thiago
 * @param <T>
 */
public abstract class IterativeRetrieverData<T> implements RetrieveData<T> {
	
	protected abstract Object getInitialData(T target, String key0);
	
	protected abstract int beginLoopIndex();
	
	protected Object getProperty(Object obj, String key) {
		return Utils.getProperty(obj, key);
	}

	protected Object loopProperties(int begin, int length, Object obj, String []keys) {
		for (int i = begin; i < length; i++) {
			if ((obj = getProperty(obj, keys[i])) == null) {
				break;
			}
		}

		return obj;
	}
	
	@Override
	public Object getValue(T target, String ...keys) {
		int len = keys.length;

		if (len > 0) {
			Object obj;

			if ((obj = getInitialData(target, keys[0])) != null) {
				obj = loopProperties(beginLoopIndex(), len, obj, keys);
			}

			return obj;
		} else {
			return null;
		}
	}	
}
