package parser;

import util.LemmingsException;

class StringValue extends Value {
    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public String toStr()
        throws LemmingsException {
        return value;
    }
}