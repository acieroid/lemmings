package model;

public class Character extends Entity {
    public static int LEFT = -1;
    public static int RIGHT = 1;

    public static int CHANGE_DIRECTION = 1;
    public static int CHANGE_FALLING = 2;
    public static int CHANGE_BEHAVIOR = 3;

    private int direction;
    private boolean falling;

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
        getView().characterChanged(this, CHANGE_FALLING);
    }

    public int getDirection() {
        return direction;
    }

    public void changeDirection() {
        direction *= -1;
        getView().characterChanged(this, CHANGE_DIRECTION);
    }

    public void setName(String name) {
        super.setName(name);
        getView().characterChanged(this, CHANGE_BEHAVIOR);
    }
}