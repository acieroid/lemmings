package view;

import model.Model;
import util.Database;
import util.LemmingsException;

import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

public class LevelEnd extends BasicGameState {
    public static final int ID = 3;
    private StateBasedGame game;

    private static int MAX_NAME_LENGTH = 30;

    private View view;
    private String name;

    public LevelEnd(View view) {
        this.view = view;
    }

    public int getID() {
        return ID;
    }

    public void init(GameContainer gc, StateBasedGame game)
        throws SlickException {
        this.game = game;
        name = "";
    }

    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        String[] text = {"Lemmings rescued: " +
                         view.getModel().getLemmingsRescued() + "/" +
                         view.getModel().getMap().getLemmingsToRescue(),
                         view.getModel().hasWon() ? "Congratulations, you won!" : "Too bad, you lost.",
                         "Type your name to register your score and then press Enter",
                         "or press escape to return to the main menu.",
                         "Your name: " + name};
        for (int i = 0; i < text.length; i++)
            View.drawCenteredText(gc, text[i], i, text.length, Color.white);
    }

    public void update(GameContainer gc, StateBasedGame game, int delta) {
    }

    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ENTER) {
            try {
                Database.addScore(view.getModel().getMap().getName(),
                                        name, view.getModel().getLemmingsRescued());
            } catch (LemmingsException e) {
                System.out.println("Error when saving score: " + e.getMessage());
            }
            name = "";
            game.enterState(Menu.ID);
        }
        else if (key == Input.KEY_ESCAPE) {
            game.enterState(Menu.ID);
        }
        else if (key == Input.KEY_BACK && name.length() > 0) {
            name = name.substring(0, name.length() - 1);
        }
        else if (name.length() < MAX_NAME_LENGTH && key != 42) {
            name += c;
        }
    }
}