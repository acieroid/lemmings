package parser;

import java.util.ArrayList;

class ListValue extends Value {
    private ArrayList<Value> values;

    public ListValue() {
        values = new ArrayList<Value>();
    }

    public ArrayList<Value> toList()
        throws java.io.IOException {
        return values;
    }

    public void add(Value value)
        throws java.io.IOException {
        values.add(value);
    }

    public Value get(int index)
        throws java.io.IOException {
        try {
            return values.get(index);
        } catch (Exception e) {
            throw new java.io.IOException("Not enough elements in the list: " +
                                      e.toString());
        }
    }
}