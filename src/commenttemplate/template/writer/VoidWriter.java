package commenttemplate.template.writer;

/**
 *
 * @author thiago
 */
public class VoidWriter implements Writer {
	
	
	public VoidWriter() {
	}

	public VoidWriter(int capacity) {
	}

	public VoidWriter(String str) {
	}

	public VoidWriter(CharSequence seq) {
	}

	public VoidWriter(boolean printNull) {
	}

	public VoidWriter(String str, boolean printNull) {
	}

	public VoidWriter(CharSequence seq, boolean printNull) {
	}

		
	@Override
	public Writer append(Object obj) {
		return this;
	}

	@Override
	public Writer append(String str) {
		return this;
	}

	@Override
	public Writer append(StringBuilder sb) {
		return this;
	}

	@Override
	public Writer append(StringBuffer sb) {
		return this;
	}

	@Override
	public Writer append(CharSequence s) {
		return this;
	}

	@Override
	public Writer append(CharSequence s, int start, int end) {
		return this;
	}

	@Override
	public Writer append(char str[]) {
		return this;
	}

	@Override
	public Writer append(char str[], int offset, int len) {
		return this;
	}

	@Override
	public Writer append(boolean b) {
		return this;
	}

	@Override
	public Writer append(char c) {
		return this;
	}

	@Override
	public Writer append(int i) {
		return this;
	}

	@Override
	public Writer append(long lng) {
		return this;
	}

	@Override
	public Writer append(float f) {
		return this;
	}

	@Override
	public Writer append(double d) {
		return this;
	}

	@Override
	public int length() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public String toString() {
		return "";
	}
}
