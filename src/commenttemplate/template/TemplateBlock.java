package commenttemplate.template;

import commenttemplate.context.Context;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public abstract class TemplateBlock {

	private TemplateBlock nextInner;
	private TemplateBlock nextInnerElse;
	private TemplateBlock next;

	public TemplateBlock() {
	}

	public TemplateBlock getNextInner() {
		return nextInner;
	}

	public void setNextInner(TemplateBlock nextInner) {
		this.nextInner = nextInner;
	}

	public void setNextInnerElse(TemplateBlock nextInnerElse) {
		this.nextInnerElse = nextInnerElse;
	}

	public TemplateBlock getNextInnerElse()  {
		return nextInnerElse;
	}

	public void setNext(TemplateBlock next) {
		this.next = next;
	}

	public TemplateBlock getNext() {
		return next;
	}

	public void putInEnd(TemplateBlock last) {
		TemplateBlock item = this;

		while (item.getNext() != null) {
			item = item.getNext();
		}

		item.setNext(last);
	}

	public void append(TemplateBlock other) {
		if (this.getNextInner() != null) {
			this.getNextInner().putInEnd(other);
		} else {
			this.setNextInner(other);
		}
	}

	public void appendToElse(TemplateBlock other) {
		if (this.getNextInnerElse() != null) {
			this.getNextInnerElse().putInEnd(other);
		} else {
			this.setNextInnerElse(other);
		}
	}

	@Override
	public abstract String toString();

	public abstract void toString(StringBuilder sb);

	public abstract void eval(Context context, Writer sb);
}
