package model;

import org.newdawn.slick.Image;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

class CollisionImage extends ModifiableImage {
    public static Color noCollisionColor = new Color(0, 0, 0, 1);
    public CollisionImage(String ref) throws SlickException {
        super(ref);
    }

    /**
     * Does this image collides with another one ?
     * @param x: the x offset between the two images
     * @param y: the y offset between the two images
     */
    public boolean collidesWith(CollisionImage img, int x, int y) {
        int width = Math.min(getWidth(), img.getWidth());
        int height = Math.min(getHeight(), img.getHeight());
        int i, j;
        Color a, b;

        for (i = 0; i < width; i++) {
            for (j = 0; j < height; j++) {
                a = getColor(x+i, x+j);
                b = img.getColor(i, j);
                
                if (!a.equals(noCollisionColor) &&
                    !b.equals(noCollisionColor)) {
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if a pixel is collision-free
     * @param x: the x position of the pixel
     * @param y: the y position of the pixel
     */
    public boolean isCollisionFree(int x, int y) {
        return getColor(x, y).equals(noCollisionColor);
    }

    /**
     * Remove a pixel from the collision image (set it as collison
     * free)
     */
    public void destroyAt(int x, int y) {
        setColor(x, y, noCollisionColor);
    }
}
