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

    public void update(CollisionMap map, int step) {
        int x = character.getX();
        int y = character.getY();
        int width = character.getWidth();
        int height = character.getHeight();

        if (map.isCollisionFree(x, y+step, width, height)) {
            character.setY(y+step);
            isFalling = true;
        }
        else if (step == 1 && isFalling) {
            isFalling = false;
        }
        else if (step != 1) {
            for (int i = 0; i < step; i++)
                update(map, 1);
        }

        if (!isFalling) {
            int dx = step*direction;

            if (map.isCollisionFree(x+dx, y, width, height))
                character.setX(x+dx);
            else if (step == 1)
                direction *= -1;
            else if (step != 1)
                for (int i = 0; i < step; i++)
                    update(map, 1);
        }
    }
}
