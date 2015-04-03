package commenttemplate.template.writer;

import java.io.Serializable;

/**
 *
 * @author thiago
 */
public interface Writer extends Serializable {
	
	public Writer append(Object obj);

	public Writer append(String str);

	public Writer append(StringBuilder sb);

	public Writer append(StringBuffer sb);

	public Writer append(CharSequence s);

	public Writer append(CharSequence s, int start, int end);

	public Writer append(char str[]);

	public Writer append(char str[], int offset, int len);

	public Writer append(boolean b);

	public Writer append(char c);

	public Writer append(int i);

	public Writer append(long lng);

	public Writer append(float f);

	public Writer append(double d);

	public int length();

	public boolean isEmpty();

	@Override
	public String toString();
}
