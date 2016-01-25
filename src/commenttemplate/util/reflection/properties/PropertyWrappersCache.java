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
package commenttemplate.util.reflection.properties;

import commenttemplate.util.Join;
import commenttemplate.util.Utils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author thiago
 */
public class PropertyWrappersCache {

	private final Class cls;
	
	private final HashMap<String, PropertyWrapper> cache = new HashMap<String, PropertyWrapper>();

	public PropertyWrappersCache(Class cls) {
		this.cls = cls;
	}

	public PropertyWrapper findProperty(String name, Class ...params) {
		PropertyWrapper p;
		
		if ((p = cache.get(name)) != null) {
			return p;
		}
		
		String []prefixes = {"get", "is", "has"};
		String capitalized = Utils.capitalize(name);
		Method method;

		// Tenta capturar o valor da propriedade pelos métodos
		// get, is e has.
		for (int i = 0, len = prefixes.length; i < len; i++) {
			if ((method = Utils.getMethod(cls, prefixes[i] + capitalized, params)) != null) {
				method.setAccessible(true);
				cache.put(name, p = new MethodWrapper(method));
				return p;
			}
		}
		
		// Caso não tenha conseguido através da invocação dos métodos com prefixies,
		// tenta encontrar algum metódo com o nome.
		if ((method = Utils.getMethod(cls, name, params)) != null) {
			method.setAccessible(true);
			cache.put(name, p = new MethodWrapper(method));
			return p;
		}
		
		// Caso não tenha conseguido através da invocação dos métodos,
		// tenta encontrar algum atributo com o nome.
		Field field;
		if ((field = Utils.getField(cls, name)) != null) {
			field.setAccessible(true);
			p = new FieldWrapper(field);
			cache.put(name, p);
		}
		
		return null;
	}

	public String buildMethodName(String methodName, Class ...params) {
		StringBuilder sb = new StringBuilder();
		sb.append(methodName).append("(");

		if (params != null && params.length > 0) {
			sb.append(
				Join.with(",").these(
					new Iterable<String>() {
						@Override
						public Iterator<String> iterator() {
							return new Iterator<String>() {
								private int count = 0;

								@Override
								public boolean hasNext() {
									return count < params.length;
								}

								@Override
								public String next() {
									return params[count++].getName();
								}	
							};
						}
					}
				)
			);
		}
		
		return sb.append(")").toString();
	}
	
	public static void main(String[] args) {
		PropertyWrappersCache pc = new PropertyWrappersCache(PropertyWrappersCache.class);
		System.out.println(pc.buildMethodName("buildMethodName", String.class, Class.class));
	}
}
