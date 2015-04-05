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
import commenttemplate.util.Join;
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
	protected static final char REQUIRED_CHAR = '!';
	
	
	private static String getParamName(String name) {
		return name.charAt(0) == REQUIRED_CHAR ? name.substring(1) : name;
	}
	
	private static boolean isRequired(String name) {
		return name.charAt(0) == REQUIRED_CHAR;
	}

	private static Tuple<Boolean, String> []setParams(String ...params) {
		Tuple<Boolean, String> []tuples = new Tuple[params != null ? params.length : 0];

		for (int i = 0; i < tuples.length; i++) {
			String p = params[i];
			tuples[i] = new Tuple(isRequired(p), getParamName(p));
		}

		return tuples;
	}

	protected String name;
	protected final Class<? extends TemplateTag> tagClass;
	protected final Tuple<Boolean, String> []params;
	
	protected class ParamsChecker {
		private Tuple<Boolean, String> []requiredParams;
		private Tuple<Boolean, String> []othersParans;
		private int iR; // index required
		private int iO; // index others
		
		public ParamsChecker() {
			int r = 0, o = 0;
			
			for (Tuple<Boolean, String> p : params) {
				if (p.getA()) {
					r++;
				} else {
					o++;
				}
			}
			
			requiredParams = new Tuple[r];
			othersParans = new Tuple[o];
			
			for (int i = 0, x = 0, y = 0; i < params.length; i++) {
				Tuple<Boolean, String> p = params[i];
				if (p.getA()) {
					requiredParams[x++] = p;
				} else {
					othersParans[y++] = p;
				}
			}
			
			iR = requiredParams.length - 1;
			iO = othersParans.length - 1;
		}
		
		public void check(String name) {

			for (int i = 0; i <= iR; i++) {
				if (requiredParams[i].getB().equals(name)) {
					requiredParams[i] = requiredParams[iR];
					requiredParams[iR--] = null;
					return;
				}
			}

			for (int i = 0; i <= iO; i++) {
				if (othersParans[i].getB().equals(name)) {
					othersParans[i] = othersParans[iO];
					othersParans[iO--] = null;
					return;
				}
			}
			
			// TODO: MELHORAR EXCEÇÃO
			throw new RuntimeException(Utils.concat("The Tag [", TagComponent.this.name, "] does not accept a param named ", name));
		}
		
		public void check(List<Tuple<String, Exp>> params) {
			for (Tuple<String, Exp> param : params) {
				check(param.getA());
			}
			
			List<String> requireds = new LinkedList<>();
			for (int i = 0; i <= iR; i++) {
				requireds.add(requiredParams[i].getB());
			}
			
			// TODO: MELHORAR EXCEÇÃO
			if (!requireds.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				sb.append("The parameters ")
				  .append(Join.with(", ").join(requireds).s())
				  .append(" are mandatory to the Tag [")
				  .append(TagComponent.this.name)
				  .append("]")
				;
				throw new RuntimeException(sb.toString());
			}
		}
	}
	
	public TagComponent(String name, Class<? extends TemplateTag> tagClass, String ...params) {
		this.name = name;
		this.tagClass = tagClass;
		this.params = setParams(params);
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
	
	public List<Tuple<String, Exp>> singleParameterVerifies(List<Tuple<String, Exp>> params, String parameters)
			throws BadExpression, ExpectedExpression, ExpectedOperator, 
			FunctionDoesNotExists, Unexpected {

		if (params.isEmpty() && this.params.length == 1 && !(parameters = parameters.trim()).isEmpty()) {
			params.add(new Tuple<>(
				this.params[0].getB(),
				parseExpression(parameters)
			));
		}

		return params;
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
	
	protected TemplateTag newInstance() throws CouldNotInstanciateTagException {
		try {
			TemplateTag tag = tagClass.newInstance();
			tag.setTagName(name);
			return tag;
		} catch (IllegalAccessException | InstantiationException ex) {
			throw new CouldNotInstanciateTagException(tagClass.getName(), ex);
		}
	}
	
	protected void populateParameters(TemplateTag tag, List<Tuple<String, Exp>> parameters) throws CouldNotSetTagParameterException{
		for (Tuple<String, Exp> t : parameters) {
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
	}

	public TemplateTag populateParameters(String parameters)
			throws CouldNotInstanciateTagException, CouldNotSetTagParameterException,
			BadExpression, ExpectedExpression, ExpectedOperator, FunctionDoesNotExists, Unexpected {

		TemplateTag tag = newInstance();
		List<Tuple<String, Exp>> params = paramsList(parameters);
		params = singleParameterVerifies(params, parameters);
		new ParamsChecker().check(params);
		populateParameters(tag, params);

		return tag;
	}
}
