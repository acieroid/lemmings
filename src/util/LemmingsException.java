package util;

public class LemmingsException extends Exception {
    public LemmingsException(String module, String message) {
        super(module + ": " + message);
    }
}
