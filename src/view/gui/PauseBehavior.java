package view.gui;

import controller.Controller;

public class PauseBehavior extends ButtonBehavior {
    public PauseBehavior(Controller controller) {
        super(controller);
    }

    public void pressed() {
        getController().pause();

        if (getController().isPaused())
            getButton().enable();
        else
            getButton().disable();
    }

    public void released() {
        /* do nothing */
    }
}
