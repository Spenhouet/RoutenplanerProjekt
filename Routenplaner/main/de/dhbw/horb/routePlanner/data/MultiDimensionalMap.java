package de.dhbw.horb.routePlanner.data;

import java.util.HashMap;
import java.util.Map;

class MultiDimensionalMap<KeyType, ValueType> {

    public static void main(String args[]) {
	MultiDimensionalMap<Number, Number> m = new MultiDimensionalMap<Number, Number>(3);

	// put(Value, KeyElement, KeyElement, ...)
	m.put(123, 1, 2, 3);
	m.put(456, 4, 5, 6);
	m.put(789, 7, 8, 9);
	m.put(111, 1, 0, 0);
	m.put(222, 1, 0); // Only valid if DIM_CHECK==false
	m.put(333, 1); // Only valid if DIM_CHECK==false
	m.put(444, 0, 0, 1);

	System.out.println("get 123: " + m.get(1, 2, 3));
	System.out.println("get 456: " + m.get(4, 5, 6));
	System.out.println("get 789: " + m.get(7, 8, 9));
	System.out.println("size " + m.size());
	System.out.println("remove 456: " + m.remove(4, 5, 6));
	System.out.println("size " + m.size());
	System.out.println("containsKey 456: " + m.containsKey(4, 5, 6));
	System.out.println("containsKey 789: " + m.containsKey(7, 8, 9));
	System.out.println("containsValue 456: " + m.containsValue(456));
	System.out.println("containsValue 789: " + m.containsValue(789));
	System.out.println("get 100: " + m.get(1, 0, 0));
	System.out.println("get 1: " + m.get(1)); // Only valid if DIM_CHECK==false
	System.out.println("get 001: " + m.get(0, 0, 1));

    }

    private static class KeyTuple<KeyType> {
	private KeyType keyElements[];

	public KeyTuple(KeyType... keyElements) {
	    this.keyElements = keyElements;
	}

	public int hashCode() {
	    int hashCode = 0;
	    for (int i = 0; i < keyElements.length; i++) {
		hashCode ^= keyElements[i].hashCode();
	    }
	    return hashCode;
	}

	public boolean equals(Object object) {
	    if (!(object instanceof KeyTuple)) {
		return false;
	    }
	    KeyTuple other = (KeyTuple) object;
	    for (int i = 0; i < keyElements.length; i++) {
		if (!keyElements[i].equals(other.keyElements[i])) {
		    return false;
		}
	    }
	    return true;
	}
    }

    private static final boolean DIM_CHECK = false;

    private Map<KeyTuple, ValueType> map = new HashMap<KeyTuple, ValueType>();

    private int dimension;

    public MultiDimensionalMap(int dimension) {
	this.dimension = dimension;
    }

    private void dimCheck(KeyType... keyElements) {
	if (keyElements == null) {
	    throw new IllegalArgumentException("Dimension must be " + dimension);
	}
	if (keyElements.length != dimension) {
	    throw new IllegalArgumentException("Dimension must be " + dimension + " but is " + keyElements.length);
	}
    }

    public void put(ValueType value, KeyType... keyElements) {
	if (DIM_CHECK) {
	    dimCheck(keyElements);
	}
	map.put(new KeyTuple<KeyType>(keyElements), value);
    }

    public ValueType get(KeyType... keyElements) {
	if (DIM_CHECK) {
	    dimCheck(keyElements);
	}
	return map.get(new KeyTuple<KeyType>(keyElements));
    }

    public ValueType remove(KeyType... keyElements) {
	if (DIM_CHECK) {
	    dimCheck(keyElements);
	}
	return map.remove(new KeyTuple<KeyType>(keyElements));
    }

    public boolean containsKey(KeyType... keyElements) {
	if (DIM_CHECK) {
	    dimCheck(keyElements);
	}
	return map.containsKey(new KeyTuple<KeyType>(keyElements));
    }

    public boolean containsValue(ValueType value) {
	return map.containsValue(value);
    }

    public int size() {
	return map.size();
    }

    public void clear() {
	map.clear();
    }

}
