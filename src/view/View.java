package view;

import model.Model;
import model.Map;

import java.util.Observable;
import java.util.Observer;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;

public class View extends BasicGame implements Observer {
    private Model model;

    public View() {
        super("Lemmings");
    }

    public void setModel(Model m) {
        model = m;
    }

    public void init(GameContainer container)
        throws SlickException {
        model.init();
    }

    public void update(GameContainer container, int delta) {
        /* Do nothing here */
    }

    public void update(Observable observable, Object nothing) {
        /* TODO: Call render ? */
    }

    public void render(GameContainer container, Graphics g) {
        model.getMap().draw(0, 0);
    }

    public void start() {
        try {
            AppGameContainer container = new AppGameContainer(this, 960, 960, false);
            container.start();
        } catch (Exception e) {
            System.out.println("Can't launch the view: " + e.toString());
        }
    }
}
    