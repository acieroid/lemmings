package model;

import util.LemmingsException;
import parser.LispFile;

public class Character {
    private int x;
    private int y;
    private int width;
    private int height;
    private LispFile definition;

    /* TODO: strategy pattern for behavior */
    public Character(int x, int y, String file)
        throws LemmingsException {
        definition = new LispFile(file);
        this.x = x;
        this.y = y;
        this.width = definition.getNumberProperty("size", 0);
        this.height = definition.getNumberProperty("size", 1);
    }

    /* TODO: some rect/drawable class to avoid redefining this */
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}