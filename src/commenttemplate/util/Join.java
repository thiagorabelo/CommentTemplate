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
package commenttemplate.util;

import commenttemplate.util.joiner.DefaultJoiner;
import commenttemplate.util.joiner.JoinPath;
import commenttemplate.util.joiner.Joiner;
import java.io.File;

/**
 *
 * @author thiago
 */
public class Join {
	
	public static Joiner with(String joiner) {
		return new DefaultJoiner(joiner);
	}
	
	public static Joiner path() {
		return new JoinPath(File.separator);
	}
	
	public static Joiner path(String joiner) {
		return new JoinPath(joiner);
	}
}
