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

import commenttemplate.util.reflection.IterateByFields;
import commenttemplate.util.reflection.IterateByMethods;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

/**
 *
 * @author thiago
 */
public class Utils {
	
	public static boolean empty(String str) {
		return str == null || str.trim().isEmpty();
	}
	
	public static String concat(Object ...args) {
		int sum = 0;

		for (int i = 0, len = args.length; i < len; i++) {
			sum += 1;
		}

		StringBuilder sb = new StringBuilder(sum);

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
	
	private static boolean compareParams(Class<?> a1[], Class<?> a2[]) {
		if (a1 == null) {
			return a2 == null || a2.length == 0;
		}

		if (a2 == null) {
			return a1.length == 0;
		}

		if (a1.length != a2.length) {
			return false;
		}

		for (int i = 0; i < a1.length; i++) {
			if (!(a1[i] == a2[i] || a2[i].isAssignableFrom(a1[i]))) {
				return false;
			}
		}

		return true;
	}

	public static Method getMethod(Class klass, String name, Class ...paramsTypes) throws NoSuchMethodException {
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
			concat((Object[])paramsTypes),
			")"
		));
	}

	private static Field getField(Class klass, String name) throws NoSuchFieldException {
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

		// Tenta captura o valor da propriedade pelos métodos
		// get, is e has.
		for (int i = 0, len = prefixes.length; i < len;) {
			String prefix = prefixes[i];
			String methodName = prefix + capitalized;

			try {
				Method method = getMethod(klass, methodName);

				if (force) {
					method.setAccessible(true);
				}

				return method.invoke(target);
			} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				i += 1;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
		
		// Caso não tenha conseguido através da invocação dos métodos com prefixies,
		// tenta encontrar algum metódo com o nome.
		try {
			Method method = getMethod(klass, propertyName);

			if (force) {
				method.setAccessible(true);
			}

			return method.invoke(target);
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			// não faz nada.
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		// Caso não tenha conseguido através da invocação dos métodos,
		// tenta encontrar algum atributo com o nome.
		try {
			Field field = getField(klass, propertyName);

			if (force) {
				field.setAccessible(true);
			}

			return field.get(target);
		} catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
			return null;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Object setProperty(Object instance, String propertyName, Object value)
			throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		String prefix = "set";
		String methodName = prefix + capitalize(propertyName);

		Method setter = getMethod(instance.getClass(), methodName, value.getClass());
		return setter.invoke(instance, value);
	}
	
	public static String capitalize(String str) {
		if (str.length() > 1) {
			return concat(str.substring(0, 1).toUpperCase(), str.substring(1));
		}

		return str.toUpperCase();
	}
}
