package parser;

import util.LemmingsException;

class NumberValue extends Value {
    private int value;

    public NumberValue(int value) {
        this.value = value;
    }

    public int toNumber()
        throws LemmingsException {
        return value;
    }
}