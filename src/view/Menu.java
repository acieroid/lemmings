package view;

import util.MapSelector;

import java.util.LinkedList;
import java.util.ListIterator;

import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.*;
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

    private int itemSelected;
    private String[] items = {"Play", "Map", "Quit"};

    public Menu(MapSelector maps) {
        this.maps = maps;
        this.font = font;
    }

    public int getID() {
        return ID;
    }

    private void drawCenteredText(GameContainer gc, String text,
                                  int line, int nLines, Color color) {
        int spacing = font.getLineHeight()-5;
        int start = gc.getHeight()/2 - (spacing+font.getLineHeight())*nLines/2;
        int x = gc.getWidth()/2 - font.getWidth(text)/2;
        int y = start + line*(spacing+font.getLineHeight());
        font.drawString(x, y, text, color);
    }

    public void init(GameContainer gc, StateBasedGame game)
        throws SlickException {
        this.game = game;
        font = gc.getGraphics().getFont();
    }

    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        String text;
        for (int i = 0; i < items.length; i++) {
            text = items[i];
            if (text.equals("Map"))
                text = "Map : < " + maps.current() + " >";
            drawCenteredText(gc, text, i, items.length,
                             (i == itemSelected) ? Color.green : Color.white);
        }
    }

    public void update(GameContainer gc, StateBasedGame game, int delta) {
        Input input = gc.getInput();
        if (input.isKeyDown(Input.KEY_ENTER) &&
            items[itemSelected].equals("Quit"))
            gc.exit();
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
    }
}
