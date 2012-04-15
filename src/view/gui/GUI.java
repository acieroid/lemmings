package view.gui;

import util.LemmingsException;
import controller.Controller;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;

import java.util.ArrayList;

public class GUI {
    private ArrayList<Button> buttons;
    private int x, y;
    private int w, h;

    public static String RESOURCE_DIR = "../data/gui";

    public GUI(int x, int y, int w, int h, Controller controller)
        throws SlickException {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        buttons = new ArrayList<Button>();
        buttons.add(new Button("pause.png", w - 200, y,
                               new PauseBehavior(controller)));
        buttons.add(new Button("slower.png", w - 150, y,
                               new SlowerBehavior(controller)));
        buttons.add(new Button("faster.png", w - 100, y,
                               new FasterBehavior(controller)));
        //nuke = new AnimatedButton("nuke.sprite");
    }

    public void draw(GameContainer gc) {
        for (Button b : buttons)
            b.draw();
    }

    public void mousePressed(int x, int y) {
        for (Button b : buttons)
            if (b.contains(x, y))
                b.pressed();
    }

    public void mouseReleased(int x, int y) {
        for (Button b : buttons)
            if (b.contains(x, y))
                b.released();
    }
}