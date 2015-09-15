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

package commenttemplate.template.tags.builtin;

import commenttemplate.context.Context;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.template.tags.Tag;
import commenttemplate.template.tags.TypeEval;
import commenttemplate.template.writer.Writer;
import commenttemplate.util.MyHashMap;
import java.util.Map;

/**
 *
 * @author thiago
 */
public class WithTag extends Tag {
	
	protected MyHashMap<String, Exp> params = new MyHashMap<>();
	
	@Override
	public TypeEval evalParams(Context context, Writer sb) {
		return EVAL_BODY;
	}
	
	@Override
	public void start(Context context, Writer sb) {
		context.push();

		for (Map.Entry<String, Exp> e : params.entrySet()) {
			context.put(e.getKey(), e.getValue().eval(context));
		}
	}

	@Override
	public void end(Context context, Writer sb) {
		context.pop();
	}
}