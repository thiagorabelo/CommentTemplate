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
package commenttemplate.template.tagparams;

import commenttemplate.template.exceptions.InvalidParamsSintaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author thiago
 */
public class TagParamsTokenizer implements Iterable<String[]> {

	public static void main(String[] args) {
		String []t = {
			"abcd.RegA == \"Thiago\"",
//			"foo=\"bar\" one= \" tow\" three =\"four\" abc = \"cde\"",
//			"foo=(bar) one= ( two) three =(four) abc = (cde)",
//			"foo=( one * ( two + three ) % four  )",
//			"foo   bar=(name)   no_val    mortal=\"kombat\"",
//			" \"extends/cabecalho.html\"",
//			" 'vish maria'",
//			" one * ( two + three  % four ) ",
//			""
		};

		for (String s : t) {
			System.out.println(s);
			for (String []tks : new TagParamsTokenizer(s)) {
				System.out.flush();
				System.out.println("[0]::" + tks[0]);
				System.out.println("[1]::" + tks[1]);
				System.out.println("-------------------");
			}
			System.out.println("\n===================\n");
		}
	}

	//                                                 0    1
	private static final String []ALLOWED_OPENING = {"\"", "("};
	private static final String []ALLOWED_CLOSING = {"\"", ")"};
	private static final String ASSIGNMENT = "=";
	private static final String []PRE_ASSIGNMENT_EXCLUDES = {"=", "!", "<", ">"};
	private static final int WITH_QUOTES = 0;
	private static final int WITH_PARENTH = 1;

	private final String stream;
	
	public TagParamsTokenizer(String input) {
		stream = input;
	}

	@Override
	public Iterator<String[]> iterator() {
		return new TagParamsTokenizerIterator();
	}

	private class TagParamsTokenizerIterator implements Iterator<String[]> {
		private final int length = stream.length();
		private int index = 0;

		@Override
		public boolean hasNext() {
			for (; index < length; index++) {
				if (!Character.isWhitespace(stream.charAt(index))) {
					return true;
				}
			}

			return false;
		}

		@Override
		public String[] next() throws InvalidParamsSintaxException {
			StringBuilder left = sb();
			int paramBegin = -1;
			String []tokens = new String[2];

			for (; index < length; index++) {
				char ch = stream.charAt(index);
				boolean whiteSpace = Character.isWhitespace(ch);

				if (!whiteSpace && paramBegin > -1) {
					boolean assigment = isAssignment(ch);//ASSIGNMENT.equals("" + ch);
					if (!assigment && tokens[0] == null) {
						left.append(ch);
						if (paramBegin == -1) {
							paramBegin = index;
						}
					} else if (tokens[0] != null && !assigment) {
						left = null;
						return tokens;
					} else {
						if (tokens[0] == null) {
							tokens[0] = left.toString();
						}
						index = buildRight(tokens, index + 1, length, paramBegin);
						left = null;
						return tokens;
					}
				} else if (!whiteSpace) {
					left.append(ch);
					if (paramBegin == -1) {
						paramBegin = index;
					}
				} else if (tokens[0] == null) {
					tokens[0] = left.toString();
					left = sb();
				}
			}

			if (index < (length - 1) && left.length() > 0) {
				throw new InvalidParamsSintaxException(stream, paramBegin, "Invalid token [", left, "]");
			} else if (tokens[0] != null) {
				return tokens;
			}

			return new String[] {left.toString(), null};
		}

		protected int buildRight(String []tokens, int startIndex, int length, int paramBegin) {
			int i, cw = -1;

			for (i = startIndex; i < length; i++) {
				char ch = stream.charAt(i);
				String sch = "" + ch;

				if (Character.isWhitespace(ch)) continue;

				if ((cw = closeWith(sch)) > -1) {
					break;
				}
			}

			if (cw == -1) {
				throw new InvalidParamsSintaxException(stream, startIndex, "Invalid sitax [", stream, "]");
			}

			if (cw == WITH_QUOTES) {
				return withQuotes(tokens, i + 1, length, paramBegin);
			}

			return withParenth(tokens, i + 1, length);
		}

		private int withParenth(String []tokens, int startIndex, int length) {
			StringBuilder right = sb();
			int i, openedParenth = 0;

			for (i = startIndex; i < length; i++) {
				String sch = "" + stream.charAt(i);

				if (sch.equals(ALLOWED_CLOSING[WITH_PARENTH]) && openedParenth == 0) {
					tokens[1] = right.toString();
					return i + 1;
				} else {
					if (sch.equals(ALLOWED_OPENING[WITH_PARENTH])) {
						openedParenth++;
					} else if (sch.equals(ALLOWED_CLOSING[WITH_PARENTH])) {
						openedParenth--;
					}

					right.append(sch);
				}
			}

			throw new InvalidParamsSintaxException(stream, startIndex, "Unbalanced parentheses [", stream, "]");
		}

		private int withQuotes(String []tokens, int startIndex, int length, int paramBegin) {
			StringBuilder right = sb();
			int i;

			for (i = startIndex; i < length; i++) {
				String sch = "" + stream.charAt(i);

				if (sch.equals(ALLOWED_CLOSING[WITH_QUOTES])) {
					tokens[1] = right.toString();
					return i + 1;
				} else {
					right.append(sch);
				}
			}

			throw new InvalidParamsSintaxException(stream, startIndex, "Unbalanced parentheses [", stream, "]");
		}

		private int closeWith(String ch) {
			if (ALLOWED_OPENING[WITH_QUOTES].equals(ch)) {
				return WITH_QUOTES;
			} else if (ALLOWED_OPENING[WITH_PARENTH].equals(ch)) {
				return WITH_PARENTH;
			}

			return -1;
		}

		private StringBuilder sb() {
			return new StringBuilder();
		}

		private boolean isAssignment(char ch) {
			boolean is_assigment = ASSIGNMENT.equals("" + ch);
			
			if (!is_assigment) {
				return false;
			}

			for (String s : PRE_ASSIGNMENT_EXCLUDES) {
				if (("" + stream.charAt(index - 1)).equals(s)) {
					return false;
				}
			}

			return is_assigment && !(stream.charAt(index + 1) + "").equals(ASSIGNMENT);
		}
	}

	public List<String[]> tokens() {
		List<String[]> listTokens = new ArrayList<String[]>();

		for (String [] tokens : this) {
			listTokens.add(tokens);
		}

		return listTokens;
	}

	public String getStream() {
		return stream;
	}
}
