package view.gui;

import util.LemmingsException;
import controller.Controller;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;

import java.util.ArrayList;

public class GUI {
    private ArrayList<Button> buttons, characters;
    private int x, y;
    private int w, h;

    public static String RESOURCE_DIR = "../data/";

    public GUI(int x, int y, int w, int h, Controller controller)
        throws SlickException, LemmingsException {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        buttons = new ArrayList<Button>();
        buttons.add(new Button("gui", "nuke.sprite", w - 50, y,
                               new NukeBehavior(controller),
                               true));
        buttons.add(new Button("gui", "faster.png", w - 100, y,
                               new FasterBehavior(controller)));
        buttons.add(new Button("gui", "slower.png", w - 150, y,
                               new SlowerBehavior(controller)));
        buttons.add(new Button("gui", "pause.png", w - 200, y,
                               new PauseBehavior(controller)));

        characters = new ArrayList<Button>();
        characters.add(new Button("characters/bomber", "left.sprite", w - 250, y,
                                  new CharacterBehavior(controller, this, "bomber"),
                                  true));
        characters.add(new Button("characters/blocker", "left.sprite", w - 300, y,
                                  new CharacterBehavior(controller, this, "blocker"),
                                  true));
    }

    public void draw(GameContainer gc) {
        for (Button b : buttons)
            b.draw();

        for (Button b : characters)
            b.draw();
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