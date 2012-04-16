package view.gui;

import controller.Controller;

public class NukeBehavior extends ButtonBehavior {
    public NukeBehavior(Controller controller) {
        super(controller);
    }

    public void pressed() {
        //getController().nuke();
        getButton().enable();
    }

    public void released() {
        /* do nothing */
    }
}
