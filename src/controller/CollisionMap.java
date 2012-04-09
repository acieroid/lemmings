package controller;

import util.LemmingsException;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class CollisionMap {
    private BufferedImage colImage;
    private int[][] map;

    public CollisionMap(String collisionImagePath)
        throws LemmingsException {
        try {
            /* Note that colImage type is TYPE_4BYTE_ABGR */
            colImage = ImageIO.read(new File(collisionImagePath));
            map = new int[getWidth()][getHeight()];
            //colImage.getRGB(0, 0, getWidth(), getHeight(), map, 0, 0);
            /* TODO: get the RGB of a region, which is faster */
            for (int x = 0; x < getWidth(); x++)
                for (int y = 0; y < getHeight(); y++)
                    map[x][y] = (colImage.getRGB(x, y) >> 24);
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
        /* This is the bottleneck function */
        int pixel, i, j;
        for (i = 0; i < w; i++) {
            for (j = 0; j < h; j++) {
                if (map[x+i][y+j] != 0)
                    /* alpha seems to have 0 or -1 as value */
                    return false;
            }
        }
        return true;
    }
}