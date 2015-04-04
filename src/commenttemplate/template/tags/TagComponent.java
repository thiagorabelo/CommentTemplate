/*
 * Copyright (C) 2015 Thiago Rabelo.
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

package commenttemplate.template.tags;

import commenttemplate.expressions.exceptions.BadExpression;
import commenttemplate.expressions.exceptions.ExpectedExpression;
import commenttemplate.expressions.exceptions.ExpectedOperator;
import commenttemplate.expressions.exceptions.FunctionDoesNotExists;
import commenttemplate.expressions.exceptions.Unexpected;
import commenttemplate.expressions.parser.Parser;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.exceptions.CouldNotInstanciateTagException;
import commenttemplate.template.exceptions.CouldNotSetTagParameterException;
import commenttemplate.util.Tuple;
import commenttemplate.util.Utils;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author thiago
 */
public class TagComponent {
	//                                                               1          2
	protected static final Pattern PARAMS_PATTERN = Pattern.compile("(\\w+)\\=\"([^\"]*)\"");
	protected static final int PARAM_NAME_GROUP = 1;
	protected static final int PARAM_VALUE_GROUP = 2;
	

	private String name;
	private final Class<? extends TemplateTag> tagClass;
	
	public TagComponent(String name, Class<? extends TemplateTag> tagClass) {
		this.name = name;
		this.tagClass = tagClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<? extends TemplateTag> getTagClass() {
		return tagClass;
	}
	
	public Exp parseExpression(String expression)
			throws ExpectedOperator, ExpectedExpression, BadExpression,
			Unexpected, FunctionDoesNotExists {

		Parser p = new Parser(expression);
		return p.parse();
	}
	
	public List<Tuple<String, Exp>> paramsList(String parameters)
			throws BadExpression, ExpectedExpression, ExpectedOperator, 
			FunctionDoesNotExists, Unexpected {

		LinkedList<Tuple<String, Exp>> params = new LinkedList<>();
		Matcher m = PARAMS_PATTERN.matcher(parameters);

		while (m.find()) {
			params.add(new Tuple<>(
				m.group(PARAM_NAME_GROUP),
				parseExpression(m.group(PARAM_VALUE_GROUP))
			));
		}
		
		return params;
	}

	public TemplateTag populateParameters(String parameters)
			throws CouldNotInstanciateTagException, CouldNotSetTagParameterException,
			BadExpression, ExpectedExpression, ExpectedOperator, FunctionDoesNotExists, Unexpected {

		List<Tuple<String, Exp>> params = paramsList(parameters);

		TemplateTag tag;

		try {
			tag = tagClass.newInstance();
			tag.setTagName(name);
		} catch (IllegalAccessException | InstantiationException ex) {
			throw new CouldNotInstanciateTagException(tagClass.getName(), ex);
		}

		for (Tuple<String, Exp> t : params) {
			try {
				Utils.setProperty(tag, t.getA(), t.getB());
			} catch (
				IllegalAccessException    | IllegalArgumentException |
				InvocationTargetException | NoSuchMethodException    |
				SecurityException ex
			) {
				throw new CouldNotSetTagParameterException(tagClass.getName(), t.getA(), ex);
			}
		}

		return tag;
	}
}
