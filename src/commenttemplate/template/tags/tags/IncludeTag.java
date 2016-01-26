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

package commenttemplate.template.tags.tags;

import commenttemplate.context.Context;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.loader.TemplateLoader;
import commenttemplate.template.exceptions.TemplateException;
import commenttemplate.template.nodes.RootNode;
import commenttemplate.template.tags.MappableTag;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class IncludeTag extends MappableTag {
	
	private Exp template;
	
	public void setTemplate(Exp template) {
		this.template = template;
	}
	
	@Override
	public void eval(Context context, Writer sb) {
		try {
			RootNode node = TemplateLoader.get(template.eval(context).toString());

			String result = node.render(context);

			sb.append(result);
		} catch (TemplateException ex) {
			// @TODO: fazer o quê?
			throw new RuntimeException(ex);
		}
	}
}
