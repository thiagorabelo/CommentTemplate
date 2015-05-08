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
import java.util.Map;
import java.util.Set;

/**
 * Do the mapping for all names do be merged to the Template.
 * 
 * The template engine uses the data stored in context to get the data to operate
 * or replace the names on template.
 * 
 * {@code Context} implements {@code Map<String, Object>}, but under de hood
 * contains a stack of maps. This feature allow to create scopes. If a name is not found
 * in a scope, it will be searched in the deepest scopes.
 * 
 * @author thiago
 */
public class Context implements Map<String, Object> {

	/**
	 * The context is also responsible for returning values through the chain
	 * of object properties, so this is the default {@code RetrieverDataMap} that
	 * do this job.
	 * 
	 * Names like {@code foo.propA.propB} are computed by this object.
	 * 
	 * The sequence of steps are:
	 * <ol>
	 *	<li>Get {@code foo} object from Context</li>
	 *	<li>
	 *		Get the {@code propA} from {@code foo} through introspection
	 *		<ol>
	 *			<li>First try call {@code getPropA()}</li>
	 *			<li>Second try call {@code isPropA()}</li>
	 *			<li>Third try call {@code hasPropA()}</li>
	 *			<li>Fourth try call {@code propA()}</li>
	 *			<li>Fifth try get the field {@code propA}</li>
	 *			<li>Else return {@code null}</li>
	 *		</ol>
	 *	</li>
	 *	<li>The same steps are used to get {@code propB}</li>
	 * </ol>
	 * 
	 * @see commenttemplate.util.retrieve.IterativeRetrieverDataMap
	 * @see commenttemplate.util.retrieve.RetrieveDataMap
	 */
	private static final RetrieveDataMap<Map<String, Object>> defaultRetrieverDataMap = new IterativeRetrieverDataMap();

	/**
	 * Returns the first reference of an objetc found on the stack of scopes.
	 * 
	 * Search through the satck of maps until find an ocurrence otherwise
	 * return {@code null}.
	 * 
	 * @param maps A stack that represents the each scope.
	 * @param key The name that will be searched in the stack.
	 * @return The first object found in which the key indexes to. If
	 * none is located, {@code null} is returned.
	 * 
	 * @see commenttemplate.util.MyStack
	 * @see commenttemplate.util.MyHashMap
	 */
	private static Object get(MyStack<MyHashMap<String, Object>> maps, Object key) {
		MyStack.Node<MyHashMap<String, Object>> node;

		for (node = maps.getTopNode(); node != null; node = node.getAncestral()) {
			Map.Entry<String, Object> e;

			if ((e = node.getItem().getEntry(key)) != null) {
				return e.getValue();
			}
		}

		return null;
	}
	
	/**
	 * Compute the size of stack context.
	 * 
	 * @param maps The stack of map
	 * @return The size of stack context
	 */
	private static int size(MyStack<MyHashMap<String, Object>> maps) {
		return keySet(maps).size();
	}
	
	/**
	 * Compute the size of stack context. The repeated names through stack will
	 * be counted how only one.
	 * 
	 * @param stack The stack of map
	 * @return The size of stack context
	 */
	private static Set<String> keySet(MyStack<MyHashMap<String, Object>> stack) {
		HashSet<String> keys = new HashSet<>();

		for (MyHashMap<String, Object> map : stack) {
			keys.addAll(map.keySet());
		}

		return keys;
	}
	
	/*------------------------------------------------------------------------*/
	
	/**
	 * The stack of maps that represents the scopes.
	 */
	private MyStack<MyHashMap<String, Object>> contextStack;
	
	/**
	 * The size cache.
	 */
	private int size = 0;
	
	/**
	 * Denotes if had change on maps to verify the necessity of recompute the
	 * size.
	 */
	private boolean mapChanged = false;

	/**
	 * Instace of {@code RetieverDataMap} that will compute the values of chain
	 * properties of an object.
	 * 
	 * In exemple, will compute the result of the name {@code foo.propA.propB}.
	 * 
	 * @see commenttemplate.util.retrieve.RetrieveDataMap
	 */
	private RetrieveDataMap<Map<String, Object>> retrieverDataMap;
	
	/**
	 * Create a default instance of {@code Context} with one scope and the default
	 * {@code RetrivierDataMap}.
	 * 
	 * @see commenttemplate.util.retrieve.IterativeRetrieverDataMap
	 */
	public Context() {
		contextStack = new MyStack<>();
		contextStack.push(new MyHashMap<>());
		retrieverDataMap = defaultRetrieverDataMap;
	}

	/**
	 * Create a {@code Context} with onde scope with a specified
	 * {@code RetrivieDataMap}.
	 * 
	 * @param retrieverDataMap An instance of {@code RetrieveDataMap}. A object
	 * that do the job of compute de value of cheined properties, like
	 * {@code foo.propA.propB}.
	 * 
	 * @see commenttemplate.util.retrieve.RetrieveDataMap
	 * @see commenttemplate.util.MyHashMap
	 */
	public Context(RetrieveDataMap retrieverDataMap) {
		contextStack = new MyStack<>();
		contextStack.push(new MyHashMap<>());
		this.retrieverDataMap = retrieverDataMap;
	}
	
	/**
	 * Create a {@code Context} based on {@code Map} passed as parameter.
	 * 
	 * @param params If params is instance of {@code MyHashMap}, then it is
	 * <b>pushed</b> to the stack map. Otherwise the content is <b>copied</b> to
	 * the top of the stack.
	 * 
	 * @see commenttemplate.util.MyStack
	 * @see commenttemplate.util.MyHashMap
	 */
	public Context(Map<String, Object> params) {
		contextStack = new MyStack<>();
		retrieverDataMap = defaultRetrieverDataMap;

		if (params instanceof MyHashMap) {
			contextStack.push((MyHashMap<String, Object>)params);
		} else {
			contextStack.push(new MyHashMap<>()).peek().putAll(params);
		}

		size = params.size();
		mapChanged = false;
	}

	/**
	 * Create a {@code Context} based on {@code Map} and a
	 * {@code RetrieveDataMap} passed as parameter.
	 * 
	 * @param params If params is instance of {@code MyHashMap}, then it is
	 * <b>pushed</b> to the stack map. Otherwise the content is <b>copied</b> to
	 * the top of the stack.
	 * 
	 * @param retrieverDataMap An instance of {@code RetrieveDataMap}. A object
	 * that do the job of compute de value of cheined properties, like
	 * {@code foo.propA.propB}.
	 * 
	 * @see commenttemplate.util.MyHashMap
	 * @see commenttemplate.util.MyStack
	 * @see commenttemplate.util.retrieve.RetrieveDataMap
	 */
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
	
	/**
	 * Create a {@code Context} based on another {@code Context} passed as
	 * parameter and the {@code defaultRitrieverDataMap}.
	 * 
	 * @param other {@code Context} to be copied.
	 */
	public Context(Context other) {
		this(other, defaultRetrieverDataMap);
	}

	/**
	 * Create a {@code Context} based on another {@code Context} and the
	 * {@code RetrieveDataMap} passed as parameter.
	 * 
	 * @param other {@code Context} to be copied.
	 * 
	 * @param retrieverDataMap An instance of {@code RetrieveDataMap}. A object
	 * that do the job of compute de value of cheined properties, like
	 * {@code foo.propA.propB}.
	 * 
	 * @see commenttemplate.util.MyHashMap
	 * @see commenttemplate.util.MyStack
	 * @see commenttemplate.util.retrieve.RetrieveDataMap
	 */
	public Context(Context other, RetrieveDataMap retrieverDataMap) {
		this(retrieverDataMap);
		
		for (MyHashMap<String, Object> map : other.contextStack) {
			contextStack.push((MyHashMap<String, Object>)map.clone());
		}

		mapChanged = true;
	}
	
	/**
	 * Create a new level on {@code Context} allowing work on isolated scope.
	 * 
	 * @param params A other {@code Map} of names to put on new scope. If is
	 * instance of {@code MyHashMap} is pushed directly as new scope otherwise
	 * is copied to the new scope.
	 * 
	 * @return {@code this} {@code Context}
	 * 
	 * @see commenttemplate.util.MyHashMap
	 * @see commenttemplate.util.MyStack
	 */
	public Context push(Map<String, Object> params) {
		if (params instanceof MyHashMap) {
			contextStack.push((MyHashMap<String, Object>)params);
		} else {
			contextStack.push(new MyHashMap<>()).peek().putAll(params);
		}

		size = size(contextStack);
		mapChanged = false;

		return this;
	}

	/**
	 * Create a new level on {@code Context} allowing work on isolated scope.
	 * 
	 * @return {@code this} {@code Context}
	 * 
	 * @see commenttemplate.util.MyHashMap
	 * @see commenttemplate.util.MyStack
	 */
	public Context push() {
		contextStack.push(new MyHashMap<>());
		
		return this;
	}
	
	/**
	 * Removes the current scope and return it.
	 * 
	 * @return The top scope od stack.
	 */
	public MyHashMap<String, Object> pop() {
		MyHashMap<String, Object> top = contextStack.pop();
		mapChanged = true;

		return top;
	}

	/**
	 * Compute the size of stack context.
	 * 
	 * @return The size of stack context
	 */
	@Override
	public int size() {
		if (mapChanged) {
			size = size(contextStack);
			mapChanged = false;
		}

		return size;
	}

	/**
	 * Determine if {@code Context} is empty.
	 * 
	 * @return {@code true} if the size is 0
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Search on the stack looking for a name that matches to {@code key}.
	 * 
	 * @param key A name to search for.
	 * @return {@code true} if a match is found
	 */
	@Override
	public boolean containsKey(Object key) {

		for (MyHashMap<String, Object> map : contextStack) {
			if (map.containsKey(key)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Search on the stack looking for a object that matches to {@code value}.
	 * 
	 * @param value A value to search for.
	 * @return {@code true} if a match is found
	 */
	@Override
	public boolean containsValue(Object value) {

		for (MyHashMap<String, Object> map : contextStack) {
			if (map.containsValue(value)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the first reference of an objetc found on the stack of scopes.
	 * 
	 * @param key The name that will be searched in the stack.
	 * @return The first object found in which the key indexes to. If
	 * none is located, {@code null} is returned.
	 * 
	 * @see commenttemplate.util.MyStack
	 * @see commenttemplate.util.MyHashMap
	 */
	@Override
	public Object get(Object key) {
		return get(contextStack, key);
	}

	/**
	 * Put the {@code value} in the current scope with the following {@code key}.
	 * 
	 * @param key The "index" to the {@code value}
	 * @param value The object to be stored
	 * @return If had an object previously with the same {@code key} replate with
	 * {@code value} and return the old value.
	 */
	@Override
	public Object put(String key, Object value) {
		Object obj = contextStack.peek().put(key.intern(), value);

		if (obj == null) {
			mapChanged = true;
		}

		return obj;
	}

	/**
	 * Remove and return the object in the current scope matched by informed
	 * key {@code key}.
	 * 
	 * @param key The key to look for
	 * @return The removed object.
	 */
	@Override
	public Object remove(Object key) {
		return contextStack.peek().remove(key);
	}

	/**
	 * Copy all elements in the {@code map} to the current scop.
	 * 
	 * @param m The map to be copied
	 */
	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		contextStack.peek().putAll(m);
		mapChanged = true;
	}

	/**
	 * Remove all elements of all scopes.
	 */
	@Override
	public void clear() {
		contextStack.clear();
		size = 0;
		mapChanged = false;
	}

	/**
	 * Return a set of key of all elements.
	 * 
	 * @return A set of key of all elements
	 * 
	 * @see java.util.Set
	 */
	@Override
	public Set<String> keySet() {
		return keySet(contextStack);
	}

	/**
	 * Return a {@code Collection} of all elementos of all scope.
	 * @return A {@code Collection} of all elementos of all scope.
	 * 
	 * @see java.util.Collection
	 */
	@Override
	public Collection<Object> values() {
		Set<String> keys = keySet(contextStack);
		ArrayList<Object> objs = new ArrayList<>();

		for (String key : keys) {
			objs.add(get(contextStack, key));
		}

		return objs;
	}

	/**
	 * Return a {@code Set} of the {@code Map.Entry}.
	 * 
	 * @return A {@code Set} of the {@code Map.Entry}
	 */
	@Override
	public Set<Map.Entry<String, Object>> entrySet() {
		MyHashMap<String, Object> plainMap = new MyHashMap<>();
		ArrayList<Map<String, Object>> temp = new ArrayList<>();

		for (MyHashMap<String, Object> map : contextStack) {
			temp.add(map);
		}

		for (int i = temp.size(); i-- > 0;) {
			Map<String, Object> map = temp.get(i);
			plainMap.putAll(map);
		}

		return plainMap.entrySet();
	}

	/**
	 * The context is also responsible for returning values through the chain
	 * of object properties, so this is the default {@code RetrieverDataMap} that
	 * do this job.
	 * 
	 * Names like {@code foo.propA.propB} are computed by this method.
	 * 
	 * @param key Array containing the list of names (["foo", "propA", "propB"])
	 * to be computed.
	 * 
	 * @return The computed object
	 */
	public Object getValue(String ...key) {
		return retrieverDataMap.getValue(this, key);
	}
}
