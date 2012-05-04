package view.gui;

import controller.Controller;
import util.LemmingsException;

public class SaveBehavior extends ButtonBehavior {
    public SaveBehavior(Controller controller) {
        super(controller);
    }

    public void pressed() {
        super.pressed();
        try {
            getController().save();
        } catch (LemmingsException e) { }
    }
}