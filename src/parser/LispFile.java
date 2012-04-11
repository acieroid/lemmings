package parser;

import util.LemmingsException;

import java.io.PushbackReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

public class LispFile {
    private Value content;
    public LispFile(String file)
        throws LemmingsException {
        parse(file);
    }

    public String getType()
        throws LemmingsException {
        return content.get(0).toStr();
    }

    public String getStringProperty(String name)
        throws LemmingsException {
        return getStringProperty(name, 0);
    }

    public String getStringProperty(String name, int elem)
        throws LemmingsException {
        return getProperty(name).get(elem+1).toStr();
    }

    public String getStringProperty(String name, int elem, String def) {
        try {
            return getStringProperty(name, elem);
        } catch (LemmingsException e) {
            return def;
        }
    }

    public int getNumberProperty(String name)
        throws LemmingsException {
        return getNumberProperty(name, 0);
    }

    public int getNumberProperty(String name, int elem)
        throws LemmingsException {
        return getProperty(name).get(elem+1).toNumber();
    }

    public int getNumberProperty(String name, int elem, int def) {
        try {
            return getNumberProperty(name, elem);
        } catch (LemmingsException e) {
            return def;
        }
    }

    public boolean getBooleanProperty(String name)
        throws LemmingsException {
        return getProperty(name).get(1).toBoolean();
    }

    private Value getProperty(String name)
        throws LemmingsException {
        boolean first = true;
        for (Value v : content.toList()) {
            if (first) {
                first = false;
                continue;
            }

            if (v.get(0).toStr().equals(name)) {
                return v;
            }
        }
        throw new LemmingsException("parser",
                                    "no such property: " +
                                    name);
    }

    private void parse(String file)
        throws LemmingsException {
        try {
            int c;
            PushbackReader input =
                new PushbackReader(new InputStreamReader(new FileInputStream(file)));
            while ((c = input.read()) != -1) {
                if (c == '(')
                    content = parseList(input);
            }
        } catch (java.io.IOException e) {
            throw new LemmingsException("parser",
                                    "can't open the file '" + file + "': " +
                                    e.getMessage());
        }
    }

    private Value parseList(PushbackReader input)
        throws LemmingsException, java.io.IOException {
        int c;
        ListValue value = new ListValue();

        while ((c = input.read()) != -1) {
            if (c == ')')
                return value;
            else if (c == '(')
                value.add(parseList(input));
            else if (c >= 'A' && c <= 'z')
                value.add(parseAtom(input, c));
            else if (c >= '0' && c <= '9')
                value.add(parseNumber(input, c));
            else if (c == '"')
                value.add(parseString(input));
            else if (c == '#')
                value.add(parseBoolean(input));
        }
        return value;
    }

    private Value parseAtom(PushbackReader input, int first)
        throws LemmingsException, java.io.IOException {
        String atom = "" + (char) first;
        int c;
        while ((c = input.read()) != -1) {
            if ((c >= 'A' && c <= 'z') || c == '-') {
                atom += (char) c;
            }
            else {
                input.unread((char) c);
                return new StringValue(atom);
            }
        }
        throw new LemmingsException("parser",
                                    "Caught EOF when parsing atom " +
                                    "(atom was: " + atom + ")");
    }

    private Value parseNumber(PushbackReader input, int first)
        throws LemmingsException, java.io.IOException {
        int number = first - '0';
        int c;
        while ((c = input.read()) != -1) {
            if (c >= '0' && c <= '9') {
                number = number*10 + c - '0';
            }
            else {
                input.unread((char) c);
                return new NumberValue(number);
            }
        }
        throw new LemmingsException("parser",
                                    "Caught EOF when parsing number " +
                                    "(number was: " + number + ")");
    }

    private Value parseString(PushbackReader input)
        throws LemmingsException, java.io.IOException {
        String str = "";
        int c;
        while ((c = input.read()) != -1) {
            if (c == '"') {
                return new StringValue(str);
            }
            str += (char) c;
        }
        throw new LemmingsException("parser",
                                    "Caught EOF when parsing string " +
                                    "(string was: \"" + str + "\")");
    }

    private Value parseBoolean(PushbackReader input)
        throws LemmingsException, java.io.IOException {
        int c = input.read();
        if (c == 't')
            return new BooleanValue(true);
        else if (c == 'f')
            return new BooleanValue(false);
        else if (c == -1)
            throw new LemmingsException("parser",
                                        "Caught EOF when parsing boolean");
        else
            throw new LemmingsException("parser",
                                        "Invalid character after a '#': " +
                                        (char) c + " (a boolean should be #f or #t)");
    }
}