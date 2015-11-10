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
package commenttemplate.util.retrieve;

import commenttemplate.util.reflection.PropertyCache;
import commenttemplate.util.reflection.PropertyWrapper;
import java.util.HashMap;

/**
 *
 * @author thiago
 */
public class CacheableRetrieverDataMap extends IterativeRetrieverDataMap {
	
	private HashMap<Class, PropertyCache> cache = new HashMap<Class, PropertyCache>();

	@Override
	protected Object getProperty(Object obj, String key) {
		PropertyCache pc = cache.get(obj.getClass());
		if (pc == null) {
			pc = new PropertyCache(obj.getClass());
			cache.put(obj.getClass(), pc);
		}

		PropertyWrapper pw = pc.findProperty(key);

		try {
			return pw != null ? pw.execute(obj) : null;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}