package controller.behaviors;

import model.Character;
import controller.Controller;
import controller.Behavior;
import controller.CollisionMap;

public class Exit extends SimpleBehavior {
    public Exit(Behavior b) {
        super(b);
    }

    public String getName() {
        return "exit";
    }

    public void update(CollisionMap map) {
        /* don't move */
        /* TODO: notify the controller to delete this character after a bit */
    }
}
