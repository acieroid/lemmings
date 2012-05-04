package view.gui;

import controller.Controller;
import util.LemmingsException;

public class LoadBehavior extends ButtonBehavior {
    public LoadBehavior(Controller controller) {
        super(controller);
    }

    public void pressed() {
        super.pressed();
        try {
            getController().load();
        } catch (LemmingsException e) { }
    }
}