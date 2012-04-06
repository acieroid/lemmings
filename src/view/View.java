package view;

import model.Model;
import controller.Controller;
import util.LemmingsException;

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
    private static int width = 800, height = 600;

    private Model model;
    private Controller controller;

    private int scrollX, scrollY;
    private int scrollingDir;
    private static int scrollSpeed = 1;

    private boolean inMenu; /* TODO: use strategy pattern ? */
    private ListIterator<String> menuItem;

    private Log log;

    private ResourceManager manager;
    private Map map;
    private TrueTypeFont font;

    public View() {
        super("Lemmings");
        scrollX = 0;
        scrollY = 0;
        scrollingDir = 0;
        inMenu = true;
        manager = new ResourceManager("../data");
    }

    public void setModel(Model m) {
        model = m;
    }

    public void setController(Controller c) {
        controller = c;
    }

    public void init(GameContainer container)
        throws SlickException {
        menuItem = model.getAllMaps().listIterator();
        font = manager.getTTF("font.ttf", Font.BOLD, 20);
        log = new Log(width - 20, 200,
                      manager.getTTF("font.ttf", Font.PLAIN, 12));
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
            drawCenteredText("< " + menuItem.next() + " >");
            menuItem.previous();
        }
        else {
            map.draw(-scrollX, -scrollY);

            for (model.Character c : model.getCharacters())
                g.drawRect(c.getX()-scrollX, c.getY()-scrollY, c.getWidth(), c.getHeight());

            if (model.isPaused())
                drawCenteredText("Pause");
        }
        log.draw(10, height - 210);
    }

    private void drawCenteredText(String text) {
        font.drawString(width/2 - font.getWidth(text)/2,
                        height/2 - font.getHeight()/2,
                        text, Color.white);
    }
        
    public void start() {
        try {
            AppGameContainer container = new AppGameContainer(this, width, height, false);
            container.start();
        } catch (Exception e) {
            System.out.println("Can't launch the view: " + e.getMessage());
        }
    }

    public void keyPressed(int key, char c) {
        if (key == Input.KEY_F1)
            log.toggle();
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
                String mapName = menuItem.next();
                menuItem.previous();
                try {
                    log.add("Loading map: '" + mapName + "'");
                    model.start(mapName);
                    map = manager.getMap(mapName);
                    log.add("Map loaded.");
                } catch (Exception e) {
                    inMenu = true;
                    log.add(e.getMessage());
                }
            }
        }
        else {
            if (key == Input.KEY_ESCAPE) {
                inMenu = true;
                model.stop();
            }
            else if (key == Input.KEY_P || key == Input.KEY_PAUSE) {
                model.pause();
            }
        }
    }

    public void mouseClicked(int button, int x, int y, int clickCount) {
        if (!inMenu) {
            try {
                controller.mouseClicked(x + scrollX, y + scrollY);
            } catch (Exception e) {
                log.add(e.getMessage());
            }
        }
    }
}
