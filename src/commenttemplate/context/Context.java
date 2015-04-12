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

package commenttemplate.context;


import commenttemplate.util.MyStack;
import commenttemplate.util.MyHashMap;
import commenttemplate.util.retrieve.IterativeRetrieverDataMap;
import commenttemplate.util.retrieve.RetrieveDataMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author thiago
 */
public class Context implements Map<String, Object> {

	private static final RetrieveDataMap<Map<String, Object>> defaultRetrieverDataMap = new IterativeRetrieverDataMap();

	private static Object get(List<MyHashMap<String, Object>> maps, Object key) {
		for (int i = maps.size() - 1; i >= 0; i--) {
			MyHashMap<String, Object> map = maps.get(i);

			Map.Entry<String, Object> e = map.getEntry(key);

			if (e != null) {
				return e.getValue();
			}
		}

		return null;
	}
	
	private static int size(List<MyHashMap<String, Object>> maps) {
		return keySet(maps).size();
	}
	
	private static Set<String> keySet(List<MyHashMap<String, Object>> maps) {
		HashSet<String> keys = new HashSet<>();

		for (MyHashMap<String, Object> map : maps) {
			keys.addAll(map.keySet());
		}

		return keys;
	}
	
	/*------------------------------------------------------------------------*/
	
	/**
	 * A pilha de contextos.
	 */
	private MyStack<MyHashMap<String, Object>> contextStack;
	
	/**
	 * Cache da soma de quantidade de itens dos mapas na pilha de contexto.
	 */
	private int size = 0;
	
	/**
	 * Denota se houve alteração nos mapas para verificar a necessidade de
	 * recalcular o size.
	 */
	private boolean mapChanged = false;

	/**
	 * Classe que vai fazer a busca dentro do mapa.
	 */
	private RetrieveDataMap<Map<String, Object>> retrieverDataMap;
	
	public Context() {
		contextStack = new MyStack<>();
		retrieverDataMap = defaultRetrieverDataMap;
	}

	public Context(RetrieveDataMap retrieverDataMap) {
		contextStack = new MyStack<>();
		this.retrieverDataMap = retrieverDataMap;
	}
	
	public Context(Map<String, Object> params) {
		this();

		if (params instanceof MyHashMap) {
			contextStack.push((MyHashMap<String, Object>)params);
		} else {
			contextStack.push(new MyHashMap<>()).peek().putAll(params);
		}

		size = params.size();
		mapChanged = false;
	}

	public Context(Map<String, Object> params, RetrieveDataMap retrieverDataMap) {
		this(retrieverDataMap);

		if (params instanceof MyHashMap) {
			contextStack.push((MyHashMap<String, Object>)params);
		} else {
			contextStack.push(new MyHashMap<>()).peek().putAll(params);
		}

		size = params.size();
		mapChanged = false;
	}
	
	public Context(Context other) {
		this(other, defaultRetrieverDataMap);
	}

	public Context(Context other, RetrieveDataMap retrieverDataMap) {
		this(retrieverDataMap);
		
		List<MyHashMap<String, Object>> stack = other.contextStack.getList();
		
		for (MyHashMap<String, Object> map : stack) {
			contextStack.push((MyHashMap<String, Object>)map.clone());
		}

		mapChanged = true;
	}
	
	public Context push(Map<String, Object> params) {
		if (params instanceof MyHashMap) {
			contextStack.push((MyHashMap<String, Object>)params);
		} else {
			contextStack.push(new MyHashMap<>()).peek().putAll(params);
		}

		size = size(contextStack.getList());
		mapChanged = false;

		return this;
	}

	public Context push() {
		contextStack.push(new MyHashMap<>());
		
		return this;
	}
	
	public MyHashMap<String, Object> pop() {
		MyHashMap<String, Object> top = contextStack.pop();
		mapChanged = true;

		return top;
	}

	@Override
	public int size() {
		if (mapChanged) {
			size = size(contextStack.getList());
			mapChanged = false;
		}

		return size;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		List<MyHashMap<String, Object>> maps = contextStack.getList();

		for (int i = maps.size() - 1; i >= 0; i--) {
			MyHashMap<String, Object> map = maps.get(i);
			if (map.containsKey(key)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		List<MyHashMap<String, Object>> maps = contextStack.getList();

		for (int i = maps.size() - 1; i >= 0; i--) {
			MyHashMap<String, Object> map = maps.get(i);
			if (map.containsValue(value)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Object get(Object key) {
		return get(contextStack.getList(), key);
	}

	@Override
	public Object put(String key, Object value) {
		Object obj = contextStack.peek().put(key, value);

		if (obj == null) {
			mapChanged = true;
		}

		return obj;
	}

	@Override
	public Object remove(Object key) {
		return contextStack.peek().remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		contextStack.peek().putAll(m);
		mapChanged = true;
	}

	@Override
	public void clear() {
		contextStack.getList().clear();
		size = 0;
		mapChanged = false;
	}

	@Override
	public Set<String> keySet() {
		return keySet(contextStack.getList());
	}

	@Override
	public Collection<Object> values() {
		Set<String> keys = keySet(contextStack.getList());
		ArrayList<Object> objs = new ArrayList<>();

		for (String key : keys) {
			objs.add(get(contextStack.getList(), key));
		}

		return objs;
	}

	@Override
	public Set<Map.Entry<String, Object>> entrySet() {
		List<MyHashMap<String, Object>> maps = contextStack.getList();
		MyHashMap<String, Object> plainMap = new MyHashMap<>();
		
		for (MyHashMap<String, Object> map : maps) {
			plainMap.putAll(map);
		}

		return plainMap.entrySet();
	}

	public Object getValue(String ...key) {
		return retrieverDataMap.getValue(this, key);
	}
}
