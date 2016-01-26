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
package commenttemplate.template.nodes;

import commenttemplate.context.Context;
import commenttemplate.template.tags.MountingHelper;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public abstract class AbstractNode implements Node {

	private Node []nodeList;
	private Node []nodeListElse;

	@Override
	public Node[] getNodeList() {
		return nodeList;
	}

	@Override
	public void setNodeList(Node[] nodeList) {
		this.nodeList = nodeList;
	}

	@Override
	public Node[] getNodeListElse() {
		return nodeListElse;
	}

	@Override
	public void setNodeListElse(Node[] nodeListElse) {
		this.nodeListElse = nodeListElse;
	}

	public MountingHelper createMountingHelper() {
		return new MountingHelper(this);
	}

	@Override
	public abstract String toString();

	@Override
	public abstract void toString(StringBuilder sb);

	@Override
	public abstract void render(Context context, Writer sb);
}
