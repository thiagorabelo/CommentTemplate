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
package commenttemplate.template.tags;

import commenttemplate.context.Context;
import commenttemplate.expressions.tree.Exp;
import static commenttemplate.template.tags.AbstractTag.EVAL_BODY;
import commenttemplate.template.writer.Writer;
import commenttemplate.util.Join;
import commenttemplate.util.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author thiago
 */
public abstract class MappableTag extends AbstractTag {
	
	protected HashMap<String, Exp> params = new HashMap<>();
	
	@Override
	public void eval(Context context, Writer sb) {
		EVAL_BODY.doEval(this, context, sb);
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

	@Override
	public String paramsToString() {
		return super.paramsToString() + " "  + Join.with(" ").these(new Iterator<String>() {

			private final Iterator<String> it = params.keySet().iterator();

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public String next() {
				String p = it.next();
				return Utils.concat(p, "=\"", params.get(p).toString(), '"');
			}
		});
	}
}
