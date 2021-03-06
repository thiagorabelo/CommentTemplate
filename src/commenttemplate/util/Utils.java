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
package commenttemplate.util;

import commenttemplate.util.reflection.properties.IterateByFields;
import commenttemplate.util.reflection.properties.IterateByMethods;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 *
 * @author thiago
 */
public class Utils {
	
	public static boolean empty(String str) {
		return str == null || str.trim().isEmpty();
	}
	
	public static String concat(Object ...args) {
		StringBuilder sb = new StringBuilder();

		for (Object o : args) {
			sb.append(o);
		}

		return sb.toString();
	}


	/*
		http://stackoverflow.com/questions/3209901/absolute-path-of-projects-folder-in-java#answer-3209914
	
		You should really be using getResource() or getResourceAsStream() using your class loader
		for this sort of thing. In particular, these methods use your ClassLoader to determine the
		search context for resources within your project.

		Specify something like getClass().getResource("lib/txtfile.txt") in order to pick up the
		text file.

		To clarify: instead of thinking about how to get the path of the resource you ought to be
		thinking about getting the resource -- in this case a file in a directory somewhere
		(possibly inside your JAR). It's not necessary to know some absolute path in this case, only
		some URL to get at the file, and the ClassLoader will return this URL for you. If you want
		to open a stream to the file you can do this directly without messing around with a URL
		using getResourceAsStream.

		The resources you're trying to access through the ClassLoader need to be on the Class-Path
		(configured in the Manifest of your JAR file). This is critical! The ClassLoader uses the
		Class-Path to find the resources, so if you don't provide enough context in the Class-Path
		it won't be able to find anything. If you add . the ClassLoader should resolve anything
		inside or outside of the JAR depending on how you refer to the resource, though you can
		certainly be more specific.

		Referring to the resource prefixed with a . will cause the ClassLoader to also look for
		files outside of the JAR, while not prefixing the resource path with a period will direct
		the ClassLoader to look only inside the JAR file.

		That means if you have some file inside the JAR in a directory lib with name foo.txt and you
		want to get the resource then you'd run getResource("lib/foo.txt");

		If the same resource were outside the JAR you'd run getResource("./lib/foo.txt");
	*/
	public static String getContent(String fullPath) throws FileNotFoundException, IOException {
		return getContent(fullPath, null);
	}
	
	/*
		https://code.google.com/p/juniversalchardet/
		http://www-archive.mozilla.org/projects/intl/UniversalCharsetDetection.html
		http://stackoverflow.com/questions/15154577/refactoring-auto-detect-files-encoding#answer-15188306
	*/
	public static String getContent(String filePath, String encoding) throws FileNotFoundException, IOException {
		InputStream fis = Utils.class.getClassLoader().getResourceAsStream(filePath);
		InputStreamReader isr = empty(encoding) ? new InputStreamReader(fis) : new InputStreamReader(fis, Charset.forName(encoding));
		StringBuilder sb = new StringBuilder();

		while (isr.ready()) {
			sb.append((char)isr.read());
		}

		return sb.toString();
	}
	
	public static Object getProperty(Object target, String propertyName) {
		return getProperty(target, propertyName, true);
	}
	
	private static boolean compareParams(Class<?> c1[], Class<?> c2[]) {
		if (c1 == null) {
			return c2 == null || c2.length == 0;
		}

		if (c2 == null) {
			return c1.length == 0;
		}

		if (c1.length != c2.length) {
			return false;
		}

		for (int i = 0; i < c1.length; i++) {
			if (!(c1[i] == c2[i] || c2[i].isAssignableFrom(c1[i]))) {
				return false;
			}
		}

		return true;
	}

	public static Method getMethod(Class klass, String name, Class ...paramsTypes) {
		IterateByMethods im = new IterateByMethods(klass);
		
		String internedName = name.intern();
		for (Method m : im) {
			if (m.getName() == internedName && compareParams(paramsTypes, m.getParameterTypes())) {
				return m;
			}
		}

		return null;
	}

	public static Method getMethod2(Class klass, String name, Class ...paramsTypes) throws NoSuchMethodException {
		IterateByMethods im = new IterateByMethods(klass);
		
		String internedName = name.intern();
		for (Method m : im) {
			if (m.getName() == internedName && compareParams(paramsTypes, m.getParameterTypes())) {
				return m;
			}
		}

		throw new NoSuchMethodException(concat(
			klass.getName(),
			".",
			name,
			"(",
			Join.with(",").these(new Iterator<String>() {
				int length = paramsTypes != null ? paramsTypes.length : 0;
				int index = 0;
				
				@Override
				public boolean hasNext() {
					return index < length;
				}

				@Override
				public String next() {
					return paramsTypes[index++].getName();
				}
			}),
			")"
		));
	}

	public static Method[] getMethodsByName(Class klass, String name) throws NoSuchMethodException {
		return getMethodsByName(klass, name, 0);
	}

	public static Method[] getMethodsByName(Class klass, String name, int max) throws NoSuchMethodException {
		IterateByMethods im = new IterateByMethods(klass);
		MyStack<Method> stack = new MyStack<Method>();
		int total = 0;

		String internedName = name.intern();
		for (Method m : im) {
			if (m.getName() == internedName) {
				stack.push(m);
				total += 1;
			}
			if (max > 0 && total >= max) {
				break;
			}
		}

		if (!stack.isEmpty()) {
			Method []ms = new Method[stack.size()];
			stack.toArray(ms);
			return ms;
		}

		throw new NoSuchMethodException(concat(
			klass.getName(),
			".",
			name,
			"(?)"
		));
	}

	public static Field getField(Class klass, String name) {
		IterateByFields it = new IterateByFields(klass);
		
		String internedName = name.intern();
		for (Field f : it) {
			if (f.getName() == internedName) {
				return f;
			}
		}
		
		return null;
	}

	public static Field getField2(Class klass, String name) throws NoSuchFieldException {
		IterateByFields it = new IterateByFields(klass);
		
		String internedName = name.intern();
		for (Field f : it) {
			if (f.getName() == internedName) {
				return f;
			}
		}
		
		throw new NoSuchFieldException(name);
	}
	
	public static Object getProperty(Object target, String propertyName, boolean force) {
		String []prefixes = {"get", "is", "has"};
		Class klass = target.getClass();
		String capitalized = capitalize(propertyName);
		Method method;

		// Tenta captura o valor da propriedade pelos métodos
		// get, is e has.
		for (int i = 0, len = prefixes.length; i < len; i++) {

			if ((method = getMethod(klass, prefixes[i] + capitalized)) != null) {
				if (force) {
					method.setAccessible(true);
				}

				try {
					return method.invoke(target);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		}
		
		// Caso não tenha conseguido através da invocação dos métodos com prefixies,
		// tenta encontrar algum metódo com o nome.
		if ((method = getMethod(klass, propertyName)) != null) {
			if (force) {
				method.setAccessible(true);
			}

			try {
				return method.invoke(target);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		// Caso não tenha conseguido através da invocação dos métodos,
		// tenta encontrar algum atributo com o nome.
		Field field;

		if ((field = getField(klass, propertyName)) != null) {
			if (force) {
				field.setAccessible(true);
			}

			try {
				return field.get(target);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		return null;
	}

	public static Object setProperty(Object instance, String propertyName, Object value)
			throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		String prefix = "set";
		String methodName = prefix + capitalize(propertyName);

		Method setter = getMethod2(instance.getClass(), methodName, value.getClass());
		return setter.invoke(instance, value);
	}
	
	public static String capitalize(String str) {
		if (str.length() > 1) {
			return Character.toUpperCase(str.charAt(0)) + str.substring(1);
		}

		return str.toUpperCase();
	}
	
	public static String uncapitalize(String str) {
		if (str.length() > 1) {
			return Character.toLowerCase(str.charAt(0)) + str.substring(1);
		}

		return str.toLowerCase();
	}
}