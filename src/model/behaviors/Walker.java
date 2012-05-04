package model.behaviors;

import model.Character;
import model.Model;
import model.Behavior;

public class Walker extends SimpleBehavior {
    public Walker(Model model, Character character) {
        super(model, character);
    }

    public String getName() {
        return "walker";
    }
}
