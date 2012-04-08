package model;

public class Character extends Entity {
    public static int LEFT = -1;
    public static int RIGHT = 1;

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
    }

    public int getDirection() {
        return direction;
    }

    public void changeDirection() {
        direction *= -1;
    }
}