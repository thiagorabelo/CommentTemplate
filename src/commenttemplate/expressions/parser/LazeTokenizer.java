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

import commenttemplate.util.Tuple;
import java.util.Iterator;

/**
 *
 * @author thiago
 */
public class LazeTokenizer implements Iterable<Tuple<String, Integer>> {
	
	
//	public static void main(String[] args) {
//		String exp = "concat('vish').length + pessoa.idade";
//		for (Tuple<String, Integer> t : new LazeTokenizer(exp)) {
//			System.out.println(t.getA()+": "+t.getB());
//		}
//	}
	
	private static enum Status {
		BUFFERIZE, KEEP_BUFFERING, AVOID, FLUSH, BUFFERIZE_AND_FLUSH
	};

	private final String stream;
	
	// @TODO: Lançar exceção caso a string seja nula?
	public LazeTokenizer(String stream) {
		this.stream = stream;
	}
	
	private class TokenIterator implements Iterator<Tuple<String, Integer>> {
		private StringBuilder buffer = sb();
		private int length = stream.length();
		private Tuple<String, Integer> last = null;
		private int stopedAt = 0;
		private int idx = 0;
		private boolean inString = false;
		private char stringOpen = 0;
		private boolean escaping = false;
		private Status status = Status.BUFFERIZE;
		

		
		@Override
		public boolean hasNext() {
			return idx < length;
		}
		
		private void lookingahead(int i, char expected) {
			if (status == Status.FLUSH) {
				if (lookahead(i, expected)) {
					status = Status.KEEP_BUFFERING;
				} else {
					status = Status.FLUSH;
				}
			} else if (status == Status.KEEP_BUFFERING) {
				status = Status.BUFFERIZE_AND_FLUSH;
			} else if (!filledBuffer() && lookahead(i, expected)) {
				status = Status.KEEP_BUFFERING;
			} else {
				status = Status.FLUSH;
			}
		}
		
		public char nextChar(int i) {
			char ch = stream.charAt(i);
			
			if (!inString) {
				if (!Character.isWhitespace(ch)) {
					switch (ch) {
						case '\'':
						case '"':
							inString = true;
							stringOpen = ch;

							if (status == Status.FLUSH) {
								status = Status.BUFFERIZE;
							} else if (filledBuffer()) {
								status = Status.FLUSH;
							} else {
								status = Status.KEEP_BUFFERING;
							}

							break;
						
						case '*':
							lookingahead(i, '*');
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

							if (status != Status.FLUSH) {
								status = Status.FLUSH;
							}

							break;

						case '.':

							if (status == Status.FLUSH) {
								if (lookbehind(i, ')', '\'', '"')) {
									status = Status.FLUSH;
								} else {
									status = Status.BUFFERIZE_AND_FLUSH;
								}
							} else if (filledBuffer()) {
								status = Status.BUFFERIZE;
							} else {
								status = Status.BUFFERIZE_AND_FLUSH;
							}

							break;

						case '!': // !=
						case '<': // <=
						case '>': // >=
						case '=': // ==

							lookingahead(i, '=');
							break;

						case '&': // &&

							lookingahead(i, '&');
							break;

						case '|': // ||

							lookingahead(i, '|');
							break;

						default:
							status = Status.BUFFERIZE;
					}

				} else if (filledBuffer()) {
					status = Status.FLUSH;
				} else {
					status = Status.AVOID;
				}
			} else {
				if (ch == stringOpen && !escaping) {
					inString = false;
					stringOpen = 0;
					status = Status.BUFFERIZE_AND_FLUSH;
				} else {
					if (!escaping) {
						if (ch != '\\') {
							status = Status.BUFFERIZE;
						} else {
							status = Status.AVOID;
							escaping = true;
						}
					} else {
						switch (ch) {
							case 't':
								ch = '\t';
								break;
							case 'b':
								ch = '\b';
								break;
							case 'n':
								ch = '\n';
								break;
							case 'r':
								ch = '\r';
								break;
							case 'f':
								ch = '\f';
								break;
							case '\'':
								ch = '\'';
								break;
							case '"':
								ch = '"';
								break;
							case '\\':
								ch = '\\';
								break;
							/* @TODO: Rever este caso */
							/*
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
							*/
						}
						status = Status.BUFFERIZE;
						escaping = false;
					}
				}
			}
			
			return ch;
		}
		
		@Override
		public Tuple<String, Integer> next() {
			
			for (; idx < length; idx++) {
				char ch = nextChar(idx);
				
				if (status == Status.FLUSH) {
					if (filledBuffer()) {
						last = t(buffer, idx - buffer.length());
						buffer = sb();
						return last;
					} else {
						buffer.append(ch);
						status = Status.BUFFERIZE;
						last = t(buffer, ++idx - buffer.length());
						buffer = sb();
						return last;
					}
				} else if (status == Status.BUFFERIZE || status == Status.KEEP_BUFFERING) {
					buffer.append(ch);
				} else if (status == Status.BUFFERIZE_AND_FLUSH) {
					buffer.append(ch);
					status = Status.FLUSH;
					last = t(buffer, ++idx - buffer.length());
					buffer = sb();
					return last;
				}
			}
			
			return t(buffer, idx++ - buffer.length());
		}
		
		private boolean filledBuffer() {
			return buffer.length() > 0;
		}
		
		private boolean lookahead(int currentIndex, char expected) {
			int nextIndex = currentIndex + 1, len = stream.length();

			if (nextIndex >= len) {
				return false;
			} else {
				return stream.charAt(nextIndex) == expected;
			}
		}

		private boolean lookbehind(int currentIndex, char ...expectedPrev) {
			if (currentIndex > 0 && last != null) {
				for (char ch : expectedPrev) {
					String l = last.getA();
					if (l.charAt(l.length() - 1) == ch) {
						return false;
					}
				}
			}

			return true;
		}
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

	private StringBuilder sb(Object obj) {
		return new StringBuilder().append(obj);
	}
	
	public String getStream() {
		return stream;
	}
	
	@Override
	public Iterator<Tuple<String, Integer>> iterator() {
		return new TokenIterator();
	}

	private Tuple<String, Integer> t(StringBuilder buffer, int index) {
		return t(buffer.toString(), index);
	}

	private Tuple<String, Integer> t(String token, int index) {
		return new Tuple<>(token, index);
	}
}
