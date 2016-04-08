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
/**
 * @TODO: BUG
 *	- Quando uma expressão dentro do operador ${} possui espaços vazios
 *	  (ex: ${ foo }) o Tokenizer retornar o token seguido de um token "vazio"
 *	  (ex: tokens=["foo", ""]).
 */
public class Tokenizer {
	
	private final LazeTokenizer laze;
	private List<Tuple<String, Integer>> tokenList = null;
	
	// @TODO: Lançar exceção caso a string seja nula?
	public Tokenizer(String stream) {
		laze = new LazeTokenizer(stream);
	}
	
	public String getStream() {
		return laze.getStream();
	}
	
	public List<Tuple<String, Integer>> tokenList() {
		if (tokenList == null) {
			tokenList = new ArrayList<>();
			for (Tuple<String, Integer> token : laze) {
				tokenList.add(token);
			}
		}

		return tokenList;
	}
}
