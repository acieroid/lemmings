package model.behaviors;

import model.Character;
import model.Model;
import model.Behavior;
import model.Map;

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