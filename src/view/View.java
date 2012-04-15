package view;

import model.Model;
import model.Character;
import controller.Controller;
import util.LemmingsException;
import util.MapSelector;
import view.gui.GUI;

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

    private static int width = 1024, height = 768;

    private Model model;
    private Controller controller;

    private int scrollX, scrollY;
    private static int scrollSpeed = 1;

    private Font font;
    private Log log;
    private GUI gui;

    private ResourceManager manager;
    private MapSelector mapSelector;

    private MapImage map;
    private ArrayList<CharacterAnimation> characters;
    private CharacterAnimation selected;

    private boolean initialized;
    private ArrayList<Character> toAdd;

    public View() {
        super();
        scrollX = 0;
        scrollY = 0;
        manager = new ResourceManager("../data");
        characters = new ArrayList<CharacterAnimation>();
        initialized = false;
        toAdd = new ArrayList<Character>();
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

    public Model getModel() {
        return model;
    }

    public void setController(Controller c) {
        controller = c;
    }

    public void init(GameContainer gc, StateBasedGame game)
        throws SlickException {
        this.game = game;
        font = gc.getGraphics().getFont();
        log = new Log(width - 20, 200, font);
        gui = new GUI(0, gc.getHeight()-100, gc.getWidth(), 100, controller);
    }

    public void update(GameContainer gc, StateBasedGame game, int delta) {
        /* Do nothing related to the model here */
        Input input = gc.getInput();

        if (toAdd.size() != 0) {
            for (Character c : toAdd) {
                try {
                    characters.add(manager.getAnimation(c));
                } catch (LemmingsException e) {
                    log.add(e.getMessage());
                }
            }
            toAdd.clear();
        }

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

    private void drawCenteredText(GameContainer gc, String text, int y) {
        font.drawString(gc.getWidth()/2 - font.getWidth(text)/2,
                        y - font.getLineHeight()/2,
                        text, Color.white);
    }
        

    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        Input input = gc.getInput();
        if (map == null)
            return;

        map.getBackground().draw(-scrollX, -scrollY);
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

        map.getForeground().draw(-scrollX, -scrollY);

        if (controller.isPaused())
            drawCenteredText(gc, "Pause");

        gui.draw(gc);
        /* TODO: put this in the GUI */
        drawCenteredText(gc,
                         "Lemmings released: " + model.getLemmingsReleased() +
                         "/" + model.getLemmingsToRelease() + " | " +
                         "Lemmings rescued: " + model.getLemmingsRescued() +
                         "/" + model.getLemmingsToRescue(),
                         gc.getHeight()-20);
                         

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

    public void mousePressed(int button, int x, int y) {
        gui.mousePressed(x, y);
    }

    public void mouseReleased(int button, int x, int y) {
        gui.mouseReleased(x, y);
    }

    public void enter(GameContainer gc, StateBasedGame game) {
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

    public void leave(GameContainer gc, StateBasedGame game) {
        controller.stop();
    }

    /**
     * This method is called when a new character is added to the model
     * @param character: the new character
     */
    public void characterAdded(Character character) {
        toAdd.add(character);
    }

    /**
     * This method is called when a character changed its state
     * (direction, or behavior)
     * @param character: the character
     * @param change: the change (@see Character.CHANGE*)
     * @TODO: this should be implemented in CharacterAnimation
     */
    public void characterChanged(Character character, int change) {
        try {
            if (change == Character.CHANGE_DELETED) {
                CharacterAnimation toDelete = null;
                for (CharacterAnimation a : characters) {
                    if (a.getCharacter() == character) {
                        toDelete = a;
                        break;
                    }
                }
                if (toDelete != null)
                    characters.remove(characters.indexOf(toDelete));
            }
            else {
                for (CharacterAnimation a : characters) {
                    if (a.getCharacter() == character) {
                        if (change == Character.CHANGE_FALLING ||
                            change == Character.CHANGE_BEHAVIOR)
                            a.setAnimation(character.getName());
                        else if (change == Character.CHANGE_DIRECTION)
                            a.changeDirection();

                        break;
                    }
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

    /**
     * Called when a game is finished
     */
    public void finished() {
        game.enterState(LevelEnd.ID);
    }

    /**
     * Launch the view (displaying the menu first !)
     */
    public void start() {
        try {
            game = new LemmingsGame(this);
            AppGameContainer gc = new AppGameContainer(game, width, height, false);
            gc.setShowFPS(true);
            gc.setTargetFrameRate(60);
            gc.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
