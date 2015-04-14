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

import commenttemplate.context.ContextPreprocessor;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author thiago
 */
public class PreprocessorCache extends LinkedList<ContextPreprocessor> {
	
	static {
		INSTANCE = new PreprocessorCache();
	}
	
	private static final PreprocessorCache INSTANCE;
	
	public static PreprocessorCache instance() {
		return INSTANCE;
	}
	
	private PreprocessorCache() {}
	private PreprocessorCache(Collection<? extends ContextPreprocessor> c) {
        super(c);
    }
}
