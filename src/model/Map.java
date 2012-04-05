package model;

import util.LemmingsException;

public class Map extends Collidable {
    public Map(String col)
        throws LemmingsException {
        super(0, 0, col);
    }
}