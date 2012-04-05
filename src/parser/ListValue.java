package parser;

import util.LemmingsException;

import java.util.ArrayList;

class ListValue extends Value {
    private ArrayList<Value> values;

    public ListValue() {
        values = new ArrayList<Value>();
    }

    public ArrayList<Value> toList()
        throws LemmingsException {
        return values;
    }

    public void add(Value value)
        throws LemmingsException {
        values.add(value);
    }

    public Value get(int index)
        throws LemmingsException {
        try {
            return values.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new LemmingsException("parser",
                                        "not enough elements in the list: " +
                                        e.getMessage());
        }
    }
}