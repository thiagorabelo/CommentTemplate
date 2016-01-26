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
import commenttemplate.template.tags.AbstractTag;
import commenttemplate.template.writer.Writer;
import commenttemplate.util.Tuple;
import commenttemplate.util.reflection.properties.Instantiator;
import commenttemplate.util.reflection.properties.MethodWrapper;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 *
 * @author thiago
 */
public class TagAdaptor extends AbstractTag {
	
	protected final Instantiator<? extends AbstractTag> instanciator;

	public TagAdaptor(Class<? extends AbstractTag> klass) {
		instanciator = new Instantiator(klass);
	}

	protected AbstractTag initTag()
	throws IllegalAccessException, InstantiationException,
	IllegalArgumentException, InvocationTargetException {

		AbstractTag tag = instanciator.newInstance();

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
			AbstractTag tag = initTag();

			tag.start(context, sb);
			tag.eval(context, sb);
			tag.end(context, sb);

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
}
