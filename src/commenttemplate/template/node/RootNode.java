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
package commenttemplate.template.node;

import commenttemplate.context.Context;
import commenttemplate.context.ContextProcessor;
import commenttemplate.context.processor.ContextProcessorCache;
import commenttemplate.template.writer.TemplateWriter;
import commenttemplate.template.writer.Writer;
import java.util.Map;

/**
 *
 * @author thiago
 */
public class RootNode extends NodeImpl {

	private int initialBufferSize;

	public RootNode() {
		initialBufferSize = 128;
	}

	public RootNode(int initialBufferSize) {
		this.initialBufferSize = initialBufferSize;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		toString(sb);

		return sb.toString();
	}

	@Override
	public void toString(StringBuilder sb) {
		NodeImpl []nodeList = getNodeList();

		for (NodeImpl t : nodeList) {
			t.toString(sb);
		}
	}

	public String eval(Map<String, Object> params) {
		TemplateWriter sb = new TemplateWriter(initialBufferSize);

		if (params instanceof Context) {
			eval((Context)params, sb);
		} else {
			eval(params, sb);
		}

		return sb.toString();
	}

	public void eval(Map<String, Object> params, Writer sb) {
		Context context = new Context(params);
		eval(context, sb);
	}

	@Override
	public void eval(Context context, Writer sb) {
		
		for (ContextProcessor p : ContextProcessorCache.instance()) {
			p.before(context);
		}

		NodeImpl []list = getNodeList();
		
		for (int i = 0, len = list.length; i < len; i++) {
			list[i].eval(context, sb);
		}

		for (ContextProcessor p : ContextProcessorCache.instance()) {
			p.after(sb, context);
		}
	}
}
