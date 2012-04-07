package controller.behaviors;

import controller.Behavior;
import controller.CollisionMap;
import model.Character;

public class Walker implements Behavior {
    private static int LEFT = -1;
    private static int RIGHT = 1;

    private Character character;
    private int direction;
    private boolean isFalling;
    
    public Walker(Character character) {
        this.character = character;
        direction = RIGHT;
        isFalling = false;
    }

    public String getName() {
        return "walker";
    }

    public void update(CollisionMap map) {
        int x = character.getX();
        int y = character.getY();
        int width = character.getWidth();
        int height = character.getHeight();

        if (map.isCollisionFree(x, y+1, width, height)) {
            character.setY(y+1);
            isFalling = true;
        }
        else if (isFalling) {
            isFalling = false;
        }

        if (!isFalling) {
            int dx = direction;

            if (map.isCollisionFree(x+dx, y, width, height))
                character.setX(x+dx);
            else
                direction *= -1;
        }
    }
}
