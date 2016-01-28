/*
 * Copyright (C) 2016 Thiago Rabelo.
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
package commenttemplate.util.reflection.annotations;

import commenttemplate.util.Utils;
import commenttemplate.util.reflection.IterateBySuperClasses;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author thiago
 */
public class Annotations {
	private Class klass;

	public Annotations(Class klass) {
		this.klass = klass;
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return (A) klass.getAnnotation(annotationClass);
	}

	public <A extends Annotation> List<A> listInHierarchy(Class<A> annotation) {
		ArrayList<A> l = new ArrayList<A>();

		for (Class k : new IterateBySuperClasses(klass)) {
			A an;
			if ((an = (A)k.getAnnotation(annotation)) != null) {
				l.add(an);
			}
		}

		return l;
	}

	public Object annotationParam(Class annotationClass, String param)
	throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Annotation a;
		if ((a = klass.getAnnotation(annotationClass)) != null) {
			return Utils.getMethod2(annotationClass, param).invoke(a);
		}

		return null;
	}

	public Set<Object> annotationParams(Class annotationClass, String ...params)
	throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Set<Object> l = new HashSet<Object>();

		Annotation a;
		if ((a = klass.getAnnotation(annotationClass)) != null) {
			for (String p : params) {
				l.add(Utils.getMethod2(annotationClass, p).invoke(a));
			}
		}

		return l;
	}

	public Set<Object> annotationParamsInHierarchy(Class annotationClass, String ...params)
	throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Set<Object> l = new HashSet<Object>();

		for (Class k : new IterateBySuperClasses(klass)) {
			Annotation a;
			if ((a = k.getAnnotation(annotationClass)) != null) {
				for (String p : params) {
					l.add(Utils.getMethod2(annotationClass, p).invoke(a));
				}
			}
		}

		return l;
	}
}
