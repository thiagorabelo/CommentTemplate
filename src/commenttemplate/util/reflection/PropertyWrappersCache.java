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
package commenttemplate.util.reflection;

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

	// @TODO: Criar um PropertyWrapper para null, para impedir que toda
	// vez que seja null, refaça toda a busca através de reflexão.
	public PropertyWrapper findProperty(String name, Class ...params) {
		PropertyWrapper p;
		
		String capitalized = Utils.capitalize(name);
		String []prefixes = {"get", "is", "has"};

		for (String prefix : prefixes) {
			if ((p = cache.get(buildMethodName(prefix + capitalized, params))) != null) {
				return p;
			}
		}

		if ((p = cache.get(buildMethodName(name, params))) != null) {
			return p;
		}
		
		Method method;

		// Tenta capturar o valor da propriedade pelos métodos
		// get, is e has.
		for (String prefix : prefixes) {
			if ((method = Utils.getMethod(cls, prefix + capitalized, params)) != null) {
				method.setAccessible(true);
				cache.put(buildMethodName(prefix + capitalized, params), p = new MethodWrapper(method));
				return p;
			}
		}
		
		// Caso não tenha conseguido através da invocação dos métodos com prefixies,
		// tenta encontrar algum metódo com o nome.
		if ((method = Utils.getMethod(cls, name, params)) != null) {
			method.setAccessible(true);
			cache.put(buildMethodName(name, params), p = new MethodWrapper(method));
			return p;
		}
		
		// Caso não tenha conseguido através da invocação dos métodos,
		// tenta encontrar algum atributo com o nome.
		Field field;
		if ((field = Utils.getField(cls, name)) != null) {
			field.setAccessible(true);
			cache.put(name, p = new FieldWrapper(field));
			return p;
		}
		
		return null;
	}

	public String buildMethodName(String methodName, Class ...params) {
		StringBuilder sb = new StringBuilder();
		sb.append(methodName).append("(");

		if (params != null && params.length > 0) {
			
			// Não foi declarada direto na chamada da função
			// para melhorar a leitura.
			Iterable<String> it = new Iterable<String>() {
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
			};

			sb.append(Join.with(",").these(it));
		}
		
		return sb.append(")").toString();
	}
//	
//	public static void main(String[] args) {
//		PropertyWrappersCache pc = new PropertyWrappersCache(PropertyWrappersCache.class);
//		System.out.println(pc.buildMethodName("buildMethodName", String.class, Class.class));
//	}
}
