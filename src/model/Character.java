package model;

import org.newdawn.slick.SlickException;

public class Character extends Collidable {
    public Character(int x, int y, String img, String col)
        throws SlickException {
        super(x, y, img, col);
    }
}