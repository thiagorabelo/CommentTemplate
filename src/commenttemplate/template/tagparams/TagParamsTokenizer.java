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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thiago
 */
public class TagParamsTokenizer {
	public static void main(String[] args) {
		String []t = {
			"foo=\"bar\" thiago= \" rabelo\" torres =\"sales\" abc = \"cde\"",
			"foo=(bar) thiago= ( rabelo) torres =(sales) abc = (cde)",
			"foo=( thiago * ( rabelo + torres ) % sales )"
		};

		for (String s : t) {
			TagParamsTokenizer tpt = new TagParamsTokenizer(s);
			System.out.println(s);
			List<String[]> list = tpt.tokens();
			for (String []tks : list) {
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
	private static final int WITH_QUOTES = 0;
	private static final int WITH_PARENTH = 1;

	private final String stream;
	
	public TagParamsTokenizer(String input) {
		stream = input;
	}

	public List<String[]> tokens() {
		List<String[]> listTokens = new ArrayList<String[]>();
		StringBuilder left = sb();

		for (int i = 0, len = stream.length(); i < len; i++) {
			char ch = stream.charAt(i);
			
			if (!Character.isWhitespace(ch)) {
				if (!ASSIGNMENT.equals("" + ch)) {
					left.append(ch);
				} else {
					String []tokens = new String[2];
					tokens[0] = left.toString();
					i = buildRight(tokens, i + 1, len);
					listTokens.add(tokens);
					left = sb();
				}
			}
		}

		return listTokens;
	}

	protected int buildRight(String []tokens, int startIndex, int length) {
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
			// @TODO: Sapora tá faltando tudo!
			throw new RuntimeException("Err: " + stream + " [" + i + "]");
		}
		
		if (cw == WITH_QUOTES) {
			return withQuotes(tokens, i + 1, length);
		}
		
		return withParenth(tokens, i + 1, length);
	}

	private int withParenth(String []tokens, int startIndex, int length) {
		StringBuilder right = sb();
		boolean opened = true;
		int i, openedParenth = 0;

		for (i = startIndex; i < length; i++) {
			String sch = "" + stream.charAt(i);
			
			if (sch.equals(ALLOWED_CLOSING[WITH_PARENTH]) && openedParenth == 0) {
				opened = false;
				tokens[1] = right.toString();
				break;
			} else {
				if (sch.equals(ALLOWED_OPENING[WITH_PARENTH])) {
					openedParenth++;
				} else if (sch.equals(ALLOWED_CLOSING[WITH_PARENTH])) {
					openedParenth--;
				}

				right.append(sch);
			}
		}

		if (opened) {
			// @TODO: Sapora tá faltando tudo!
			throw new RuntimeException("Err: " + stream + " [" + i + "]");
		}

		return i;
	}

	private int withQuotes(String []tokens, int startIndex, int length) {
		StringBuilder right = sb();
		boolean opened = true;
		int i;

		for (i = startIndex; i < length; i++) {
			String sch = "" + stream.charAt(i);
			
			if (sch.equals(ALLOWED_CLOSING[WITH_QUOTES])) {
				opened = false;
				tokens[1] = right.toString();
				break;
			} else {
				right.append(sch);
			}
		}

		if (opened) {
			// @TODO: Sapora tá faltando tudo!
			throw new RuntimeException("Err: " + stream + " [" + i + "]");
		}

		return i;
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

	public String getStream() {
		return stream;
	}
}
