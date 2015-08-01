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

package commenttemplate.template.tags.builtin.customcomponent;

import commenttemplate.expressions.exceptions.BadExpression;
import commenttemplate.expressions.exceptions.ExpectedExpression;
import commenttemplate.expressions.exceptions.ExpectedOperator;
import commenttemplate.expressions.exceptions.FunctionDoesNotExists;
import commenttemplate.expressions.exceptions.Unexpected;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.exceptions.CouldNotInstanciateTagException;
import commenttemplate.template.exceptions.CouldNotSetTagParameterException;
import commenttemplate.template.tags.Tag;
import commenttemplate.template.tags.TagComponent;
import commenttemplate.template.tags.builtin.WithTag;
import commenttemplate.util.Tuple;
import commenttemplate.util.Utils;
import java.util.List;
import java.util.Map;

/**
 *
 * @author thiago
 */
public class WithComponent extends TagComponent {
	
	public WithComponent() {
		super("with", WithTag.class);
	}
	
	@Override
	protected void populateParameters(Tag tag, List<Tuple<String, Exp>> parameters) {
		Map<String, Exp> mapParams = (Map<String, Exp>)Utils.getProperty(tag, "params");
		
		for (Tuple<String, Exp> t : parameters) {
			mapParams.put(t.getA(), t.getB());
		}
	}
	
	@Override
	public Tag populateParameters(String parameters)
			throws CouldNotInstanciateTagException, CouldNotSetTagParameterException,
			BadExpression, ExpectedExpression, ExpectedOperator, FunctionDoesNotExists, Unexpected {

		Tag tag = newInstance();
		List<Tuple<String, Exp>> params = paramsList(parameters);
		populateParameters(tag, params);

		return tag;
	}

}
