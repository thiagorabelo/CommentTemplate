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

package commenttemplate.context.preprocessor;

import commenttemplate.context.ContextProcessor;
import java.util.LinkedList;

/**
 *
 * Contains de set of ContextPreprocessors that will be applyed to all Templates.
 * 
 * 
 * @author thiago
 */
public class ContextProcessorCache extends LinkedList<ContextProcessor> {
	
	static {
		INSTANCE = new ContextProcessorCache();
	}
	
	/**
	 * Keep a unique instance of this class.
	 */
	private static final ContextProcessorCache INSTANCE;
	
	/**
	 * Return a unique instance o this class.
	 * 
	 * @return {@code PreprocessorCache}
	 */
	public static ContextProcessorCache instance() {
		return INSTANCE;
	}
	
	private ContextProcessorCache() {}
}
