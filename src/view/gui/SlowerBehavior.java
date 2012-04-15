package view.gui;

import controller.Controller;

public class SlowerBehavior extends ButtonBehavior {
    public SlowerBehavior(Controller controller) {
        super(controller);
    }

    public void pressed() {
        super.pressed();
        getController().decreaseSpeed();
    }
}