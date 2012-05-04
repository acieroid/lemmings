package view.gui;

import controller.Controller;

public class CharacterBehavior extends ButtonBehavior {
    private String behavior;
    private GUI gui;

    public CharacterBehavior(Controller controller, GUI gui, String behavior) {
        super(controller);
        this.gui = gui;
        this.behavior = behavior;
    }

    public void pressed() {
        gui.disableAllCharactersButton();
        getButton().enable();
        getController().setSelectedBehavior(behavior);
    }

    public void released() {
        /* do nothing */
    }
}
            