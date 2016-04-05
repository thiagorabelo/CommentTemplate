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
import commenttemplate.template.tags.consequence.Consequence;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public abstract class LoopTag extends BasicTag {

	public abstract Consequence doTest(Context context, Writer sb);
	public abstract void init(Context context, Writer sb);


	@Override
	public void eval(Context context, Writer sb) {
		init(context, sb);
		
		Consequence c;
		
		if ((c = doTest(context, sb)) == EVAL_BODY) {

			do {

				EVAL_BODY.doEval(this, context, sb);

			} while (doTest(context, sb) == EVAL_BODY);

		} else if (c == EVAL_ELSE) {
			c.doEval(this, context, sb);
		}
	}

	@Override
	public void start(Context context, Writer sb) {
		context.push();
	}

	@Override
	public void end(Context context, Writer sb) {
		context.pop();
	}
}
