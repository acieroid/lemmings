package view;

import model.Model;
import model.Map;

import java.util.Observable;
import java.util.Observer;
import java.util.ListIterator;

import java.awt.Font;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Color;

public class View extends BasicGame implements Observer, InputListener {
    private Model model;
    private int scrollX, scrollY;
    private int scrollingDir;
    private static int scrollSpeed = 1;
    private static int width = 800, height = 600;

    private boolean inMenu; /* TODO: use strategy pattern ? */
    private ListIterator<String> menuItem;
    private TrueTypeFont font;

    public View() {
        super("Lemmings");
        scrollX = 0;
        scrollY = 0;
        scrollingDir = 0;
        inMenu = true;
    }

    public void setModel(Model m) {
        model = m;
    }

    public void init(GameContainer container)
        throws SlickException {
        menuItem = model.getAllMaps().listIterator();
        font = new TrueTypeFont(new Font("../data/font.ttf", Font.BOLD, 20),
                                true);
        container.getInput().addPrimaryListener(this);
    }

    public void update(GameContainer container, int delta) {
        /* Do nothing related to the model here */
        Input input = container.getInput();

        if (!inMenu) {
            if (input.isKeyDown(Input.KEY_LEFT))
                scrollX = Math.max(scrollX - (scrollSpeed * delta), 0);
            if (input.isKeyDown(Input.KEY_RIGHT))
                scrollX = Math.min(scrollX + (scrollSpeed * delta),
                                   model.getMap().getWidth() - width);
            if (input.isKeyDown(Input.KEY_UP))
                scrollY = Math.max(scrollY - (scrollSpeed * delta), 0);
            if (input.isKeyDown(Input.KEY_DOWN))
                scrollY = Math.min(scrollY + (scrollSpeed * delta),
                                   model.getMap().getHeight() - height);
        }
    }

    public void update(Observable observable, Object nothing) {
        /* TODO: Call render ? */
    }

    public void render(GameContainer container, Graphics g) {
        if (inMenu) {
            String str = "< " + menuItem.next() + " >";
            menuItem.previous();
            font.drawString(width/2 - font.getWidth(str)/2,
                            height/2 - font.getHeight()/2,
                            str, Color.white);
        }
        else {
            model.getMap().draw(-scrollX, -scrollY);
        }
    }

    public void start() {
        try {
            AppGameContainer container = new AppGameContainer(this, width, height, false);
            container.start();
        } catch (Exception e) {
            System.out.println("Can't launch the view: " + e.toString());
        }
    }

    public void keyPressed(int key, char c) {
        if (inMenu) {
            if (key == Input.KEY_LEFT) {
                if (menuItem.hasPrevious())
                    menuItem.previous();
            }
            else if (key == Input.KEY_RIGHT) {
                menuItem.next();
                if (!menuItem.hasNext())
                    /* We're on the last element, so we go back from one */
                    menuItem.previous();
            }
            else if (key == Input.KEY_ENTER) {
                inMenu = false;
                model.start(menuItem.next());
                menuItem.previous();
            }
        }
    }
                
}
