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
package commenttemplate.template;

import java.util.ArrayList;

/**
 *
 * @author thiago
 */
public class MountingHelper {
	
	TemplateBlock block;
	ArrayList<TemplateBlock> nodeList;
	ArrayList<TemplateBlock> nodeListElse;
	
	public MountingHelper(TemplateBlock b) {
		block = b;
		nodeList = new ArrayList<>();
		nodeListElse = new ArrayList<>();
	}

	public TemplateBlock getBlock() {
		return block;
	}

	public void setBlock(TemplateBlock block) {
		this.block = block;
	}

	public ArrayList<TemplateBlock> getNodeList() {
		return nodeList;
	}

	public void setNodeList(ArrayList<TemplateBlock> nodeList) {
		this.nodeList = nodeList;
	}

	public ArrayList<TemplateBlock> getNodeListElse() {
		return nodeListElse;
	}

	public void setNodeListElse(ArrayList<TemplateBlock> nodeListElse) {
		this.nodeListElse = nodeListElse;
	}
	
	public TemplateBlock buildBlock(String innerContent) {
		//System.out.println("=====================\n{"+innerContent+"}");
		if (!nodeList.isEmpty()) {
			TemplateBlock []list = new TemplateBlock[nodeList.size()];
			nodeList.toArray(list);
			block.setBlockList(list);
			nodeList.clear();
		}

		if (!nodeListElse.isEmpty()) {
			TemplateBlock []list = new TemplateBlock[nodeListElse.size()];
			nodeListElse.toArray(list);
			block.setBlockListElse(list);
			nodeListElse.clear();
		}

		return block;
	}
	
	public void append(TemplateBlock b) {
		nodeList.add(b);
	}

	public void appendToElse(TemplateBlock b) {
		nodeListElse.add(b);
	}
}
