package commenttemplate.template;

import commenttemplate.context.Context;
import commenttemplate.template.writer.Writer;
import java.util.ArrayList;

/**
 *
 * @author thiago
 */
public abstract class TemplateBlock {

	private TemplateBlock []blockList;
	private TemplateBlock []blockListElse;


	public TemplateBlock() {
	}

	public TemplateBlock []getBlockList() {
		return blockList;
	}

	public void setBlockList(TemplateBlock []blockList) {
		this.blockList = blockList;
	}

	public TemplateBlock []getBlockListElse() {
		return blockListElse;
	}

	public void setBlockListElse(TemplateBlock []blockListElse) {
		this.blockListElse = blockListElse;
	}
	
	public MountingHelper createMountingHelper() {
		return new MountingHelper(this);
	}

	@Override
	public abstract String toString();

	public abstract void toString(StringBuilder sb);

	public abstract void eval(Context context, Writer sb);
}
