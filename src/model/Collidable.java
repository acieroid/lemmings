package model;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;

public class Collidable extends ModifiableImage {
    private int x;
    private int y;
    private CollisionImage colImage;

    public Collidable(int x, int y, String imgPath, String colPath)
        throws SlickException {
        super(imgPath);
        this.x = x;
        this.y = y;
        this.colImage = new CollisionImage(colPath);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CollisionImage getCollisionImage() {
        return colImage;
    }

    /**
     * Does this object collides with another one ?
     * @param col: the other object
     */
    public boolean collidesWith(Collidable col) {
        return getCollisionImage().collidesWith(col.getCollisionImage(),
                                                col.getX() - getX(),
                                                col.getY() - getY());
    }

    /**
     * Is the pixel at (x, y) collision free ?
     */
    public boolean isCollisionFree(int x, int y) {
        return getCollisionImage().isCollisionFree(x, y);
    }

    /**
     * Destroy the pixel at the given position.
     * It will be destroyed from the collision image and from the image
     */
    public void destroyAt(int x, int y) {
        //setColor(x, y, new Color(0, 0, 0, 1));
        getCollisionImage().destroyAt(x, y);
    }
}