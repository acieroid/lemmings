package parser;

import java.util.ArrayList;

class Value {
    Value() {
    }
    public int toNumber()
        throws java.io.IOException {
        throw new java.io.IOException("Not a number");
    }

    public String toStr()
        throws java.io.IOException {
        throw new java.io.IOException("Not a string");
    }

    public ArrayList<Value> toList()
        throws java.io.IOException {
        throw new java.io.IOException("Not a list");
    }

    public void add(Value value)
        throws java.io.IOException {
        throw new java.io.IOException("Not a list");
    }

    public Value get(int index)
        throws java.io.IOException {
        throw new java.io.IOException("Not a list");
    }
}