package parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

public class SpriteFile {
    private Value content;
    public SpriteFile(String file) {
        parse(file);
    }

    private void parse(String file) {
        try {
            int c;
            BufferedReader input =
                new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((c = input.read()) != -1) {
                if (c == '(')
                    content = parseList(input);
            }
            
        } catch (Exception e) {
            System.out.println("Error when parsing the file '" + file + "': " +
                               e.toString());
        }
    }

    private Value parseList(BufferedReader input)
        throws java.io.IOException {
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
        throws java.io.IOException {
        String atom = "" + (char) first;
        int c;
        while ((c = input.read()) != -1) {
            if ((c >= 'A' && c <= 'z') || c == '-')
                atom += (char) c;
            else
                return new StringValue(atom);
        }
        throw new java.io.IOException("EOF too early");
    }

    private Value parseNumber(BufferedReader input, int first)
        throws java.io.IOException {
        int number = first - '0';
        int c;
        while ((c = input.read()) != -1) {
            if (c >= '0' && c <= '9')
                number = number*10 + c - '0';
            else
                return new NumberValue(number);
        }
        throw new java.io.IOException("EOF too early");
    }

    private Value parseString(BufferedReader input)
        throws java.io.IOException {
        String str = "";
        int c;
        while ((c = input.read()) != -1) {
            if (c == '"') {
                input.read();
                return new StringValue(str);
            }
            str += (char) c;
        }
        throw new java.io.IOException("EOF too early");
    }
}