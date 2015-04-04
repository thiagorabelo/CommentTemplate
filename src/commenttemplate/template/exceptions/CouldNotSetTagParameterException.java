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
public class CouldNotSetTagParameterException extends Exception {
	private final String tagName;
	
	public CouldNotSetTagParameterException(String tagName, String paramName, Throwable cause) {
		super(paramName, cause);
		this.tagName = tagName;
	}
	
	@Override
	public String getMessage() {
		return Utils.concat("Could not set parameter called \"", super.getMessage(), "\" in tag [", tagName, "]");
	}
}
