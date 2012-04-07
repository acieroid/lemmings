package controller;

import util.LemmingsException;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class CollisionMap {
    private BufferedImage colImage;

    public CollisionMap(String collisionImagePath)
        throws LemmingsException {
        try {
            /* Note that colImage type is TYPE_4BYTE_ABGR */
            colImage = ImageIO.read(new File(collisionImagePath));
        } catch (java.io.IOException e) {
            throw new LemmingsException("controller",
                                        "Can't load image '" +
                                        collisionImagePath +
                                        "': " + e.getMessage());
        }
    }

    public int getWidth() {
        return colImage.getWidth();
    }

    public int getHeight() {
        return colImage.getHeight();
    }

    public boolean isCollisionFree(int x, int y, int w, int h) {
        /* TODO: is there something more efficient ? */
        int[] argb = new int[x*y];
        colImage.getRGB(x, y, w, h, argb, 0, 0);
        for (int pixel : argb) {
            int alpha = pixel >> 24;
            if (alpha != 0) /* alpha seems to have 0 or -1 as value */
                return false;
        }
        return true;
    }
}