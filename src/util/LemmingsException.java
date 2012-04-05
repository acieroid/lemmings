package util;

public class LemmingsException extends Exception {
    public LemmingsException(String module, String message) {
        super("Error in module '" + module + "': " + message);
    }
}
