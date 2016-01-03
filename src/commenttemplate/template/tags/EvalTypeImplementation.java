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
package commenttemplate.template.tags;

import commenttemplate.context.Context;
import commenttemplate.template.nodes.Node;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public enum EvalTypeImplementation implements EvalType {
	SKIP_BODY {
		@Override
		public void doEval(AbstractTag tag, Context context, Writer sb) {
			// do nothing
		}
	},
	EVAL_BODY {
		@Override
		public void doEval(AbstractTag tag, Context context, Writer sb) {
			Node []nodeList;

			if ((nodeList = tag.getNodeList()) != null) {
				tag.start(context, sb);
				tag.loopNodeList(nodeList, context, sb);
				tag.end(context, sb);
			}
		}
	},
	EVAL_ELSE {
		@Override
		public void doEval(AbstractTag tag, Context context, Writer sb) {
			Node []nodeListElse;

			if ((nodeListElse = tag.getNodeListElse()) != null) {
				tag.start(context, sb);
				tag.loopNodeList(nodeListElse, context, sb);
				tag.end(context, sb);
			}
		}
	}
	;

	@Override
	public void doEval(AbstractTag tag, Context context, Writer sb) {
		throw new UnsupportedOperationException("Not supported.");
	}
}
