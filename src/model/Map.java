package model;

import util.LemmingsException;
import view.View;

public class Map extends Entity {
    private View view;
    private String name;
    private int width, height;
    private int entranceX, entranceY;
    private int exitX, exitY;
    int[] collisionData;

    public Map(int width, int height, String name,
               int collisionData[], int entranceX, int entranceY,
               int exitX, int exitY) {
        super(0, 0, width, height, name);
        this.name = name;
        this.width = width;
        this.height = height;
        this.collisionData = collisionData;
        this.entranceX = entranceX;
        this.entranceY = entranceY;
        this.exitX = exitX;
        this.exitY = exitY;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int[] getCollisionData() {
        return collisionData;
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
                if (zone[j*w + i] != 0)
                    collisionData[(y+j)*getWidth() + x+i] = 0;
        view.destroyed(zone, x, y, w, h);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
}