package controller.behaviors;

import model.Character;
import controller.Controller;
import controller.Behavior;
import controller.CollisionMap;

abstract class SimpleBehavior implements Behavior {
    private Controller controller;
    private Character character;

    public SimpleBehavior(Controller controller, Character character) {
        this.controller = controller;
        this.character = character;
        character.setName(getName());
    }

    public SimpleBehavior(Behavior b) {
        this(b.getController(), b.getCharacter());
    }

    abstract public String getName();

    public Character getCharacter() {
        return character;
    }

    public Controller getController() {
        return controller;
    }

    public void update(CollisionMap map) {
        int x = character.getX();
        int y = character.getY();
        int width = character.getWidth();
        int height = character.getHeight();

        /* Is the character falling */
        if (map.isCollisionFree(x, y+1, width, height)) {
            character.setY(y+1);
            if (!character.isFalling() && map.isCollisionFree(x, y+2, width, height))
                character.setFalling(true);
        }
        else if (character.isFalling()) {
            character.setFalling(false);
        }

        /* The character walks */
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

        /* Check the exit */
        if (map.getExitX() >= character.getX() &&
            map.getExitX() <= character.getX() + character.getWidth() &&
            map.getExitY() >= character.getY() &&
            map.getExitY() <= character.getY() + character.getWidth())
            controller.changeBehavior(this, new Exit(this));
    }
}