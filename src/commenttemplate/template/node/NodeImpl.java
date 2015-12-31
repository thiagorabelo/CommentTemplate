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
package commenttemplate.template.node;

import commenttemplate.context.Context;
import commenttemplate.template.tags.managers.TagManager;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public abstract class NodeImpl implements Node {

	private NodeImpl []nodeList;
	private NodeImpl []nodeListElse;

	private TagManager tagManager;

	@Override
	public NodeImpl[] getNodeList() {
		return nodeList;
	}

	public void setNodeList(NodeImpl[] nodeList) {
		this.nodeList = nodeList;
	}

	@Override
	public NodeImpl[] getNodeListElse() {
		return nodeListElse;
	}

	public void setNodeListElse(NodeImpl[] nodeListElse) {
		this.nodeListElse = nodeListElse;
	}

	@Override
	public abstract String toString();

	public abstract void toString(StringBuilder sb);

	public abstract void eval(Context context, Writer sb);
}
