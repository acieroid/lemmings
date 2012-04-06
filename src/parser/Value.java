package parser;

import util.LemmingsException;

import java.util.ArrayList;

class Value {
    Value() {
    }

    public String toString() {
        return "abstract value";
    }

    public int toNumber()
        throws LemmingsException {
        throw new LemmingsException("parser", "value is not a number: " +
                                    toString());
    }

    public String toStr()
        throws LemmingsException {
        throw new LemmingsException("parser", "value is not a string: " +
                                    toString());
    }

    public ArrayList<Value> toList()
        throws LemmingsException {
        throw new LemmingsException("parser", "value is not a list: " +
                                    toString());
    }

    public void add(Value value)
        throws LemmingsException {
        throw new LemmingsException("parser", "value is not a list: " +
                                    toString());
    }

    public Value get(int index)
        throws LemmingsException {
        throw new LemmingsException("parser", "value is not a list: " +
                                    toString());
    }
}