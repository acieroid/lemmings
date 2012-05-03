package view;

import model.Model;
import model.Character;
import model.ResourceManager;
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

    private int width, height;

    private Model model;
    private Controller controller;

    private int scrollX, scrollY;
    private static int scrollSpeed = 1;

    private Font font;
    private Log log;
    private GUI gui;

    private MapSelector mapSelector;

    private MapImage map;
    private ArrayList<CharacterAnimation> characters;
    private CharacterAnimation selected;

    private boolean initialized;
    private ArrayList<Character> toAdd;
    private ArrayList<DestroyInfo> toDestroy;

    public View() {
        super();
        scrollX = 0;
        scrollY = 0;
        characters = new ArrayList<CharacterAnimation>();
        initialized = false;
        toAdd = new ArrayList<Character>();
        toDestroy = new ArrayList<DestroyInfo>();
    }

    public int getID() {
        return ID;
    }

    public MapSelector getMapSelector() {
        return mapSelector;
    }

    public void setModel(Model model) {
        this.model = model;
        mapSelector = new MapSelector(ResourceManager.getAllMaps());
    }

    public Model getModel() {
        return model;
    }

    public void setController(Controller c) {
        controller = c;
    }

    public void init(GameContainer gc, StateBasedGame game)
        throws SlickException {
        try {
            this.game = game;
            font = gc.getGraphics().getFont();
            log = new Log(width - 20, 200, font);
            gui = new GUI(0, gc.getHeight()-100, gc.getWidth(), 100, controller);
        } catch (LemmingsException e) {
            throw new SlickException(e.getMessage());
        }
    }

    public void update(GameContainer gc, StateBasedGame game, int delta) {
        /* Do nothing related to the model here */
        Input input = gc.getInput();

        if (toAdd.size() != 0) {
            for (Character c : toAdd) {
                try {
                    characters.add(GraphicsResourceManager.getAnimation(c));
                } catch (LemmingsException e) {
                    log.add(e.getMessage());
                }
            }
            toAdd.clear();
        }

        if (toDestroy.size() != 0) {
            for (DestroyInfo i : toDestroy) {
                /* TODO: map should handle the destroy info */
                if (i.hasZone)
                    map.destroy(i.zone, i.x, i.y, i.w, i.h);
                else
                    map.destroy(i.x, i.y, i.w, i.h);
            }
            toDestroy.clear();
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
        try {
            font.drawString(gc.getWidth()/2 - font.getWidth(text)/2,
                            gc.getHeight()/2 - font.getLineHeight()/2,
                            text, Color.white);
        } catch (org.lwjgl.opengl.OpenGLException e) {
            System.out.println("Warning: " + e);
        }
    }

    private void drawCenteredText(GameContainer gc, String text, int y) {
        try {
            font.drawString(gc.getWidth()/2 - font.getWidth(text)/2,
                            y - font.getLineHeight()/2,
                            text, Color.white);
        } catch (org.lwjgl.opengl.OpenGLException e) {
            System.out.println("Warning: " + e);
        }
    }
        

    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        Input input = gc.getInput();
        if (map == null)
            return;

        map.getBackground().draw(-scrollX, -scrollY);
        selected = null;

        ArrayList<CharacterAnimation> anims;
        synchronized(characters) {
            anims = new ArrayList<CharacterAnimation>(characters);
        }

        for (CharacterAnimation a : anims) {
            try {
                if (model.isPaused())
                    a.stop();
                else if (a.isStopped())
                    a.start();

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

        if (model.isPaused())
            drawCenteredText(gc, "Pause");

        gui.draw(gc);
        /* TODO: put this in the GUI */
        drawCenteredText(gc,
                         "Lemmings released: " + model.getLemmingsReleased() +
                         "/" + model.getMap().getLemmingsToRelease() + " | " +
                         "Lemmings rescued: " + model.getLemmingsRescued() +
                         "/" + model.getMap().getLemmingsToRescue(),
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
        }
        else if (key == Input.KEY_F2) {
            controller.decreaseSpeed();
            log.add("Speed set to " + model.getSpeed());
        }
        else if (key == Input.KEY_F3) {
            controller.increaseSpeed();
            log.add("Speed set to " + model.getSpeed());
        }
    }

    public void mouseClicked(int button, int x, int y, int clickCount) {
        try {
            if (selected != null)
                controller.characterSelected(selected.getCharacter());
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
            controller.start(mapName);
            map = GraphicsResourceManager.getMap(mapName);
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
            AppGameContainer gc = new AppGameContainer(game);
            width = gc.getScreenWidth();
            height = gc.getScreenHeight();
            gc.setDisplayMode(width, height, false);
            gc.setShowFPS(true);
            gc.setTargetFrameRate(60);
            gc.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when pixels on the collision map are destroyed
     */
    public void destroyed(int x, int y, int w, int h) {
        toDestroy.add(new DestroyInfo(x, y, w, h));
    }

    /**
     * Called when a zone of pixel on the collision maps are destroyed
     */
    public void destroyed(int[] zone, int x, int y, int w, int h) {
        toDestroy.add(new DestroyInfo(zone, x, y, w, h));
    }

    /**
     * Represents a zone to be destroyed
     */
    class DestroyInfo {
        public int x, y, w, h;
        public int[] zone;
        public boolean hasZone;

        public DestroyInfo(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.hasZone = false;
        }

        public DestroyInfo(int zone[], int x, int y, int w, int h) {
            this(x, y, w, h);
            this.zone = zone;
            this.hasZone = true;
        }
    }   
}
