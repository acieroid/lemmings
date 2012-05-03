package model.behaviors;

import model.Character;
import model.Model;
import model.Behavior;
import model.Map;

import java.io.Serializable;

abstract class SimpleBehavior implements Behavior, Serializable{
    private Model model;
    private Character character;

    public SimpleBehavior(Model model, Character character) {
        this.model = model;
        this.character = character;
        character.setName(getName());
    }

    public SimpleBehavior(Behavior b) {
        this(b.getModel(), b.getCharacter());
    }

    abstract public String getName();

    public Character getCharacter() {
        return character;
    }

    public Model getModel() {
        return model;
    }

    public void update(Map map, long delta) {
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
        if (character.getDirection() != Character.DONT_MOVE &&
            !character.isFalling()) {
            int dx = character.getDirection();

            if (map.isCollisionFree(x+dx, y, width, height)) {
                character.setX(x+dx);
            }
            else if (map.isCollisionFree(x+dx, y-1, width, height)) {
                /* Can climb one pixel */
                character.setX(x+dx);
                character.setY(y-1);
            }
            else if (map.isCollisionFree(x+dx, y-2, width, height)) {
                /* Can even climb two pixel ! */
                character.setX(x+dx);
                character.setY(y-2);
            }
            else {
                character.changeDirection();
            }
        }

        /* Check the exit */
        if (map.getExitX() == character.getX() + width/2 &&
            map.getExitY() >= character.getY() &&
            map.getExitY() <= character.getY() + character.getWidth())
            character.setBehavior(new Exit(this));
    }
}