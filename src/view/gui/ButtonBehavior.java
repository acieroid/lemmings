package view.gui;

import controller.Controller;

public class ButtonBehavior {
    private Controller controller;
    private Button button;

    public ButtonBehavior(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    public void pressed() {
        button.enable();
    }

    public void released() {
        button.disable();
    }
}