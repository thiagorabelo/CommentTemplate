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

import java.util.regex.Pattern;

/**
 *
 * @author thiago
 */
public class Properties extends java.util.Properties {
	private static final long serialVersionUID = -5804324205409904611L;
	
	private final Pattern spliter = Pattern.compile("\\s*,\\s*");
	
	public String []getPropertyAsArray(String key) {
		Object o = super.getProperty(key);
		if (o != null) {
			String []ret = spliter.split(o.toString());
			for (int i = 0, l = ret.length; i < l; i++) {
				ret[i] = ret[i].trim();
			}

			return ret;
		}

		return new String[0];
	}
}
