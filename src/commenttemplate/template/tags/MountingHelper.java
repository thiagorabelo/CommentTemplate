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
package commenttemplate.template.tags;

import commenttemplate.template.nodes.Node;
import java.util.ArrayList;

/**
 *
 * @author thiago
 */
public class MountingHelper {
	
	Node block;
	ArrayList<Node> nodeList;
	ArrayList<Node> nodeListElse;
	
	public MountingHelper(Node b) {
		block = b;
		nodeList = new ArrayList<Node>();
		nodeListElse = new ArrayList<Node>();
	}

	public Node getBlock() {
		return block;
	}

	public void setBlock(Node block) {
		this.block = block;
	}

	public ArrayList<Node> getNodeList() {
		return nodeList;
	}

	public void setNodeList(ArrayList<Node> nodeList) {
		this.nodeList = nodeList;
	}

	public ArrayList<Node> getNodeListElse() {
		return nodeListElse;
	}

	public void setNodeListElse(ArrayList<Node> nodeListElse) {
		this.nodeListElse = nodeListElse;
	}
	
	public Node buildNode(String innerContent) {
		//System.out.println("=====================\n{"+innerContent+"}");
		if (!nodeList.isEmpty()) {
			Node []list = new Node[nodeList.size()];
			nodeList.toArray(list);
			block.setNodeList(list);
			nodeList.clear();
		}

		if (!nodeListElse.isEmpty()) {
			Node []list = new Node[nodeListElse.size()];
			nodeListElse.toArray(list);
			block.setNodeListElse(list);
			nodeListElse.clear();
		}

		return block;
	}
	
	public void append(Node n) {
		nodeList.add(n);
	}

	public void appendToElse(Node n) {
		nodeListElse.add(n);
	}
}
