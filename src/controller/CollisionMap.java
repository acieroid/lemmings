package controller;

import util.LemmingsException;
import model.Map;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class CollisionMap {
    private Map map;

    public CollisionMap(String name, String directory)
        throws LemmingsException {
        try {
            /* Load the collison map */
            /* Note that colImage type is TYPE_4BYTE_ABGR */
            BufferedImage colImage = ImageIO.read(new File(directory + "/collision.png"));
            int width = colImage.getWidth();
            int height = colImage.getHeight();
            int[] colData = new int[width*height];
            /* If there are still performance issues with this, maybe
             * we should use getData */
            colImage.getRGB(0, 0, width, height, colData, 0, width);

            /* Load the entrance and exit position */
            BufferedImage objects = ImageIO.read(new File(directory + "/objects.png"));
            int[] data = new int[width*height];
            objects.getRGB(0, 0, width, height, data, 0, width);
            int entranceX = 0, entranceY = 0, exitX = 0, exitY = 0;
            boolean entranceFound = false, exitFound = false;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (!entranceFound &&
                        (data[y*width + x] & 0xFF00) >> 8 == 0xFF) {
                        /* Green: the entrance */
                        entranceX = x;
                        entranceY = y;
                        entranceFound = true;
                    }
                    else if (!exitFound &&
                             (data[y*width + x] & 0xFF0000) >> 16 == 0xFF) {
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

            map = new Map(width, height, name, colData,
                          entranceX, entranceY, exitX, exitY);
        } catch (java.io.IOException e) {
            throw new LemmingsException("controller",
                                        "Can't load map in '" +
                                        directory +
                                        "': " + e.getMessage());
        }
    }

    /**
     * Check if a rectangle is collision free
     */
    public boolean isCollisionFree(int x, int y, int w, int h) {
        /* This is the bottleneck function */
        int pixel, i, j;
        /* Only check the border of the rectangle */
        for (i = 0; i < w; i++) {
            if (map.getCollisionData()[y*map.getWidth() + x+i] != 0 ||
                map.getCollisionData()[(y+h-1)*map.getWidth() + x+i] != 0)
                return false;
        }
        for (j = 0; j < h; j++) {
            if (map.getCollisionData()[(y+j)*map.getWidth() + x] != 0 ||
                map.getCollisionData()[(y+j)*map.getWidth() + x+w] != 0)
                return false;
        }
        return true;
    }

    /**
     * Return the model.Map corresponding to this Collision Map
     */
    public Map getMap() {
        return map;
    }

    /**
     * Return the number of lemmings to release for this map
     * @TODO: read this from the definition file
     * @TODO: put this in the model.Map
     */
    public int getLemmingsToRelease() {
        return 20;
    }

    /**
     * Return the number of lemmings to save for this map
     * @TODO: read this from the definition file
     * @TODO: put this in the model.Map
     */
    public int getLemmingsToSave() {
        return 10;
    }
}