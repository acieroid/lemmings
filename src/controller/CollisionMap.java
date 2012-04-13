package controller;

import util.LemmingsException;
import model.Map;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class CollisionMap {
    private String name;
    private BufferedImage colImage;
    private int[] map;

    private int entranceX, entranceY;
    private int exitX, exitY;

    public CollisionMap(String name, String directory)
        throws LemmingsException {
        this.name = name;
        try {
            /* Load the collison map */
            /* Note that colImage type is TYPE_4BYTE_ABGR */
            colImage = ImageIO.read(new File(directory + "/collision.png"));
            map = new int[getWidth()*getHeight()];
            /* If there are still performance issues with this, maybe
             * we should use getData */
            colImage.getRGB(0, 0, getWidth(), getHeight(), map, 0, getWidth());

            /* Load the entrance and exit position */
            BufferedImage objects = ImageIO.read(new File(directory + "/objects.png"));
            int[] data = new int[getWidth()*getHeight()];
            objects.getRGB(0, 0, getWidth(), getHeight(), data, 0, getWidth());
            boolean entranceFound = false, exitFound = false;
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    if (!entranceFound &&
                        (data[y*getWidth() + x] & 0xFF00) >> 8 == 0xFF) {
                        /* Green: the entrance */
                        entranceX = x;
                        entranceY = y;
                        entranceFound = true;
                    }
                    else if (!exitFound &&
                             (data[y*getWidth() + x] & 0xFF0000) >> 16 == 0xFF) {
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

    public int getEntranceX() {
        return entranceX;
    }

    public int getEntranceY() {
        return entranceY;
    }

    public int getExitX() {
        return exitX;
    }

    public int getExitY() {
        return exitY;
    }

    public boolean isCollisionFree(int x, int y, int w, int h) {
        /* This is the bottleneck function */
        int pixel, i, j;
        /* Only check the border of the rectangle */
        for (i = 0; i < w; i++) {
            if (map[y*getWidth() + x+i] != 0 ||
                map[(y+h-1)*getWidth() + x+i] != 0)
                return false;
        }
        for (j = 0; j < h; j++) {
            if (map[(y+j)*getWidth() + x] != 0 ||
                map[(y+j)*getWidth() + x+w] != 0)
                return false;
        }
        return true;
    }

    /**
     * Return the model.Map corresponding to this Collision Map
     */
    public Map getMap() {
        return new Map(getWidth(), getHeight(), name);
    }

    /**
     * Return the number of lemmings to release for this map
     * @TODO: read this from the definition file
     */
    public int getLemmingsToRelease() {
        return 20;
    }

    /**
     * Return the number of lemmings to save for this map
     * @TODO: read this from the definition file
     */
    public int getLemmingsToSave() {
        return 10;
    }
}