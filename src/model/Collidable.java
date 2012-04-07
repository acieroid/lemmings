package model;

import util.LemmingsException;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class Collidable {
    private int x;
    private int y;
    private BufferedImage colImage;

    public Collidable(int x, int y, String colPath)
        throws LemmingsException {
        this.x = x;
        this.y = y;
        try {
            /* Note that colImage type is TYPE_4BYTE_ABGR */
            colImage = ImageIO.read(new File(colPath));
        } catch (java.io.IOException e) {
            throw new LemmingsException("model/rm",
                                        "Can't load image '" + colPath + "': " +
                                        e.getMessage());
        }
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