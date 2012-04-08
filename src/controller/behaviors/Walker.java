package controller.behaviors;

import controller.Behavior;
import controller.CollisionMap;
import model.Character;

public class Walker implements Behavior {
    private static int LEFT = -1;
    private static int RIGHT = 1;

    private Character character;
    
    public Walker(Character character) {
        this.character = character;
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
            character.setFalling(true);
        }
        else if (step == 1 && character.isFalling()) {
            character.setFalling(false);
        }
        else if (step != 1) {
            for (int i = 0; i < step; i++)
                update(map, 1);
        }

        if (!character.isFalling()) {
            int dx = step*character.getDirection();

            if (map.isCollisionFree(x+dx, y, width, height))
                character.setX(x+dx);
            else if (step == 1)
                character.changeDirection();
            else if (step != 1)
                for (int i = 0; i < step; i++)
                    update(map, 1);
        }
    }
}
