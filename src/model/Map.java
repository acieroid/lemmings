package model;

import org.newdawn.slick.SlickException;

public class Map extends Collidable {
    public Map(String img, String col)
        throws SlickException {
        super(0, 0, img, col);
    }
}