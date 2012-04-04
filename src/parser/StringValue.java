package parser;

class StringValue extends Value {
    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String toStr()
        throws java.io.IOException {
        return value;
    }
}