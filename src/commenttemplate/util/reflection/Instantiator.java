/*
 * Copyright (C) 2016 thiago.
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

import commenttemplate.util.Tuple;
import commenttemplate.util.Utils;
import commenttemplate.util.reflection.properties.MethodWrapper;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thiago
 */
public class Instantiator<C> {

	private final Class<C> klass;
	private List<Tuple<MethodWrapper, Object>> setterCache;

	public Instantiator(Class<C> klass) {
		this.klass = klass;
		setterCache = new ArrayList<Tuple<MethodWrapper, Object>>();
	}

	public C newPopulatedInstance()
	throws IllegalAccessException, InstantiationException,
	IllegalArgumentException, InvocationTargetException{
		C i = (C) klass.newInstance();

		for (Tuple<MethodWrapper, Object> t : setterCache) {
			t.getA().execute(i, t.getB());
		}

		return i;
	}

	public C newInstance()
	throws IllegalAccessException, InstantiationException,
	IllegalArgumentException, InvocationTargetException{
		C i = (C) klass.newInstance();

		return i;
	}

	public List<Tuple<MethodWrapper, Object>> getSetterCache() {
		return setterCache;
	}

	public void setSetterCache(List<Tuple<MethodWrapper, Object>> setterCache) {
		this.setterCache = setterCache;
	}

	public void addSetter(Tuple<MethodWrapper, Object> tuple) {
		setterCache.add(tuple);
	}

	public void addSetter(MethodWrapper wrapper, Object value) {
		setterCache.add(new Tuple<MethodWrapper, Object>(wrapper, value));
	}

	public void addSetter(String prefix, String propertyName, boolean capitalize, Object value) throws NoSuchMethodException {
		prefix = (prefix == null ? "" : prefix);
		String methodName = prefix + (capitalize ? Utils.capitalize(propertyName) : propertyName);

		MethodWrapper mw = new MethodWrapper(Utils.getMethod2(klass, methodName, value.getClass()));
		addSetter(mw, value);
	}
}
