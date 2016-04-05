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

import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.tags.BasicTag;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 *
 * @author thiago
 */
public class MappedTagAdaptor extends TagAdaptor {
	
	protected HashMap<String, Exp> extraParams;

	public MappedTagAdaptor(Class<? extends BasicTag> klass) {
		super(klass);
		extraParams = new HashMap<String, Exp>();
	}

	@Override
	protected BasicTag initTag()
	throws IllegalAccessException, InstantiationException,
	IllegalArgumentException, InvocationTargetException {

		BasicTag tag = instanciator.newPopulatedInstance();

		tag.setNodeList(getNodeList());
		tag.setNodeListElse(getNodeListElse());
		tag.setTagName(getTagName());

		return tag;
	}

	public void addExtraParams(String property, Exp value) {
		extraParams.put(property, value);
	}

	public void setExtraParams(HashMap<String, Exp> extraParams) {
		this.extraParams = extraParams;
	}
}
