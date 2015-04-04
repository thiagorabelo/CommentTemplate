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

package commenttemplate.template.tags;

import commenttemplate.template.exceptions.TemplateTagDoesNotExists;
import commenttemplate.template.exceptions.TemplateWithSameNameAlreadyExistsException;
import static commenttemplate.util.Utils.concat;
import java.util.HashMap;

/**
 *
 * @author thiago
 */
public class TagContainer {
	
	static {
		INSTANCE = new TagContainer();
	}

	private static final TagContainer INSTANCE;

	public static TagContainer instance() {
		return INSTANCE;
	}
	
	public final int CUSTOM_TAGS = 0;
	public final int BUILTIN_TAGS = 1;
	
	HashMap<String, TagComponent> []container = new HashMap[] {
		new HashMap<>(),
		new HashMap<>()
	};
	
	public TagContainer() {

	}
	
	protected HashMap<String, TagComponent> builtin() {
		return container[BUILTIN_TAGS];
	}

	protected HashMap<String, TagComponent> custom() {
		return container[CUSTOM_TAGS];
	}
	
	protected synchronized void addBuiltinTag(TagComponent component) {
		if (!builtin().containsKey(component.getName())) {
			builtin().put(component.getName(), component);
		} else {
			String className = builtin().get(component.getName()).getClass().getName();
			throw new TemplateWithSameNameAlreadyExistsException(concat("A built in TemplateTag whith the same name (", component.getName(), ") already exists: ", className));
		}
	}

	public synchronized void addCustomTag(TagComponent component) {
		if (builtin().containsKey(component.getName())) {
			String className = builtin().get(component.getName()).getClass().getName();
			throw new TemplateWithSameNameAlreadyExistsException(concat("A built in TemplateTag whith the same name (", component.getName(), ") already exists: [", className, "]"));
		} else if (custom().containsKey(component.getName())) {
			String className = custom().get(component.getName()).getClass().getName();
			throw new TemplateWithSameNameAlreadyExistsException(concat("A custom TemplateTag whith the same name (", component.getName(), ") already exists: [", className, "]"));
		}

		custom().put(component.getName(), component);
	}

	public TagComponent getByTagName(String tagName) {
		TagComponent component;
		int i = container.length;
		
		while (i-- > 0) {
			if ((component = container[i].get(tagName)) != null) {
				return component;
			}
		}

		throw new TemplateTagDoesNotExists(concat("There is no ", tagName, " registred."));
	}
}
