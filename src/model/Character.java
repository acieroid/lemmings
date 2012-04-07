package model;

import util.LemmingsException;
import parser.LispFile;

public class Character {
    private int x;
    private int y;
    private int width;
    private int height;
    private LispFile definition;
    private CharacterBehavior behavior;

    /* TODO: strategy pattern for behavior */
    public Character(int x, int y,
                     CharacterBehavior behavior, String file)
        throws LemmingsException {
        this.behavior = behavior;
        definition = new LispFile(file);
        this.x = x;
        this.y = y;
        this.width = definition.getNumberProperty("size", 0);
        this.height = definition.getNumberProperty("size", 1);
    }

    public void update(Map map) {
        behavior.update(this, map);
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

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}