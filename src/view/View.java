package view;

import model.Model;
import model.Character;
import controller.Controller;
import util.LemmingsException;
import util.MapSelector;

import java.util.ListIterator;
import java.util.ArrayList;

import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.Animation;

public class View extends BasicGameState implements InputListener {
    public static final int ID = 2;
    private StateBasedGame game;

    private static int width = 640, height = 480;

    private Model model;
    private Controller controller;

    private int scrollX, scrollY;
    private static int scrollSpeed = 1;

    private Font font;
    private Log log;

    private ResourceManager manager;
    private MapSelector mapSelector;

    private MapImage map;
    private ArrayList<CharacterAnimation> characters;
    private CharacterAnimation selected;

    public View() {
        super();
        scrollX = 0;
        scrollY = 0;
        manager = new ResourceManager("../data");
        characters = new ArrayList<CharacterAnimation>();
    }

    public int getID() {
        return ID;
    }

    public MapSelector getMapSelector() {
        return mapSelector;
    }

    public void setModel(Model m) {
        model = m;
        mapSelector = new MapSelector(m.getAllMaps());
    }

    public void setController(Controller c) {
        controller = c;
    }

    public void init(GameContainer container, StateBasedGame game)
        throws SlickException {
        this.game = game;
        font = container.getGraphics().getFont();
        log = new Log(width - 20, 200, font);
    }

    public void update(GameContainer container, StateBasedGame game, int delta) {
        /* Do nothing related to the model here */
        Input input = container.getInput();

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

    private void drawCenteredText(GameContainer gc, String text) {
        font.drawString(gc.getWidth()/2 - font.getWidth(text)/2,
                        gc.getHeight()/2 - font.getLineHeight()/2,
                        text, Color.white);
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        Input input = container.getInput();
        map.draw(-scrollX, -scrollY);
        selected = null;

        for (CharacterAnimation a : characters) {
            try {
                a.checkIfChanged();
                g.drawAnimation(a.getAnimation(),
                                a.getX()-scrollX, a
                                .getY()-scrollY);
                int x = input.getMouseX() + scrollX;
                int y = input.getMouseY() + scrollY;
                if (x >= a.getX() && x <= a.getX() + a.getWidth() &&
                    y >= a.getY() && y <= a.getY() + a.getHeight())
                    selected = a;
            } catch (LemmingsException e) {
                log.add(e.getMessage());
            }
        }
        if (selected != null)
            g.drawRect(selected.getX()-scrollX, selected.getY()-scrollY,
                       selected.getWidth(), selected.getHeight());

        if (controller.isPaused())
            drawCenteredText(container, "Pause");

        log.draw(10, height - 210);
    }

    public void keyPressed(int key, char c) {
        if (key == Input.KEY_F1) {
            log.toggle();
        }
        else if (key == Input.KEY_ESCAPE) {
            game.enterState(Menu.ID);
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

    public void mouseClicked(int button, int x, int y, int clickCount) {
        try {
            if (selected != null)
                controller.characterSelected(selected.getCharacter());
            else
                controller.mouseClicked(x + scrollX, y + scrollY);
        } catch (Exception e) {
            log.add(e.getMessage());
        }
    }

    public void enter(GameContainer container, StateBasedGame game) {
        String mapName = mapSelector.current();
        try {
            log.add("Loading map: '" + mapName + "'");
            controller.start(mapName);
            map = manager.getMap(mapName);
            log.add("Map loaded.");
        } catch (Exception e) {
            log.add(e.getMessage());
            game.enterState(Menu.ID);
        }
    }

    public void leave(GameContainer container, StateBasedGame game) {
        controller.stop();
    }
        
    /**
     * This method is called when a new character is added to the model
     * @param character: the new character
     */
    public void characterAdded(Character character) {
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
        try {
            if (change == Character.CHANGE_DELETED) {
                /* TODO */
                return;
            }
            for (CharacterAnimation a : characters) {
                if (a.getCharacter() == character) {
                    if (change == Character.CHANGE_FALLING)
                        a.setAnimation(character.getName());
                    else if (change == Character.CHANGE_DIRECTION)
                        a.changeDirection();

                    break;
                }
            }
        } catch (LemmingsException e) {
            log.add(e.getMessage());
        }
    }

    /**
     * Called when all the characters are deleted
     */
    public void charactersCleared() {
        characters.clear();
    }


    public void start() {
        try {
            game = new LemmingsGame(this);
            AppGameContainer container = new AppGameContainer(game, width, height, false);
            container.setShowFPS(true);
            container.setTargetFrameRate(60);
            container.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
