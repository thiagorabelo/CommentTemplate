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
package commenttemplate.template.tags.tags;

import commenttemplate.context.Context;
import commenttemplate.template.tags.MountingHelper;
import commenttemplate.template.nodes.Node;
import commenttemplate.template.tags.BasicTag;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class Literal extends BasicTag {
	
	private String stringRepr;

	@Override
	public void eval(Context context, Writer sb) {
		sb.append(stringRepr);
	}
	
	private class MountingLiteralsHelper extends MountingHelper {
		public MountingLiteralsHelper(Node n) {
			super(n);
		}
		
		@Override
		public Node buildNode(String innerContent) {
			Node node = super.buildNode(innerContent);
			Literal.this.stringRepr = innerContent;

			return node;
		}
		
		@Override
		public void appendToElse(Node n) {
		}
	}

	@Override
	public MountingHelper createMountingHelper() {
		return new MountingLiteralsHelper(this);
	}
}
