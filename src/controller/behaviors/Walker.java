package controller.behaviors;

import controller.Behavior;
import controller.CollisionMap;
import model.Character;

public class Walker implements Behavior {
    private Character character;
    
    public Walker(Character character) {
        this.character = character;
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
            if (!character.isFalling() && map.isCollisionFree(x, y+2, width, height))
                character.setFalling(true);
        }
        else if (character.isFalling()) {
            character.setFalling(false);
        }

        if (!character.isFalling()) {
            int dx = character.getDirection();

            if (map.isCollisionFree(x+dx, y, width, height)) {
                character.setX(x+dx);
            }
            else if (map.isCollisionFree(x+dx, y-1, width, height)) {
                /* Can climb one pixel */
                character.setX(x+dx);
                character.setY(y-1);
            }
            else {
                character.changeDirection();
            }
        }
    }
}
