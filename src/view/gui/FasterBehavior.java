package view.gui;

import controller.Controller;

public class FasterBehavior extends ButtonBehavior {
    public FasterBehavior(Controller controller) {
        super(controller);
    }

    public void pressed() {
        super.pressed();
        getController().increaseSpeed();
    }
}
