package commenttemplate.template;

import commenttemplate.context.Context;
import commenttemplate.template.writer.Writer;
import java.util.ArrayList;

/**
 *
 * @author thiago
 */
public abstract class TemplateBlock {

	private ArrayList<TemplateBlock> blockList;
	private ArrayList<TemplateBlock> blockListElse;


	public TemplateBlock() {
	}

	public ArrayList<TemplateBlock> getBlockList() {
		return blockList;
	}

	public void setBlockList(ArrayList<TemplateBlock> blockList) {
		this.blockList = blockList;
	}

	public ArrayList<TemplateBlock> getBlockListElse() {
		return blockListElse;
	}

	public void setBlockListElse(ArrayList<TemplateBlock> blockListElse) {
		this.blockListElse = blockListElse;
	}
	
	public void append(TemplateBlock other) {
		if (blockList == null) {
			blockList = new ArrayList<>();
		}
		
		blockList.add(other);
	}

	public void appendToElse(TemplateBlock other) {
		if (blockListElse == null) {
			blockListElse = new ArrayList<>();
		}

		blockListElse.add(other);
	}

	@Override
	public abstract String toString();

	public abstract void toString(StringBuilder sb);

	public abstract void eval(Context context, Writer sb);
}
