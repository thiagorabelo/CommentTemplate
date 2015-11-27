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
package commenttemplate.expressions.operators.properties;

import commenttemplate.context.Context;
import commenttemplate.expressions.operators.core.BinaryOperator;
import commenttemplate.util.retrieve.IterativeRetrieverDataObject;

/**
 *
 * @author thiago
 */
public class Property extends BinaryOperator {
	private static final IterativeRetrieverDataObject retriever = new IterativeRetrieverDataObject();

	public static Object execute(Object l, Object r) {
		if (!isNull(l)) {
			String []ids = (String[])r;
			return retriever.getValue(l, ids);
		}

		return null;
	}

	@Override
	public String toString() {
		return ".";
	}

	@Override
	public Object eval(Context context) {
		Object left = getLeft().eval(context);
		Object right = getRight().eval(context);

		return execute(left, right);
	}
}
