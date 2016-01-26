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

import commenttemplate.util.reflection.IterateBySuperClasses;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thiago
 */
public class AnnotationList {
	private Class klass;

	public AnnotationList(Class klass) {
		this.klass = klass;
	}

	public List<Annotation> listInHierarchy(Class<? extends Annotation> c) {
		ArrayList<Annotation> l = new ArrayList<Annotation>();

		for (Class k : new IterateBySuperClasses(klass)) {
			if (k.isAnnotationPresent(c)) {
				l.add(k.getAnnotation(c));
			}
		}

		return l;
	}
}
