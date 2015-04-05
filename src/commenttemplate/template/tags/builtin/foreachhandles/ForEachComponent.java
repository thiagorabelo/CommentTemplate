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

package commenttemplate.template.tags.builtin.foreachhandles;

import commenttemplate.expressions.exceptions.BadExpression;
import commenttemplate.expressions.exceptions.ExpectedExpression;
import commenttemplate.expressions.exceptions.ExpectedOperator;
import commenttemplate.expressions.exceptions.FunctionDoesNotExists;
import commenttemplate.expressions.exceptions.Unexpected;
import commenttemplate.expressions.parser.Parser;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.exceptions.CouldNotInstanciateTagException;
import commenttemplate.template.exceptions.CouldNotSetTagParameterException;
import commenttemplate.template.tags.TagComponent;
import commenttemplate.template.tags.TemplateTag;
import commenttemplate.template.tags.builtin.ForTemplateTag;
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
public class ForEachComponent extends TagComponent {

	//                                                              1           2
	private static final Pattern BUILTIN_PATTERN = Pattern.compile("(\\d+)\\.\\.(\\d+)");
	private static final int BUILTIN_GROUP_BEGIN = 1;
	private static final int BUILTIN_GROUP_LENGTH = 2;

	private static final String LIST = "list";
	
	
	public ForEachComponent() {
		super("for", ForTemplateTag.class, "!list", "var", "step", "counter");
	}

	public Exp parseExpression(ForTemplateTag tag, String paramName, String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected, FunctionDoesNotExists {
		if (!paramName.equals(LIST)) {
			return parseExpression(expression);
		}

		Matcher m = BUILTIN_PATTERN.matcher(expression);

		if (m.find()) {
			Exp begin = new Parser(m.group(BUILTIN_GROUP_BEGIN)).parse();
			Exp length = new Parser(m.group(BUILTIN_GROUP_LENGTH)).parse();
			
			return tag.new NumericalList(begin, length);
		}

		return parseExpression(expression);
	}
	
	public List<Tuple<String, Exp>> paramsList(ForTemplateTag tag, String parameters) throws BadExpression, ExpectedExpression, ExpectedOperator, FunctionDoesNotExists, Unexpected {
		LinkedList<Tuple<String, Exp>> params = new LinkedList<>();
		Matcher m = PARAMS_PATTERN.matcher(parameters);

		while (m.find()) {
			String paramName = m.group(PARAM_NAME_GROUP);
			String expression = m.group(PARAM_VALUE_GROUP);
			params.add(new Tuple<>(paramName, parseExpression(tag, paramName, expression)));
		}
		
		return params;
	}
	
	@Override
	public TemplateTag populateParameters(String parameters)
			throws CouldNotInstanciateTagException, CouldNotSetTagParameterException,
			BadExpression, ExpectedExpression, ExpectedOperator, FunctionDoesNotExists, Unexpected {

		ForTemplateTag tag = (ForTemplateTag)newInstance();
		List<Tuple<String, Exp>> params = paramsList(tag, parameters);
		new ParamsChecker().check(params);
		populateParameters(tag, params);

		return tag;
	}
}
