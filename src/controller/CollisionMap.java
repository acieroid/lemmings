package controller;

import util.LemmingsException;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class CollisionMap {
    private BufferedImage colImage;
    private int[][] map;
    private int entranceX, entranceY;
    private int exitX, exitY;

    public CollisionMap(String directory)
        throws LemmingsException {
        try {
            /* Load the collison map */
            /* Note that colImage type is TYPE_4BYTE_ABGR */
            colImage = ImageIO.read(new File(directory + "/collision.png"));
            map = new int[getWidth()][getHeight()];
            //colImage.getRGB(0, 0, getWidth(), getHeight(), map, 0, 0);
            /* TODO: get the RGB of a region, which is faster */
            for (int x = 0; x < getWidth(); x++)
                for (int y = 0; y < getHeight(); y++)
                    map[x][y] = (colImage.getRGB(x, y) >> 24);
            /* Load the entrance and exit position */
            BufferedImage objects = ImageIO.read(new File(directory + "/objects.png"));
            boolean entranceFound = false, exitFound = false;
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    if (!entranceFound &&
                        (objects.getRGB(x, y) & 0xFF00) >> 8 == 0xFF) {
                        /* Green: the entrance */
                        entranceX = x;
                        entranceY = y;
                        entranceFound = true;
                    }
                    else if (!exitFound &&
                             (objects.getRGB(x, y) & 0xFF0000) >> 16 == 0xFF) {
                        /* Red: the exit */
                        exitX = x;
                        exitY = y;
                        exitFound = true;
                    }
                }
            }
            if (!entranceFound || !exitFound)
                throw new LemmingsException("controller",
                                            "Can't load map in '" +
                                            directory +
                                            "': no entrance and/or exit");
        } catch (java.io.IOException e) {
            throw new LemmingsException("controller",
                                        "Can't load map in '" +
                                        directory +
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
        /* Only check the border of the rectangle */
        for (i = 0; i < w; i++) {
            if (map[x+i][y] != 0 ||
                map[x+i][y+h-1] != 0)
                return false;
        }
        for (j = 0; j < h; j++) {
            if (map[x][y+j] != 0 ||
                map[x+w][y+j] != 0)
                return false;
        }
        return true;
    }
}