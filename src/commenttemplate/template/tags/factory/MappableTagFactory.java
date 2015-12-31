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

package commenttemplate.template.tags.factory;

import commenttemplate.expressions.exceptions.BadExpression;
import commenttemplate.expressions.exceptions.ExpectedExpression;
import commenttemplate.expressions.exceptions.ExpectedOperator;
import commenttemplate.expressions.exceptions.FunctionDoesNotExists;
import commenttemplate.expressions.exceptions.Unexpected;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.exceptions.CouldNotInstanciateTagException;
import commenttemplate.template.exceptions.CouldNotSetTagParameterException;
import commenttemplate.template.exceptions.InvalidParamsSintaxException;
import commenttemplate.template.tags.Tag;
import commenttemplate.util.Tuple;
import commenttemplate.util.Utils;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author thiago
 */
public class MappableTagFactory extends TagFactory {

	public MappableTagFactory(String name, Class<? extends Tag> tagClass, String... params) {
		super(name, tagClass, params);
	}
	
	protected class MappableParamsChecker extends ParamsChecker {
		
		@Override
		public void check(String name) {

			for (int i = 0; i <= iR; i++) {
				if (requiredParams[i].getB().equals(name)) { // Doing this for i=1, iR=4:
					requiredParams[i] = requiredParams[iR];  // [A, B, C, D] -> [A, D, C, null]
					requiredParams[iR--] = null;             //     ^ iR bacomes 3
					return;
				}
			}
		}
	}

	protected void populateMapParameters(Tag tag, List<Tuple<String, Exp>> parameters) {
		Map<String, Exp> mapParams = (Map<String, Exp>)Utils.getProperty(tag, "params");
		
		for (Tuple<String, Exp> t : parameters) {
			mapParams.put(t.getA(), t.getB());
		}
	}
	
	// Provavel Bug: Se for passado o mesmo parâmetro mais de uma vez.
	protected class FindInParams {
		protected String []paramNames = new String[params.length];
		protected int maxIndex = params.length;
		
		public FindInParams() {
			for (int i = 0, len = params.length; i < len; i++) {
				paramNames[i] = params[i].getB();
			}
		}
		
		public boolean isPresent(String name) {
			for (int i = 0; i < maxIndex; i++) {
				if (paramNames[i].equals(name)) {
					paramNames[i] = paramNames[maxIndex - 1];
					paramNames[(maxIndex-- - 1)] = null;
					return true;
				}
			}
			
			return false;
		}
	}

	@Override
	protected void populateParameters(Tag tag, List<Tuple<String, Exp>> parameters)
			throws CouldNotSetTagParameterException {

		Tuple<Boolean, String> []paramsCopy = new Tuple[params.length];
		System.arraycopy(params, 0, paramsCopy, 0, params.length);
		FindInParams f = new FindInParams();
		
		for (int i = 0, len = parameters.size(); i < len; i++) {
			Tuple<String, Exp> t = parameters.get(i);
			if (f.isPresent(t.getA())) {
				try {
					Utils.setProperty(tag, t.getA(), t.getB());
					parameters.remove(i);
					len = parameters.size();
				} catch (
					IllegalAccessException    | IllegalArgumentException |
					InvocationTargetException | NoSuchMethodException    |
					SecurityException ex
				) {
					throw new CouldNotSetTagParameterException(tagClass.getName(), t.getA(), ex);
				}
			}
		}
	}

	@Override
	public Tag populateParameters(String parameters)
			throws CouldNotInstanciateTagException, CouldNotSetTagParameterException,
			BadExpression, ExpectedExpression, ExpectedOperator, FunctionDoesNotExists, Unexpected, InvalidParamsSintaxException {

		Tag tag = newInstance();
		List<Tuple<String, Exp>> params = paramsList(parameters);
//		params = singleParameterVerifies(params, parameters);
		new MappableParamsChecker().check(params);
		populateParameters(tag, params);
		populateMapParameters(tag, params);

		return tag;
	}
}
