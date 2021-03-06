package view;

import model.Model;
import model.ModelObserver;
import model.Character;
import model.ResourceManager;
import controller.Controller;
import util.LemmingsException;
import util.Selector;
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

public class View extends BasicGameState implements InputListener, ModelObserver {
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

    private Selector mapSelector;

    private MapImage map;
    private ArrayList<CharacterAnimation> characters;
    private CharacterAnimation selected;

    private boolean initialized, modelReloaded;
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
        modelReloaded = false;
    }

    public int getID() {
        return ID;
    }

    public void setModel(Model model) {
        if (model != null)
            modelReloaded = true;
        this.model = model;
        if (gui != null)
            gui.setModel(model);
        toAdd = new ArrayList<Character>(model.getCharacters());
    }

    public void setMapSelector(Selector mapSelector) {
        this.mapSelector = mapSelector;
    }

    public Model getModel() {
        return model;
    }

    public void setController(Controller c) {
        controller = c;
    }

    public void init(GameContainer gc, StateBasedGame game) {
        this.game = game;
        font = gc.getGraphics().getFont();
    }

    public void update(GameContainer gc, StateBasedGame game, int delta) {
        /* Do nothing related to the model here */
        Input input = gc.getInput();

        if (toAdd.size() != 0) {
            for (int i = 0; i < toAdd.size(); i++) {
                Character c = toAdd.get(i);
                try {
                    characters.add(GraphicsResourceManager.getAnimation(c));
                } catch (LemmingsException e) {
                    log.add(e.getMessage());
                }
            }
            toAdd.clear();
        }

        if (toDestroy.size() != 0) {
            for (int i = 0; i < toDestroy.size(); i++)
                toDestroy.get(i).apply(map);
            toDestroy.clear();
        }

        if (modelReloaded) {
            try {
                map = GraphicsResourceManager.getMap(model.getMap().getName());
                map.applyChanges(model.getMap().getChanges());
            } catch (LemmingsException e) {
                log.add(e.getMessage());
            }
            modelReloaded = false;
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

    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        Input input = gc.getInput();
        if (map == null)
            return;

        map.getBackground().draw(-scrollX, -scrollY);
        selected = null;

        for (int i = 0; i < characters.size(); i++) {
            CharacterAnimation a = characters.get(i);
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
        else if (key == Input.KEY_S) {
            try {
                controller.save();
            } catch (LemmingsException e) {
                log.add(e.getMessage());
            }
        }
        else if (key == Input.KEY_L) {
            try {
                controller.load();
            } catch (LemmingsException e) {
                log.add(e.getMessage());
            }
        }
        else if (key == Input.KEY_F2) {
            controller.decreaseSpeed();
        }
        else if (key == Input.KEY_F3) {
            controller.increaseSpeed();
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
        width = gc.getWidth();
        height = gc.getHeight();
        try {
            controller.start(mapName);
            map = GraphicsResourceManager.getMap(mapName);
            log = new Log(width - 20, 200, font);
            gui = new GUI(0, gc.getHeight()-100, gc.getWidth(), 100,
                          controller, model);
        } catch (LemmingsException e) {
            log.add(e.getMessage());
            game.enterState(Menu.ID);
        } catch (SlickException e) {
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
                    a.change(change);
                    break;
                }
            }
        }
    }

    /**
     * Called when the model speed changed
     */
    public void speedChanged() {
        int speed = model.getSpeed();
        for (int i = 0; i < characters.size(); i++)
            characters.get(i).setSpeed(speed);
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
        private int x, y, w, h;
        private int[] zone;
        private boolean hasZone;

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

        public void apply(MapImage map) {
            if (hasZone)
                map.destroy(zone, x, y, w, h);
            else
                map.destroy(x, y, w, h);
        }
    }

    /**
     * Draw centered text on a GameContainer
     */
    public static void drawCenteredText(GameContainer gc, String text) {
        Font font = gc.getGraphics().getFont();
        try {
            font.drawString(gc.getWidth()/2 - font.getWidth(text)/2,
                            gc.getHeight()/2 - font.getLineHeight()/2,
                            text, Color.white);
        } catch (org.lwjgl.opengl.OpenGLException e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }

    /**
     * Draw centered text on a game container, at a certain position
     */
    public static void drawCenteredText(GameContainer gc, String text, int y) {
        Font font = gc.getGraphics().getFont();
        try {
            font.drawString(gc.getWidth()/2 - font.getWidth(text)/2,
                            y - font.getLineHeight()/2,
                            text, Color.white);
        } catch (org.lwjgl.opengl.OpenGLException e) {
            System.out.println("Warning: " + e.getMessage());

        }
    }

    /**
     * Draw centered text on a certain line
     */
    public static void drawCenteredText(GameContainer gc, String text,
                                        int line, int nLines, Color color) {
        Font font = gc.getGraphics().getFont();
        int spacing = font.getLineHeight()-5;
        int start = gc.getHeight()/2 - (spacing+font.getLineHeight())*nLines/2;
        int x = gc.getWidth()/2 - font.getWidth(text)/2;
        int y = start + line*(spacing+font.getLineHeight());
        try {
            font.drawString(x, y, text, color);
        } catch (org.lwjgl.opengl.OpenGLException e) {
            System.out.println("Warning: " + e);
        }
    }
}
