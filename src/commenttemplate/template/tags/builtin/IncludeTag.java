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

package commenttemplate.template.tags.builtin;

import commenttemplate.context.Context;
import commenttemplate.expressions.tree.Exp;
import commenttemplate.loader.TemplateLoader;
import commenttemplate.template.TemplateBlockBase;
import commenttemplate.template.exceptions.TemplateException;
import commenttemplate.template.tags.TypeEval;
import commenttemplate.template.writer.TemplateWriter;
import commenttemplate.template.writer.Writer;

/**
 *
 * @author thiago
 */
public class IncludeTag extends WithTag {
	
	private Exp template;
	
	public void setTemplate(Exp template) {
		this.template = template;
	}
	
	@Override
	public TypeEval evalParams(Context context, Writer sb) {
		return SKIP_BODY;
	}
	
	@Override
	public void eval(Context context, Writer sb) {
		try {
			TemplateBlockBase block = TemplateLoader.get(template.eval(context).toString());

			start(context, sb);
			String result = block.eval(context);
			end(context, sb);

			sb.append(result);
		} catch (TemplateException ex) {
			// @TODO: fazer o quê?
			throw new RuntimeException(ex);
		}
	}
}
