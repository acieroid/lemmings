package model;

import util.LemmingsException;

public class Character extends Collidable {
    public Character(int x, int y, String col)
        throws LemmingsException {
        super(x, y, col);
    }
}