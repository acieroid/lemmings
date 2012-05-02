package model;

import util.LemmingsException;
import view.View;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class Map extends Entity {
    private View view;
    private String name;
    private int entranceX, entranceY;
    private int exitX, exitY;
    int[] collisionData;

    public Map(String name, String directory)
        throws LemmingsException {
        super(0, 0, 0, 0, name);
        try {
            /* Load the collison map */
            /* Note that colImage type is TYPE_4BYTE_ABGR */
            BufferedImage colImage = ImageIO.read(new File(directory + "/collision.png"));
            setWidth(colImage.getWidth());
            setHeight(colImage.getHeight());
            collisionData = new int[getWidth()*getHeight()];
            /* If there are still performance issues with this, maybe
             * we should use getData */
            colImage.getRGB(0, 0, getWidth(), getHeight(),
                            collisionData, 0, getWidth());

            /* Load the entrance and exit position */
            BufferedImage objects = ImageIO.read(new File(directory + "/objects.png"));
            int[] data = new int[getWidth()*getHeight()];
            objects.getRGB(0, 0, getWidth(), getHeight(), data, 0, getWidth());
            entranceX = 0;
            entranceY = 0;
            exitX = 0;
            exitY = 0;
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
                throw new LemmingsException("model",
                                            "Can't load map in '" +
                                            directory +
                                            "': no entrance and/or exit");

        } catch (java.io.IOException e) {
            throw new LemmingsException("model",
                                        "Can't load map in '" +
                                        directory +
                                        "': " + e.getMessage());
        }
    }

    public void setView(View view) {
        this.view = view;
    }

    
    /**
     * Check if a rectangle is collision free
     */
    public boolean isCollisionFree(int x, int y, int w, int h) {
        /* This is the bottleneck function */
        int pixel, i, j;
        /* Only check the border of the rectangle */
        for (i = 0; i < w; i++) {
            if (collisionData[y*getWidth() + x+i] != 0 ||
                collisionData[(y+h-1)*getWidth() + x+i] != 0)
                return false;
        }
        for (j = 0; j < h; j++) {
            if (collisionData[(y+j)*getWidth() + x] != 0 ||
                collisionData[(y+j)*getWidth() + x+w] != 0)
                return false;
        }
        return true;
    }

    /**
     * Delete a rectangle from the collision map
     */
    public void destroy(int x, int y, int w, int h) {
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
                collisionData[(y+j)*getWidth() + x+i] = 0;
        view.destroyed(x, y, w, h);
    }

    /**
     * Destroy a zone from the collision map
     */
    public void destroy(int[] zone, int x, int y, int w, int h) {
        for (int i = 0; i < h; i++)
            for (int j = 0; j < h; j++)
                if (zone[j*w + i] != 0xFFFFFF)
                    collisionData[(y+j)*getWidth() + x+i] = 0;
        view.destroyed(zone, x, y, w, h);
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
    
    /**
     * Return the number of lemmings to release for this map
     * @TODO: read this from the definition file
     */
    public int getLemmingsToRelease() {
        return 20;
    }

    /**
     * Return the number of lemmings to rescue for this map
     * @TODO: read this from the definition file
     */
    public int getLemmingsToRescue() {
        return 10;
    }

}