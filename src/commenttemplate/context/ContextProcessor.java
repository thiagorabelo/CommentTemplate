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

package commenttemplate.context;

import commenttemplate.template.writer.Writer;

/**
 * Classes that extends {@code ContextProcessor} proccess a {@code Context}
 * instance before the process of renderization and process the {@code Writer}
 * and {@code Context} instance after the process renderization.
 * 
 * @see commenttemplate.context.Context
 * @see commenttemplate.template.writer.Writer
 * 
 * @author thiago
 */
public abstract class ContextProcessor {

	/**
	 * Used to manipulate de {@code Context} instance before render process
	 * 
	 * @param context {@code Context} used to do the renderization of the
	 * template
	 */
	public void before(Context context) {}

	/**
	 * Used to manipulate the {@code Writer} and {@code Context} intances
	 * after reder process.
	 * 
	 * @param sb Output where the rendered template is buffered
	 * @param context {@code Context} instance used to render the template
	 */
	public void after(Writer sb, Context context) {}

}
