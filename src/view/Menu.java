package view;

import util.MapSelector;

import java.util.LinkedList;
import java.util.ListIterator;

import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

public class Menu extends BasicGameState {
    public static final int ID = 1;
    private StateBasedGame game;

    private boolean activated;
    private Font font;

    private MapSelector maps;

    public Menu(MapSelector maps) {
        this.maps = maps;
        this.font = font;
    }

    public int getID() {
        return ID;
    }

    private void drawCenteredText(GameContainer gc, String text) {
        font.drawString(gc.getWidth()/2 - font.getWidth(text)/2,
                        gc.getHeight()/2 - font.getLineHeight()/2,
                        text, Color.white);
    }

    public void init(GameContainer c, StateBasedGame game)
        throws SlickException {
        this.game = game;
        font = c.getGraphics().getFont();
    }

    public void render(GameContainer c, StateBasedGame game, Graphics g) {
        drawCenteredText(c, "< " + maps.current() + " >");
    }

    public void update(GameContainer c, StateBasedGame game, int delta) {
    }

    /**
     * Handle a key press
     */
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_LEFT)
            maps.previous();
        else if (key == Input.KEY_RIGHT)
            maps.next();
        else if (key == Input.KEY_ENTER)
            game.enterState(View.ID);
    }
}
