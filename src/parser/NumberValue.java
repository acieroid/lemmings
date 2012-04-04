package parser;

class NumberValue extends Value {
    private int value;

    public NumberValue(int value) {
        this.value = value;
    }

    public int toNumber()
        throws java.io.IOException {
        return value;
    }
}