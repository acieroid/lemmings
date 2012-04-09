package view;

import model.Model;
import model.Character;
import controller.Controller;
import util.LemmingsException;

import java.util.ListIterator;
import java.util.ArrayList;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.Font;
import org.newdawn.slick.Color;

public class View extends BasicGame implements InputListener {
    private static int width = 640, height = 480;

    private Model model;
    private Controller controller;

    private int scrollX, scrollY;
    private int scrollingDir;
    private static int scrollSpeed = 1;

    private Font font;
    private Menu menu;
    private Log log;

    private ResourceManager manager;

    private MapImage map;
    private ArrayList<CharacterAnimation> characters;

    public View() {
        super("Lemmings");
        scrollX = 0;
        scrollY = 0;
        scrollingDir = 0;
        manager = new ResourceManager("../data");
        characters = new ArrayList<CharacterAnimation>();
    }

    public void setModel(Model m) {
        model = m;
    }

    public void setController(Controller c) {
        controller = c;
    }

    public void init(GameContainer container)
        throws SlickException {
        try {
            font = manager.getFont("font");
            menu = new Menu(width, height, font,
                            model.getAllMaps());
            menu.activate();
            log = new Log(width - 20, 200, font);
            container.getInput().addPrimaryListener(this);
        } catch (LemmingsException e) {
            throw new SlickException(e.toString());
        }
    }

    public void update(GameContainer container, int delta) {
        /* Do nothing related to the model here */
        Input input = container.getInput();

        if (!menu.isActivated()) {
            /* Handle scrolling */
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

    /**
     * This method is called when a new character is added to the model
     * @param character: the new character
     */
    public void characterAdded(Character character) {
        log.add("Character added");
        try {
            characters.add(manager.getAnimation(character));
        } catch (LemmingsException e) {
            log.add(e.getMessage());
        }
    }

    /**
     * This method is called when a character changed its state
     * (direction, or behavior)
     * @param character: the character
     * @param change: the change (@see Character.CHANGE*)
     */
    public void characterChanged(Character character, int change) {
        log.add("Character changed its state: " + change);
    }

    public void render(GameContainer container, Graphics g) {
        if (menu.isActivated()) {
            menu.draw();
        }
        else {
            map.draw(-scrollX, -scrollY);

            for (CharacterAnimation a : characters)
                g.drawAnimation(a,
                                a.getX()-scrollX, a
                                .getY()-scrollY);

            if (controller.isPaused())
                drawCenteredText("Pause");
        }
        log.draw(10, height - 210);
    }

    private void drawCenteredText(String text) {
        font.drawString(width/2 - font.getWidth(text)/2,
                        height/2 - font.getLineHeight()/2,
                        text, Color.white);
    }
        
    public void start() {
        try {
            AppGameContainer container = new AppGameContainer(this, width, height, false);
            container.setShowFPS(true);
            container.setTargetFrameRate(60);
            container.start();
        } catch (Exception e) {
            System.out.println("Can't launch the view: " + e.getMessage());
        }
    }

    public void keyPressed(int key, char c) {
        if (key == Input.KEY_F1)
            log.toggle();
        if (menu.isActivated()) {
            if (menu.keyPressed(key)) {
                String mapName = menu.getMapName();
                try {
                    log.add("Loading map: '" + mapName + "'");
                    controller.start(mapName);
                    map = manager.getMap(mapName);
                    log.add("Map loaded.");
                } catch (Exception e) {
                    menu.activate();
                    log.add(e.getMessage());
                }
            }
        }
        else {
            if (key == Input.KEY_ESCAPE) {
                menu.activate();
                controller.stop();
            }
            else if (key == Input.KEY_P || key == Input.KEY_PAUSE) {
                controller.pause();
                for (CharacterAnimation animation : characters) {
                    if (controller.isPaused())
                        animation.stop();
                    else
                        animation.start();
                }
            }
            else if (key == Input.KEY_F2) {
                controller.decreaseSpeed();
                log.add("Speed set to " + controller.getSpeed());
            }
            else if (key == Input.KEY_F3) {
                controller.increaseSpeed();
                log.add("Speed set to " + controller.getSpeed());
            }
        }
    }

    public void mouseClicked(int button, int x, int y, int clickCount) {
        if (!menu.isActivated()) {
            try {
                controller.mouseClicked(x + scrollX, y + scrollY);
            } catch (Exception e) {
                log.add(e.getMessage());
            }
        }
    }
}
