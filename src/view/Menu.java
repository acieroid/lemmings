package view;

import util.Selector;
import util.Database;
import util.LemmingsException;

import java.util.LinkedList;
import java.util.ListIterator;

import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

import org.lwjgl.util.Display;
import org.lwjgl.opengl.DisplayMode;

public class Menu extends BasicGameState {
    public static final int ID = 1;
    private StateBasedGame game;

    private Selector maps, resolutions;
    private boolean fullscreen;
    private boolean shouldApplyResolution = false;

    private int itemSelected;
    private String[] items = {"Play", "Map", "Highscores", "Resolution", "Fullscreen", "Apply resolution", "Quit"};

    public Menu() {
        this.maps = new Selector(model.ResourceManager.getAllMaps());
        fullscreen = Database.get().getOptionBoolean("fullscreen", false);
    }

    public int getID() {
        return ID;
    }

    public Selector getMapSelector() {
        return maps;
    }

    private LinkedList<String> getResolutions(GameContainer gc) {
        DisplayMode[] modes = null;
        LinkedList<String> res = new LinkedList<String>();

        try {
            modes = Display.getAvailableDisplayModes(0, 0,
                                                     gc.getScreenWidth(),
                                                     gc.getScreenHeight(),
                                                     0, 100,
                                                     0, 100);
        } catch (org.lwjgl.LWJGLException e) {
            res.add("No resolutions available !");
        }

        /* TODO: sort resolutions ? */
        for (DisplayMode mode : modes) {
            String str = mode.getWidth() + "x" + mode.getHeight();
            if (!res.contains(str))
                res.add(str);
        }

        return res;
    }

    private void applyResolution(AppGameContainer gc) {
        shouldApplyResolution = false;
        String str[] = resolutions.current().split("x");
        int width = Integer.parseInt(str[0]);
        int height = Integer.parseInt(str[1]);

        try {
            Database.get().changeOption("resolution", resolutions.current());
            Database.get().changeOptionBoolean("fullscreen", fullscreen);
            gc.setDisplayMode(width, height, fullscreen);
        } catch (SlickException e) {
            System.out.println("Can't apply resolution: " + e.getMessage());
        } catch (LemmingsException e) {
            System.out.println("Can't save resolution: " + e.getMessage());
        }
    }

    public void init(GameContainer gc, StateBasedGame game)
        throws SlickException {
        this.game = game;
        resolutions = new Selector(getResolutions(gc));
        resolutions.select(Database.get().getOption("resolution", "640x480"));
        applyResolution((AppGameContainer) gc);
    }

    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        String text;
        for (int i = 0; i < items.length; i++) {
            text = items[i];
            if (text == "Map")
                text = "Map: < " + maps.current() + " >";
            if (text == "Resolution")
                text = "Resolution: < " + resolutions.current() + " >";
            if (text == "Fullscreen")
                text = "Fullscreen: < " + fullscreen + " >";
            View.drawCenteredText(gc, text, i, items.length,
                                  (i == itemSelected) ? Color.green : Color.white);
        }
    }

    public void update(GameContainer gc, StateBasedGame game, int delta) {
        Input input = gc.getInput();
        if (input.isKeyDown(Input.KEY_ENTER) &&
            items[itemSelected] == "Quit")
            gc.exit();

        if (shouldApplyResolution)
            applyResolution((AppGameContainer) gc);
    }

    /**
     * Handle a key press
     */
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_DOWN)
            itemSelected = Math.min(itemSelected+1, items.length-1);
        else if (key == Input.KEY_UP)
            itemSelected = Math.max(itemSelected-1, 0);

        if (items[itemSelected].equals("Play") && key == Input.KEY_ENTER)
            game.enterState(View.ID);
        else if (items[itemSelected].equals("Quit"))
            ; /* handled in update */
        else if (items[itemSelected].equals("Map")) {
            if (key == Input.KEY_LEFT)
                maps.previous();
            else if (key == Input.KEY_RIGHT)
                maps.next();
        }
        else if (items[itemSelected].equals("Highscores")) {
            if (key == Input.KEY_ENTER)
                game.enterState(Highscores.ID);
        }
        else if (items[itemSelected].equals("Resolution")) {
            if (key == Input.KEY_LEFT)
                resolutions.previous();
            else if (key == Input.KEY_RIGHT)
                resolutions.next();
        }
        else if (items[itemSelected].equals("Fullscreen")) {
            if (key == Input.KEY_LEFT || key == Input.KEY_RIGHT)
                fullscreen = !fullscreen;
        }
        else if (items[itemSelected].equals("Apply resolution")) {
            if (key == Input.KEY_ENTER)
                shouldApplyResolution = true;
        }
    }
}
