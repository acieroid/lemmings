package controller.behaviors;

import model.Character;
import controller.Controller;
import controller.Behavior;
import controller.CollisionMap;

public class Blocker extends SimpleBehavior {
    public Blocker(Behavior b) {
        super(b);
    }

    public String getName() {
        return "blocker";
    }

    public void update(CollisionMap map) {
        /* don't move */
    }
}