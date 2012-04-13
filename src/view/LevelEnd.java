package view;

import model.Model;

import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

public class LevelEnd extends BasicGameState {
    public static final int ID = 3;
    private StateBasedGame game;

    private Model model;
    private Font font;


    public LevelEnd(Model model) {
        this.model = model;
    }

    public int getID() {
        return ID;
    }

    /* TODO: put this method somewhere appropriated instead of
     * copy-pasting it everywhere */
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
        drawCenteredText(gc, "Lemmings rescued: " +
                         model.getLemmingsRescued() + "/" +
                         model.getLemmingsToRescue(), 0, 3, Color.white);
        if (model.hasWon())
            text = "Congratulations, you won!";
        else
            text = "Too bad, you lost.";
        drawCenteredText(gc, text, 1, 3, Color.white);
        drawCenteredText(gc, "Press Enter to return to the main menu", 2, 3, Color.white);
    }

    public void update(GameContainer gc, StateBasedGame game, int delta) {
    }

    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ENTER)
            game.enterState(Menu.ID);
    }
}