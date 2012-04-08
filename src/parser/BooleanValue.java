package parser;

import util.LemmingsException;

class BooleanValue extends Value {
    private boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    public String toString() {
        return (value ? "#t" : "#f");
    }

    public boolean toBoolean()
        throws LemmingsException {
        return value;
    }
}
