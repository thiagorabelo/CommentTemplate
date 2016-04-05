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
package commenttemplate.template.tags.consequence;

import commenttemplate.context.Context;
import commenttemplate.template.nodes.Node;
import commenttemplate.template.tags.BasicTag;
import commenttemplate.template.writer.Writer;
import commenttemplate.template.tags.consequence.Consequence;

/**
 *
 * @author thiago
 */
public enum ConsequenceImplementation implements Consequence {

	SKIP_BODY {
		@Override
		public void doEval(BasicTag tag, Context context, Writer sb) {
			// do nothing
			return;
		}
	},
	EVAL_BODY {
		@Override
		public void doEval(BasicTag tag, Context context, Writer sb) {
			Node []nodeList;

			if ((nodeList = tag.getNodeList()) != null) {
				for (Node t : nodeList) {
					t.render(context, sb);
				}
			}
		}
	},
	EVAL_ELSE {
		@Override
		public void doEval(BasicTag tag, Context context, Writer sb) {
			Node []nodeListElse;

			if ((nodeListElse = tag.getNodeListElse()) != null) {
				for (Node t : nodeListElse) {
					t.render(context, sb);
				}
			}
		}
	}
	;

	@Override
	public void doEval(BasicTag tag, Context context, Writer sb) {
		throw new UnsupportedOperationException("Not supported.");
	}
}
