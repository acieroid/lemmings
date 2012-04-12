package controller.behaviors;

import model.Character;
import controller.Controller;
import controller.Behavior;

public class Walker extends SimpleBehavior {
    public Walker(Controller controller, Character character) {
        super(controller, character);
    }

    public Walker(Behavior b) {
        super(b);
    }

    public String getName() {
        return "walker";
    }
}
