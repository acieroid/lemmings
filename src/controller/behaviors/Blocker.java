package controller.behaviors;

import model.Character;
import controller.Controller;
import controller.Behavior;
import controller.CollisionMap;

public class Blocker extends SimpleBehavior {
    public Blocker(Behavior b) {
        super(b);
        getCharacter().setDirection(Character.DONT_MOVE);
    }

    public String getName() {
        return "blocker";
    }

    /* TODO: actually *block* the lemmings */
}