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

    private static int MAX_NAME_LENGTH = 30;

    private Model model;
    private Font font;
    private String name;

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
        name = "";
    }

    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        String[] text = {"Lemmings rescued: " +
                         model.getLemmingsRescued() + "/" +
                         model.getLemmingsToRescue(),
                         model.hasWon() ? "Congratulations, you won!" : "Too bad, you lost.",
                         "Type your name to register your score and then press Enter",
                         "or press escape to return to the main menu.",
                         "Your name: " + name};
        for (int i = 0; i < text.length; i++)
            drawCenteredText(gc, text[i], i, text.length, Color.white);
    }

    public void update(GameContainer gc, StateBasedGame game, int delta) {
    }

    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ENTER) {
            name = "";
            game.enterState(Menu.ID);
        }
        else if (key == Input.KEY_ESCAPE) {
            game.enterState(Menu.ID);
        }
        else if (key == Input.KEY_BACK) {
            name = name.substring(0, name.length() - 1);
        }
        else if (name.length() < MAX_NAME_LENGTH) {
            name += c;
        }
    }
}