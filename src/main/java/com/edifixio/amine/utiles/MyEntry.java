package com.edifixio.amine.utiles;

import java.util.Map.Entry;


public class MyEntry<K, V> implements Entry<K, V>{
		
		@Override
	public String toString() {
		// TODO Auto-generated method stub
		return key.toString()+"-----"+value.toString();
	}

		private K key;
		private V value;

		public MyEntry(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			// TODO Auto-generated method stub
			return key;
		}

		public V getValue() {
			// TODO Auto-generated method stub
			return value;
		}

		public V setValue(V value) {
			// TODO Auto-generated method stub
			return this.value=value;
		}
		
	}


