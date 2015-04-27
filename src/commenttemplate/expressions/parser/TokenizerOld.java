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

package commenttemplate.expressions.parser;

import java.util.ArrayList;
import java.util.List;
import commenttemplate.util.Tuple;

/**
 *
 * @author thiago
 */
public class TokenizerOld {

	private final String stream;
	private List<Tuple<String, Integer>> tokenList;
	private boolean executed = false;
	
	// @TODO: Lançar exceção caso a string seja nula?
	public TokenizerOld(String stream) {
		this.stream = stream;
	}
	
	// @TODO: A expressão <'olá mundo'.length>, não vira a lista de tokens ['olá mundo', ., length]
	//        Ver também na LazeTokenizer
	public List<Tuple<String, Integer>> tokenList() {
		if (!executed) {
			tokenList = new ArrayList<>();
			StringBuilder token = sb();
			int lastIndex = 0;
			boolean inString = false;
			char stringOpen = 0;
			boolean escaping = false;

			for (int i = 0, len = stream.length(); i < len; i++) {
				char ch = stream.charAt(i);
				
				if (!inString) {
					if (!Character.isWhitespace(ch)) {
						switch (ch) {
							case '\'':
							case '"':
								inString = true;
								stringOpen = ch;
								
								if (token.length() > 0) {
									tokenList.add(t(token.toString(), lastIndex));
									token = sb();
								}

								token.append(ch);
								lastIndex = i;// + 1; // 'ksdsasa' - lastIndex tava apontando pra ksd... não pra 'ksd...

								break;
							case '*':
								i = lookahead(i, token, '*', lastIndex);
								token = sb();
								lastIndex = i + 1;

								break;

							case '$': // Tokeniza, mas não é usado
							case '{': // Tokeniza, mas não é usado
							case '}': // Tokeniza, mas não é usado
							case '(':
							case ')':
							case '/':
							case '%':
							case '^':
							case '+':
							case '-':
							case ',':
								if (token.length() > 0) {
									tokenList.add(t(token.toString(), lastIndex));
									token = sb();
								}

								tokenList.add(t(Character.toString(ch), i));
								lastIndex = i + 1;

								break;
							
							case '.':
								lastIndex = lookbehind(i, token, ')', lastIndex);
								break;

							case '!': // !=
							case '<': // <=
							case '>': // >=
							case '=': // ==
								i = lookahead(i, token, '=', lastIndex);
								token = sb();
								lastIndex = i + 1;

								break;

							case '&': // &&
								i = lookahead(i, token, '&', lastIndex);
								token = sb();
								lastIndex = i + 1;

								break;

							case '|': // ||
								i = lookahead(i, token, '|', lastIndex);
								token = sb();
								lastIndex = i + 1;

								break;

							default:
								token.append(ch);
								break;
						}
					} else if (token.length() > 0) {
						tokenList.add(t(token.toString(), lastIndex));
						token = sb();
						lastIndex = i + 1;
					} else {
						lastIndex = i + 1;
					}
				} else {
					if (ch == stringOpen && !escaping) {
						token.append(ch);
						tokenList.add(t(token.toString(), lastIndex));

						token = sb();						
						lastIndex = i + 1;
						
						inString = false;
						stringOpen = 0;
					} else {
						if (!escaping) {
							if (ch != '\\') {
								token.append(ch);
							} else {
								escaping = true;
							}
						} else {
							switch (ch) {
								case 't':
									token.append("\t");
									break;
								case 'b':
									token.append("\b");
									break;
								case 'n':
									token.append("\n");
									break;
								case 'r':
									token.append("\r");
									break;
								case 'f':
									token.append("\f");
									break;
								case '\'':
									token.append("'");
									break;
								case '"':
									token.append('"');
									break;
								case '\\':
									token.append("\\");
									break;
								case 'u':
									int j, count;
									int code;
									char [] u = { '\0', '\0', '\0', '\0' };

									for (j = i + 1, count = 0; j < len && count < 4; j++, count++) {
										char c = stream.charAt(j);
										
										if (c < '0' && c > '9') {
											break;
										}
										
										u[count] = c;
									}

									code = charHexToInt(u, count - 1);

									token.append((char)code);

									i = j - 1;

									break;
								default:
									token.append(ch);
							}

							escaping = false;
						}
					}
				}
			}
			
			if (token.length() > 0) {
				tokenList.add(t(token.toString(), lastIndex));
			}

			executed = true;
		}

		return tokenList;
	}

	// Converte uma string que representa um hexadecimal (uffff) para um inteiro em hexadecimal 0xffff
	private int charHexToInt(char [] u, int count) {
		int code = 0, base = 1;
		
		for (int k = count; k >= 0; k--) {
			int val;

			switch (u[k]) {
				case '0':
					val = 0;
					break;
				case '1':
					val = 1;
					break;
				case '2':
					val = 2;
					break;
				case '3':
					val = 3;
					break;
				case '4':
					val = 4;
					break;
				case '5':
					val = 5;
					break;
				case '6':
					val = 6;
					break;
				case '7':
					val = 7;
					break;
				case '8':
					val = 8;
					break;
				case '9':
					val = 9;
					break;
				case 'a':
				case 'A':
					val = 10;
					break;
				case 'b':
				case 'B':
					val = 11;
					break;
				case 'c':
				case 'C':
					val = 12;
					break;
				case 'd':
				case 'D':
					val = 13;
					break;
				case 'e':
				case 'E':
					val = 14;
					break;
				case 'f':
				case 'F':
					val = 15;
					break;
				default:
					val = 0;
			}
			
			code += val * base;
			base *= 16;
		}
		
		return code;
	}

	private StringBuilder sb() {
		return new StringBuilder();
	}
	
	private int lookahead(int idx, StringBuilder sb, char next, int lastIndex) {
		int nxtIdx = idx + 1, len = stream.length();
		char ch = stream.charAt(idx);

		if (nxtIdx >= len) {
			if (sb.length() > 0) {
				tokenList.add(t(sb.toString(), lastIndex));
			}

			tokenList.add(t(Character.toString(ch), idx));
			
			return idx;
		} else {
			if (sb.length() > 0) {
				tokenList.add(t(sb.toString(), lastIndex));
				sb = sb();
			}

			sb.append(ch);

			char nextCh = stream.charAt(nxtIdx);

			if (nextCh == next) {
				sb.append(nextCh);
			}

			tokenList.add(t(sb.toString(), idx));
			
			return nextCh == next ? nxtIdx : idx;
		}
	}
	
	private int lookbehind(int idx, StringBuilder sb, char prev, int lastLength) {
		int listIdx = tokenList.size() - 1;
		
		if (idx > 0 && listIdx >= 0) {
			Tuple<String, Integer> tuple = tokenList.get(listIdx);

			if (tuple.getA().equals(""+prev)) {
				tokenList.add(t(""+stream.charAt(idx), lastLength));
				return idx + 1;
			}
		}

		sb.append(stream.charAt(idx));

		return lastLength;
	}
	
	public String getStream() {
		return stream;
	}
	
	private Tuple<String, Integer> t(String token, int index) {
		return new Tuple<>(token, index);
	}
}
