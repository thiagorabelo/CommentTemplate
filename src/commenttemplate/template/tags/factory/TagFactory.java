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

package commenttemplate.template.tags.factory;

import commenttemplate.expressions.exceptions.BadExpression;
import commenttemplate.expressions.exceptions.ExpectedExpression;
import commenttemplate.expressions.exceptions.ExpectedOperator;
import commenttemplate.expressions.exceptions.FunctionDoesNotExists;
import commenttemplate.expressions.exceptions.Unexpected;
import commenttemplate.expressions.parser.Parser;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.exceptions.CouldNotInstanciateTagException;
import commenttemplate.template.exceptions.CouldNotSetTagParameterException;
import commenttemplate.template.exceptions.InvalidParamsSintaxException;
import commenttemplate.template.tagparams.TagParamsTokenizer;
import commenttemplate.template.tags.AbstractTag;
import commenttemplate.template.tags.adaptor.TagAdaptor;
import commenttemplate.template.tags.annotations.Instantiable;
import commenttemplate.util.Join;
import commenttemplate.util.Tuple;
import commenttemplate.util.Utils;
import commenttemplate.util.reflection.properties.MethodWrapper;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author thiago
 */
public class TagFactory {

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
//	protected String elseName = "else";
	protected final Class<? extends AbstractTag> tagClass;
	protected final Tuple<Boolean, String> []params;
	
	protected class ParamsChecker {
		protected final Tuple<Boolean, String> []requiredParams;
		protected final Tuple<Boolean, String> []othersParans;
		protected int iR; // index required
		protected int iO; // index others
		
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
				if (requiredParams[i].getB().equals(name)) { // Doing this for i=1, iR=4:
					requiredParams[i] = requiredParams[iR];  // [A, B, C, D] -> [A, D, C, null]
					requiredParams[iR--] = null;             //     ^ iR bacomes 3
					return;
				}
			}

			for (int i = 0; i <= iO; i++) {
				if (othersParans[i].getB().equals(name)) { // Doing the same logic above
					othersParans[i] = othersParans[iO];
					othersParans[iO--] = null;
					return;
				}
			}
			
			// TODO: DO IT BETTER
			throw new RuntimeException(Utils.concat("The Tag [", 
				TagFactory.this.name,
				"] does not accept a param named ",
				name
			));
		}
		
		public void check(List<Tuple<String, Exp>> params) {
			for (Tuple<String, Exp> param : params) {
				check(param.getA());
			}
			
			List<String> requireds = new LinkedList<>();
			for (int i = 0; i <= iR; i++) {
				requireds.add(requiredParams[i].getB());
			}
			
			// TODO: DO IT BETTER
			if (!requireds.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				sb.append("The parameters ")
				  .append(Join.with(", ").these(requireds).s())
				  .append(" are mandatory to the Tag [")
				  .append(TagFactory.this.name)
				  .append("]")
				;
				throw new RuntimeException(sb.toString());
			}
		}
	}
	
	public TagFactory(String name, Class<? extends AbstractTag> tagClass, String ...params) {
		this.name = name;
		this.tagClass = tagClass;
		this.params = setParams(params);
	}
	
//	public TagComponent(String name, String elseName, Class<? extends TemplateTag> tagClass, String ...params) {
//		this.name = name;
//		this.tagClass = tagClass;
//		this.params = setParams(params);
//		this.elseName = elseName;
//	}

	public String getName() {
		return name;
	}

//	public String getElseName() {
//		return elseName;
//	}

	public Class<? extends AbstractTag> getTagClass() {
		return tagClass;
	}
	
	public Exp parseExpression(String expression)
			throws ExpectedOperator, ExpectedExpression, BadExpression,
			Unexpected, FunctionDoesNotExists {

		Parser p = new Parser(expression);
		return p.parse();
	}
	
	public List<Tuple<String, Exp>> singleParameterVerifier(List<Tuple<String, Exp>> params, String parameters)
			throws BadExpression, ExpectedExpression, ExpectedOperator, 
			FunctionDoesNotExists, Unexpected {

		if (this.params.length == 1 && !(parameters = parameters.trim()).isEmpty() && allValsNullVerifier(params)) {
			params.clear();
			params.add(new Tuple<>(
				this.params[0].getB(),
				parseExpression(parameters)
			));
		}

		return params;
	}
	
	private boolean allValsNullVerifier(List<Tuple<String, Exp>> params) {
		boolean test = true;
		for (Tuple<String, Exp> p : params) {
			test = test && p.getB() == null;
		}
		return test;
	}
	
	public List<Tuple<String, Exp>> paramsList(String parameters)
			throws BadExpression, ExpectedExpression, ExpectedOperator, 
			FunctionDoesNotExists, Unexpected, InvalidParamsSintaxException {

		LinkedList<Tuple<String, Exp>> params = new LinkedList<>();

		for (String[] tokens : new TagParamsTokenizer(parameters)) {
			params.add(new Tuple<>(
				tokens[0],
				tokens[1] != null ? parseExpression(tokens[1]) : null
			));
		}

		return params;
	}
	
	protected AbstractTag newInstance() throws CouldNotInstanciateTagException {
		try {
			AbstractTag tag = tagClass.newInstance();
			tag.setTagName(name);
			return tag;
		} catch (IllegalAccessException | InstantiationException ex) {
			throw new CouldNotInstanciateTagException(tagClass.getName(), ex);
		}
	}
	
	protected void populateParameters(AbstractTag tag, List<Tuple<String, Exp>> parameters)
			throws CouldNotSetTagParameterException {
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
	
	protected void populateParameters(TagAdaptor adaptor, List<Tuple<String, Exp>> parameters)
	throws CouldNotSetTagParameterException {
		for (Tuple<String, Exp> t : parameters) {
			try {
				MethodWrapper mw = new MethodWrapper(Utils.getMethod2(tagClass, "set" + Utils.capitalize(t.getA()), t.getB().getClass()));
				adaptor.addSetter(mw, t.getB());
			} catch (NoSuchMethodException ex) {
				throw new CouldNotSetTagParameterException(tagClass.getName(), t.getA(), ex);
			}
		}
	}

	public AbstractTag populateParameters(String parameters)
			throws CouldNotInstanciateTagException, CouldNotSetTagParameterException,
			BadExpression, ExpectedExpression, ExpectedOperator, FunctionDoesNotExists, Unexpected, InvalidParamsSintaxException {

		boolean instantiable = tagClass.isAnnotationPresent(Instantiable.class);

		List<Tuple<String, Exp>> params = paramsList(parameters);
		params = singleParameterVerifier(params, parameters);
		new ParamsChecker().check(params);

		if (!instantiable) {
			AbstractTag tag = newInstance();
			populateParameters(tag, params);

			return tag;
		}

		TagAdaptor adaptor = new TagAdaptor(tagClass);
		adaptor.setTagName(name);
		populateParameters(adaptor, params);

		return adaptor;
	}

	public String[] getParams() {
		String []ret = new String[params.length];

		for (int i = 0, len = params.length; i < len; i++) {
			ret[i] = params[i].getB();
		}

		return ret;
	}
}
