package parser;

import util.LemmingsException;

import java.util.ArrayList;

class Value {
    Value() {
    }
    public int toNumber()
        throws LemmingsException {
        throw new LemmingsException("parser", "value is not a number");
    }

    public String toStr()
        throws LemmingsException {
        throw new LemmingsException("parser", "value is not a string");
    }

    public ArrayList<Value> toList()
        throws LemmingsException {
        throw new LemmingsException("parser", "value is not a list");
    }

    public void add(Value value)
        throws LemmingsException {
        throw new LemmingsException("parser", "value is not a list");
    }

    public Value get(int index)
        throws LemmingsException {
        throw new LemmingsException("parser", "value is not a list");
    }
}