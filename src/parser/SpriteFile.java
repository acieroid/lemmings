package parser;

import util.LemmingsException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

public class SpriteFile {
    private Value content;
    public SpriteFile(String file)
        throws LemmingsException {
        parse(file);
    }

    private void parse(String file)
        throws LemmingsException {
        try {
            int c;
            BufferedReader input =
                new BufferedReader(new InputStreamReader(new FileInputStream(file)));
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

    private Value parseList(BufferedReader input)
        throws LemmingsException, java.io.IOException {
        int c;
        ListValue value = new ListValue();
        while ((c = input.read()) != -1) {
            if (c == ')')
                return value;
            if (c == '(')
                value.add(parseList(input));
            else if (c >= 'A' && c <= 'z')
                value.add(parseAtom(input, c));
            else if (c >= '0' && c <= '9')
                value.add(parseNumber(input, c));
            else if (c == '"')
                value.add(parseString(input));
        }
        return value;
    }

    private Value parseAtom(BufferedReader input, int first)
        throws LemmingsException, java.io.IOException {
        String atom = "" + (char) first;
        int c;
        while ((c = input.read()) != -1) {
            if ((c >= 'A' && c <= 'z') || c == '-')
                atom += (char) c;
            else
                return new StringValue(atom);
        }
        throw new LemmingsException("parser",
                                    "Caught EOF when parsing atom " +
                                    "(atom was: " + atom + ")");
    }

    private Value parseNumber(BufferedReader input, int first)
        throws LemmingsException, java.io.IOException {
        int number = first - '0';
        int c;
        while ((c = input.read()) != -1) {
            if (c >= '0' && c <= '9')
                number = number*10 + c - '0';
            else
                return new NumberValue(number);
        }
        throw new LemmingsException("parser",
                                    "Caught EOF when parsing number " +
                                    "(number was: " + number + ")");
    }

    private Value parseString(BufferedReader input)
        throws LemmingsException, java.io.IOException {
        String str = "";
        int c;
        while ((c = input.read()) != -1) {
            if (c == '"') {
                input.read();
                return new StringValue(str);
            }
            str += (char) c;
        }
        throw new LemmingsException("parser",
                                    "Caught EOF when parsing string " +
                                    "(string was: \"" + str + "\")");
    }
}