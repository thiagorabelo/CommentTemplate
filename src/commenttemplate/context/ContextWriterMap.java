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

import commenttemplate.template.writer.TemplateWriter;
import commenttemplate.template.writer.Writer;
import java.util.HashMap;

/**
 * Is a {@code Context} used to render a template, but is used too to store a {@code Writer} indexed
 * by name or to get a {@code Writer} by a name, depending if is in mode {@code STORE} or in mode
 * {@code RENDER}.
 * 
 * Util when a <b>Tag</b> wants to render the his own output and store this value in a separated
 * {@code Writer} (<i>STORE</i>). And than Another <b>Tag</b> wants to get the output from the
 * previous <b>Tag</b>, by passing a name (<i>RENDER</i>).
 * 
 * @author thiago
 */
public class ContextWriterMap extends Context {
	
	/**
	 * Determines the mode of operation of the {@code Context}. If it is in mode
	 * {@code STORE} or in mode {@code RENDER}.
	 */
	public static enum Mode {
		
		STORE {
			@Override
			public Writer getWriter(ContextWriterMap context, String blockName) {
				Writer w;

				if ((w = context.writerMap.get(blockName)) == null) {
					w = new TemplateWriter();
					context.writerMap.put(blockName, w);
				}

				return w;
			}
		},
		RENDER {
			@Override
			public Writer getWriter(ContextWriterMap context, String blockName) {
				return context.writerMap.get(blockName);
			}
		};

		public Writer getWriter(ContextWriterMap context, String blockName) {
			throw new UnsupportedOperationException("Not supported. This method must be overridden.");
		}
	};


	private final HashMap<String, Writer> writerMap = new HashMap<>();
	private Mode mode = Mode.RENDER;
	
	public ContextWriterMap(Context context) {
		super(context);
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public Writer getWriter(String blockName) {
		return mode.getWriter(this, blockName);
	}
}
