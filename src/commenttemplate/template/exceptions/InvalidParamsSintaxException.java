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
package commenttemplate.template.exceptions;

import commenttemplate.util.Utils;

/**
 *
 * @author thiago
 */
// Must be RuntimeException to be allowed use in TagParamsTokenizer (Iterable)
public class InvalidParamsSintaxException extends RuntimeException {
	
	private String params;
	private int index;
	
	public InvalidParamsSintaxException(String params, int index, Object ...msg) {
		super(msg != null && msg.length > 0 ? Utils.concat(msg) : null);
		this.params = params;
		this.index = index;
	}

	public InvalidParamsSintaxException(String params, int index) {
		this(params, index, (Object)null);
	}

	public String getParams() {
		return params;
	}

	public int getIndex() {
		return index;
	}
}
