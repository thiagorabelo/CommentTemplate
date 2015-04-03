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

package commenttemplate.loader;

import commenttemplate.util.Join;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author thiago
 */
public class Properties extends java.util.Properties {
	private static final long serialVersionUID = -5804324205409904611L;
	
	@Override
	public synchronized Object put(Object key, Object value) {
		Object o = super.get(key);

		if (o == null) {
			return super.put(key, value);
		} else {
			o = o.getClass().isArray() ? (Object[])o : new Object[]{o};
			Object []array = (Object[])o;
			array = new Object[array.length + 1];
			System.arraycopy((Object[])o, 0, array, 0, ((Object[])o).length);
			array[((Object[])o).length] = value;
			super.put(key, array);
			return o;
		}
	}
	
	public synchronized String []getAsArray(Object key) {
		Object o = super.get(key);

		if (o != null) {
			if (o.getClass().isArray()) {
				String []ret = new String[((Object[])o).length];
				for (int i = ret.length; i-- > 0;) {
					ret[i] = ((Object[])o)[i].toString();
				}
				return ret;
			} else {
				return new String[]{o.toString()};
			}
		} else {
			return new String[]{};
		}
	}
	
	@Override
	public String getProperty(String key) {
		Object oval = super.get(key);
		
		if (oval != null) {
			if (!oval.getClass().isArray()) {
				String sval = (oval instanceof String) ? (String)oval : null;
				return ((sval == null) && (defaults != null)) ? defaults.getProperty(key) : sval;
			} else {
				return Join.with(",").join((Object[])oval).toString();
			}
		} 
		
		return defaults != null ? defaults.getProperty(key) : null;
    }
	
	
	
	@Override
	public synchronized boolean contains(Object value) {
		Set<Map.Entry<Object,Object>> set = this.entrySet();
		
		for (Map.Entry<Object, Object> entry : set) {
			Object val = entry.getValue();
			if (!val.getClass().isArray()) {
				if (entry.getValue().equals(value)) {
					return true;
				}
			} else {
				return Arrays.asList((Object[])val).contains(value);
			}
		}
		
		return false;
	}
}
