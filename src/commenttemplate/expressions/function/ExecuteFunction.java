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
package commenttemplate.expressions.function;

import commenttemplate.context.Context;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.util.Utils;
import commenttemplate.util.reflection.properties.MethodWrapper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author thiago
 */
public class ExecuteFunction extends Function {
	private static final String METHOD_NAME = "execute";

	private Exp [] formalParameters = null;
	private MethodWrapper mwrapper;

	private class MethodResolution {
		
		public void normalizeParams(ArrayList<Exp> args, Method m) {
			Class []params = m.getParameterTypes();

			if (params.length == 0 || !params[0].isAssignableFrom(Context.class)) {
				// @TODO: Esta exceção deve ser aqui ou na hora de fazer o parser?
				throw new RuntimeException(Utils.concat(
					"The fisrt parameter of \"execute\" method in ",
					this.getClass().getName(),
					"[,", getName(), "]",
					" must be the Context"
				));
			}

			if (args.size() < params.length) {
				for (int i = args.size(), max = params.length; i < max; i++) {
					args.add(null);
				}
			}

			if (params.length < args.size()) {
				for (int i = args.size() - 1, min = params.length; i >= min; i--) {
					args.remove(i);
				}
			}

			// Disregard the first argument that is a blank space
			// reserved to Context.
			if (args.size() > 1) {
				args.remove(0);

				formalParameters = new Exp[args.size()];
				args.toArray(formalParameters);
			}
		}
		
		public void resolve() throws NoSuchMethodException {
			ArrayList<Exp> args = new ArrayList<Exp>();

			// Give space to put the Context.
			args.add(null);

			args.addAll(Arrays.asList(arguments()));

			// @TODO: Isto vai lançar uma exceção caso não exista um método
			// chamado "execute".
			Method execute = Utils.getMethodsByName(ExecuteFunction.this.getClass(), METHOD_NAME, 1)[0];

			if (execute.getReturnType() == Void.class) {
				// @TODO: Melhorar esta exceção.
				throw new RuntimeException(Utils.concat(
					"The method \"execute\" from ",
					this.getClass().getName(),
					"[", getName(), "]",
					"must return a type."
				));
			}

			normalizeParams(args, execute);

			mwrapper = new MethodWrapper(execute);
		}
	}

	public void prepare() {
		MethodResolution resolutor = new MethodResolution();
		try {
			resolutor.resolve();
		} catch (NoSuchMethodException ex) {
			// @TODO: Melhorar esta exceção.
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Convert a list of Exp in a array of function arguments.
	 * 
	 * @param args The new list of arguments.
	 */
	@Override
	public void setArgs(List<Exp> args) {
		super.setArgs(args);
		prepare();
	}

	/**
	 * Set the list of arguments.
	 * 
	 * @param args The new list of arguments.
	 */
	@Override
	public void setArgs(Exp []args) {
		super.setArgs(args);
		prepare();
	}

	public Object []arguments(Context context, Object ...others) {
		Exp []exps = arguments();
		Object []arguments = new Object[exps.length];
		int max = 0;
		
		if (others != null && others.length > 0) {
			max = others.length;
			System.arraycopy(others, 0, arguments, 0, max);
		}

		if (arguments.length > max) {
			for (int i = max, len = arguments.length; i < len; i++) {
				arguments[i] = exps[i].eval(context);
			}
		}

		return arguments;
	}

	@Override
	public Object eval(Context context) {
		try {
			Object []args;

			if (formalParameters != null && formalParameters.length > 0) {
				args = new Object[formalParameters.length + 1];
				args[0] = context;

				for (int i = 1, max = args.length; i < max; i++) {
					Exp exp = formalParameters[i - 1];
					args[i] = exp != null ? exp.eval(context) : null;
				}
			} else {
				args = new Object[]{ context };
			}

			return mwrapper.execute(this, args);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			// @TODO: Melhorar esta exceção.
			throw new RuntimeException(this.toString(), ex);
		}
	}
}
