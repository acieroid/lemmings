package model;

import java.io.Serializable;

public class Character extends Entity implements Serializable {
    public static int LEFT = -1;
    public static int DONT_MOVE = 0;
    public static int RIGHT = 1;

    public static int CHANGE_NONE = 0;
    public static int CHANGE_DIRECTION = 1;
    public static int CHANGE_FALLING = 2;
    public static int CHANGE_BEHAVIOR = 3;
    public static int CHANGE_DELETED = 4;

    private int direction;
    private boolean falling;

    private Behavior behavior;

    public Character(int x, int y, int width, int height, String name) {
        super(x, y, width, height, name);
        direction = RIGHT;
        falling = false;
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void changeDirection() {
        setDirection(getDirection() * -1);
        getModel().notifyCharacterChanged(this, CHANGE_DIRECTION);
    }

    public void setName(String name) {
        super.setName(name);
        getModel().notifyCharacterChanged(this, CHANGE_BEHAVIOR);
    }

    public void destroy() {
        getModel().notifyCharacterChanged(this, CHANGE_DELETED);
    }

    public Behavior getBehavior() {
        return behavior;
    }

    /*
     * Set the current behavior of this character
     */
    public void setBehavior(Behavior b) {
        behavior = b;
    }
}