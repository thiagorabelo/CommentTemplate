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
package commenttemplate.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;


/**
 *
 * @author thiago
 * @param <K>
 * @param <V>
 */
// http://stackoverflow.com/questions/2369467/why-are-hash-table-expansions-usually-done-by-doubling-the-size
// http://en.wikipedia.org/wiki/Hash_table
// http://courses.csail.mit.edu/6.006/spring11/rec/rec07.pdf
// http://www.algolist.net/Data_structures/Hash_table/Dynamic_resizing
// http://www.ime.usp.br/~pf/estruturas-de-dados/aulas/st-hash.html
// https://dl.pushbulletusercontent.com/umAI56cMVfTuGqqvSovHCmtJR2oJHoKf/3-hash.pdf
public class MyHashMap<K, V> implements Map<K, V> {
	
	private static int DEFAULT_INITIAL_SIZE = 1 << 4;
	
	private class Node<K, V> implements Map.Entry<K, V> {
		private final K key;
		private final int hash;
		private V value;
		private Node<K, V> next = null;
		
		public Node(K key, V value) {
			this.key = key;
			this.value = value;
			this.hash = 0;
		}
		
		public Node(int hash, K key, V value) {
			this.key = key;
			this.value = value;
			this.hash = hash;
		}
		
		public Node(Node<? extends K, ? extends V> e) {
			this(e.hash, e.key, e.value);
		}


		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			V oldvalue = this.value;
			this.value = value;
			return oldvalue;
		}

		@Override
		public String toString() {
			return key + "=>" + value;
		}
	};

	private float loadFactor = 0.75f;
	private int size = 0;
	private int threshold = 0;
	private int initialSize = DEFAULT_INITIAL_SIZE;	
	private Node<K, V> []table = null;

	
	public MyHashMap() {
		resize();
	}

	public MyHashMap(int initialSize) {
		this.initialSize = initialSize;
		resize();
	}
	
	public MyHashMap(Map<? extends K, ? extends V> map) {
		if (map != null && !map.isEmpty()) {
			putMapEntries(map);
		} else {
			resize();
		}
	}

	public MyHashMap(MyHashMap<? extends K, ? extends V> map) {
		if (map != null && !map.isEmpty()) {
			putMapEntries(map);
		} else {
			resize();
		}
	}
	
	private void putMapEntries(MyHashMap<? extends K, ? extends V> map) {
		resize(map.size());
		
		for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
			Node e = (Node<? extends K, ? extends V>) entry;
			putEntry(e.hash, (K)e.key, (V)e.value);
		}
	}

	private void putMapEntries(Map<? extends K, ? extends V> map) {
		resize(map.size());
		
		for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
			putEntry(hash(entry.getKey()), entry.getKey(), entry.getValue());
		}
	}
	
	private int index(int hash) {
		//return hash % table.length;
		return  (table.length - 1) & hash;
	}
	
	private int hash(Object key) {
		//return key != null ? (key.hashCode() & Integer.MAX_VALUE) : 0;
		int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	}
	
	public Map.Entry<K, V> getEntry(Object key) {
		int hash = hash(key);
		Node<K, V> f = table[index(hash)];

		while (f != null) {
			if (f.hash == hash && ((key == f.key) || ((key != null) && (f.key.equals(key))))) {
				return f;
			}

			f = f.next;
		}

		return null;
	}
	
	public Map.Entry<K, V> getEntryByValue(Object value) {
		Node<K, V> f;
		if (table != null) {
			for (int i = 0, len = table.length; i < len; i++) {
				for (f = table[i]; f != null; f = f.next) {
					if (f.value == value || (value != null && value.equals(f.value))) {
						return f;
					}
				}
			}
		}
		
		return null;
	}
	
	private void resize() {
		resize(0);
	}

//	private void resize(int extraSize) {
//		Node<K, V>[] oldTab = table;
//
//		int newSize = extraSize <= 0
//				? oldTab != null
//						? table.length << 1
//						: initialSize
//				: oldTab != null
//						? (int) ((size + extraSize) / loadFactor)
//						: (int) (extraSize / loadFactor);
//
//		table = new Node[newSize % 2 == 0 ? newSize : newSize + 1];
//
//		if (oldTab != null) {
//			copyTable(table, oldTab);
//		}
//
//		threshold = (int) (table.length * loadFactor);
//	}

	/* 
	 * @TODO:
	 * Aparentemente o método abaixo, que possui código duplicado,
	 * roda mais rápido que o que está comentado logo acima.
	 * Testar isso em outros computadores.
	 */
	private void resize(int extraSize) {
		Node<K, V> []oldTab = table;

		if (extraSize <= 0) {
			if (oldTab != null) {
				int s = table.length;
				table = new Node[s << 1];//s % 2 == 0 ? s + 1 : s];
				copyTable(table, oldTab);
			} else {
				table = new Node[initialSize];
			}
		} else {
			int newSize;
			if (oldTab != null) {
				newSize = (int)((size + extraSize) / loadFactor);
				table = new Node[newSize % 2 == 0 ? newSize : newSize + 1];
				copyTable(table, oldTab);
			} else {
				newSize = (int)(extraSize / loadFactor);
				table = new Node[newSize % 2 == 0 ? newSize : newSize + 1];
			}
		}

		threshold = (int)(table.length * loadFactor);
	}
	
	private void copyTable(Node<K, V> []newTab, Node<K, V> []oldTab) {
		for (int i = 0, len = oldTab.length; i < len && size > 0; i++) {
			if (oldTab[i] != null) {
				Node<K, V> f = oldTab[i];
				while (f != null) {
					putEntry(f, newTab);
					f = f.next;
				}
			}
		}
	}

	private void putEntry(Node<K, V> e, Node<K, V> []table) {
		int s = table.length;
		int index = index(e.hash);
		
		if (table[index] != null) {
			Node<K,V> f = table[index];
			while (f.next != null) {
				f = f.next;
			}
			f.next = new Node(e);
		} else {
			table[index] = new Node(e);
		}
	}
	
	public void print() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0, len = table.length; i < len; i++) {
			sb.append("[").append(i).append("] -> ");
			
			Node<K, V> n = table[i];
			if (n != null) {
				String c = "";
				while (n != null) {
					sb.append(c).append(n.toString());
					c = " -> ";
					n = n.next;
				}
			} else {
				sb.append("null");
			}
			sb.append("\n");
		}
		System.out.println(sb.append("\n----------------------------------------\n"));
	}

	protected V putEntry(int hash, K key, V value) {
		int index;
		Node<K, V> f = table[index = index(hash)];
		Node<K, V> l = null;
		
		if (f == null) {
			table[index] = new Node<K, V>(hash, key, value);

			if (++size > threshold) {
				resize();
			}
//			print();

			return null;
		} else {
			while (f != null) {
				if (f.hash == hash && (f.key == key) || ((key != null) && (f.key.equals(key)))) {
					V old = f.value;
					f.value = value;
//					print();
					return old;
				}
				
				l = f;
				f = f.next;
			}
			
			// l nunca é nulo
			l.next = new Node<K, V>(hash, key, value);
			
			if (++size > threshold) {
				resize();
			}
//			print();

			return null;
		}
	}
	

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return getEntry(key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		Map.Entry<K, V> n = getEntryByValue(value);
		return n != null;
	}

	@Override
	public V get(Object key) {
		Map.Entry<K, V> e = getEntry(key);
		return e != null ? e.getValue() : null;
	}

	@Override
	public V put(K key, V value) {
		return putEntry(hash(key), key, value);
	}

	@Override
	public V remove(Object key) {
		Node<K, V> node;
		return (node = removeEntry(key)) != null ? node.value : null;
	}
	
	private Node<K, V> removeEntry(Object key) {
		for (int i = 0, len = table.length; i < len; i++) {
			Node<K, V> f;

			if ((f = table[i]) != null) {
				int hash;

				if ((hash = hash(key)) == f.hash && (f.key == key || (key != null && key.equals(f.key)))) {
					table[i] = f.next;
					f.next = null;
					size--;
					return f;
				}

				// Nunca é nulo
				f = f.next;
				Node<K, V> p = table[i];

				while (f != null) {
					if (hash == f.hash && (f.key == key || (key != null && key.equals(f.key)))) {
						p.next = f.next;
						f.next = null;
						size--;
						return f;
					}

					f = f.next;
				}
			}
		}

		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		putMapEntries(m);
	}

	@Override
	public void clear() {
		size = 0;
		table = null;
		resize();
	}
	
	private class EntryIterator {

		private Node<K, V> current; // futuramente, usar no remove;
		private Node<K, V> next;
		private int index;

		public EntryIterator() {
			current = next = null;

			next = interruptableCrossTable(new LoopNodes<K, V>() {
				@Override
				public boolean action(Node<K, V> node, int i) {
					if (node != null) {
						index = i;
						return true;
					}
					return false;
				}
			});
		}

		public boolean hasNext() {
			return next != null;
		}

		public Node<K, V> nextEntry() {
			Node<K, V> temp = next;
			
			if (next == null) {
				throw new NoSuchElementException();
			}

			if ((next = next.next) == null) {
				int len = table.length -1;
				while (index < len) {
					if ((next = table[++index]) != null) {
						break;
					}
				}
			}

			return current = temp;
		}

		public void remove() {
			MyHashMap.this.remove(current.key);
		}
	}
	
	private class KeyIterator extends EntryIterator implements Iterator<K> {
		@Override
		public K next() {
			return super.nextEntry().key;
		}
	}
	
	private abstract class LoopNodes<K, V> {
		public abstract boolean action(Node<K, V> node, int index);
	}
	
	private void crossTable(LoopNodes<K, V> action) {
		Node<K, V> f;
		if (table != null) {
			for (int i = 0, len = table.length; i < len; i++) {
				if ((f = table[i]) != null) {
					for (; f != null; f = f.next) {
						action.action(f, i);
					}
				}
			}
		}
	}

	private Node<K, V> interruptableCrossTable(LoopNodes<K, V> action) {
		if (table != null) {
			for (int i = 0, len = table.length; i < len; i++) {
				for (Node<K, V> f = table[i]; f != null; f = f.next) {
					if (action.action(f, i)) {
						return f;
					}
				}
			}
		}
		
		return null;
	}
	
	private class KeySet extends AbstractSet<K> {

		@Override
		public final int size() {
			return size;
		}
        
		@Override
		public final void clear() {
			MyHashMap.this.clear();
		}

		@Override
		public final boolean contains(Object o) {
			return getEntry(o) != null;
		}

		@Override
		public final boolean remove(Object key) {
			return removeEntry(key) != null;
		}

		@Override
		public final Iterator<K> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public final void forEach(Consumer<? super K> action) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public final Spliterator<K> spliterator() {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	@Override
	public Set<K> keySet() {
		return new KeySet();
	}
	
	private class ValueIterator extends EntryIterator implements Iterator<V> {
		@Override
		public V next() {
			return nextEntry().value;
		}
	}
	
	private class ValuesCollection extends AbstractCollection<V> {
		@Override
		public final int size() {
			return size;
		}

		@Override
		public final void clear() {
			MyHashMap.this.clear();
		}

		@Override
		public final Iterator<V> iterator() {
			return new ValueIterator();
		}

		@Override
		public final boolean contains(Object o) {
			return getEntry(o) != null;
		}

		public final Spliterator<V> spliterator() {
			throw new UnsupportedOperationException("Not supported yet.");
		}
		public final void forEach(Consumer<? super V> action) {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	@Override
	public Collection<V> values() {
		return new ValuesCollection();
	}
	
	private class EntryIteratorImpl extends EntryIterator implements Iterator<Map.Entry<K, V>> {
		@Override
		public Map.Entry<K, V> next() {
			return super.nextEntry();
		}
	}
	
	private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

		@Override
		public final int size() {
			return size;
		}
        
		@Override
		public final void clear() {
			MyHashMap.this.clear();
		}

		@Override
		public final boolean contains(Object o) {
			return getEntry(o) != null;
		}

		@Override
		public final boolean remove(Object key) {
			return removeEntry(key) != null;
		}
		
		@Override
		public final Iterator<Map.Entry<K, V>> iterator() {
			return new EntryIteratorImpl();
		}

		public final void forEach(Consumer<? super Map.Entry<K, V>> action) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public final Spliterator<Map.Entry<K, V>> spliterator() {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return new EntrySet();
	}
	
	@Override
	public Object clone() {
		return new MyHashMap<>(this);
	}
}
