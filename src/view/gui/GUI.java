package view.gui;

import view.View;
import model.Model;
import controller.Controller;
import util.LemmingsException;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;

import java.util.ArrayList;

public class GUI {
    private ArrayList<Button> buttons, characters;
    private int x, y;
    private int w, h;
    private Model model;

    public static String RESOURCE_DIR = "../data/";

    public GUI(int x, int y, int w, int h, Controller controller, Model model)
        throws SlickException, LemmingsException {
        this.model = model;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        String[] images = { "nuke.sprite", "faster.png", "slower.png",
                          "pause.png", "save.png", "load.png" };
        ButtonBehavior[] buttonBehaviors = { new NukeBehavior(controller),
                                             new FasterBehavior(controller),
                                             new SlowerBehavior(controller),
                                             new PauseBehavior(controller),
                                             new SaveBehavior(controller),
                                             new LoadBehavior(controller) };
        buttons = new ArrayList<Button>();
        for (int i = 0; i < images.length; i++)
            buttons.add(new Button("gui", images[i], w - 50 * (i+1), y,
                                   buttonBehaviors[i]));

        characters = new ArrayList<Button>();
        String[] behaviors = model.getCharacterBehaviors();
        for (int i = 0; i < behaviors.length; i++)
            characters.add(new Button("characters/" + behaviors[i], "right.sprite",
                                      w - 50*(i+1+images.length), y,
                                      new CharacterBehavior(controller, this,
                                                            behaviors[i])));
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void draw(GameContainer gc) {
        for (Button b : buttons)
            b.draw();

        for (Button b : characters)
            b.draw();

        View.drawCenteredText(gc,
                              "Lemmings released: " + model.getLemmingsReleased() +
                              "/" + model.getMap().getLemmingsToRelease() + " | " +
                              "Lemmings rescued: " + model.getLemmingsRescued() +
                              "/" + model.getMap().getLemmingsToRescue(),
                              gc.getHeight()-20);
    }

    public void mousePressed(int x, int y) {
        for (Button b : buttons)
            if (b.contains(x, y))
                b.pressed();

        for (Button b : characters)
            if (b.contains(x, y))
                b.pressed();
    }

    public void mouseReleased(int x, int y) {
        for (Button b : buttons)
            if (b.contains(x, y))
                b.released();

        for (Button b : characters)
            if (b.contains(x, y))
                b.released();
    }

    public void disableAllCharactersButton() {
        for (Button b : characters)
            b.disable();
    }
}