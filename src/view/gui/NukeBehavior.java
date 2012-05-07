package view.gui;

import controller.Controller;
import util.LemmingsException;

public class NukeBehavior extends ButtonBehavior {
    public NukeBehavior(Controller controller) {
        super(controller);
    }

    public void pressed() {
        try {
            getController().nuke();
            getButton().enable();
        } catch (LemmingsException e) {
            System.out.println(e.getMessage());
        }
    }

    public void released() {
        /* do nothing */
    }
}
