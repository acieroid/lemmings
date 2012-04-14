package view.gui;

import util.LemmingsException;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;

public class GUI {
    private Button pause, increaseSpeed, decreaseSpeed, nuke;
    private int y;

    public static String RESOURCE_DIR = "../data/gui";

    public GUI(int y)
        throws SlickException {
        this.y = y;
        pause = new Button("pause.png");
        increaseSpeed = new Button("faster.png");
        decreaseSpeed = new Button("slower.png");
        //nuke = new AnimatedButton("nuke.sprite");
    }

    public void draw(GameContainer gc) {
        pause.draw(gc.getWidth() - 200, y);
        decreaseSpeed.draw(gc.getWidth() - 150, y);
        increaseSpeed.draw(gc.getWidth() - 100, y);
        //nuke.draw(gc.getWidth() - 50, y);
    }
}