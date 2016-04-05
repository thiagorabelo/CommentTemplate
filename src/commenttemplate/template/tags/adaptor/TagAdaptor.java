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
package commenttemplate.template.tags.adaptor;

import commenttemplate.context.Context;
import commenttemplate.template.tags.BasicTag;
import commenttemplate.template.tags.MountingHelper;
import commenttemplate.template.writer.Writer;
import commenttemplate.util.Tuple;
import commenttemplate.util.reflection.Instantiator;
import commenttemplate.util.reflection.properties.MethodWrapper;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 *
 * @author thiago
 */
public class TagAdaptor extends BasicTag {
	
	protected final Instantiator<? extends BasicTag> instanciator;

	public TagAdaptor(Class<? extends BasicTag> klass) {
		instanciator = new Instantiator(klass);
	}

	protected BasicTag initTag()
	throws IllegalAccessException, InstantiationException,
	IllegalArgumentException, InvocationTargetException {

		BasicTag tag = instanciator.newPopulatedInstance();

		tag.setNodeList(getNodeList());
		tag.setNodeListElse(getNodeListElse());
		tag.setTagName(getTagName());

		return tag;
	}

	@Override
	public void eval(Context context, Writer sb) {
	}

	@Override
	public void render(Context context, Writer sb) {
		try {
			BasicTag tag = initTag();

			tag.start(context, sb);
			tag.eval(context, sb);
			tag.end(context, sb);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public MountingHelper createMountingHelper() {
		try {
			return instanciator.newInstance().createMountingHelper(this);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void setSetterCache(List<Tuple<MethodWrapper, Object>> setterCache) {
		instanciator.setSetterCache(setterCache);
	}

	public void addSetter(Tuple<MethodWrapper, Object> tuple) {
		instanciator.addSetter(tuple);
	}

	public void addSetter(MethodWrapper wrapper, Object value) {
		instanciator.addSetter(wrapper, value);
	}

	public void addSetter(String prefix, String propertyName, boolean capitalize, Object value)
	throws NoSuchMethodException {
		instanciator.addSetter(prefix, propertyName, capitalize, value);
	}

	public boolean isInstanceOf(Object o) {
		return instanciator.isInstanceOf(o);
	}

	public boolean isAssignableOf(Class klass) {
		return instanciator.isAssignableFrom(klass);
	}

	@Override
	public void toString(StringBuilder sb) {
		try {
			BasicTag tag = initTag();
			tag.toString(sb);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
