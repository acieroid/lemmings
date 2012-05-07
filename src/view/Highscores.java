package view;

import util.Database;
import util.Score;
import util.Selector;
import util.LemmingsException;

import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public class Highscores extends BasicGameState {
    public static final int ID = 4;
    private StateBasedGame game;

    private Selector maps;
    private String map;

    public Highscores() {
    }

    public int getID() {
        return ID;
    }

    public void setMapSelector(Selector maps) {
        this.maps = maps;
    }

    public void init(GameContainer gc, StateBasedGame game) {
        this.game = game;
    }

    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        try {
            ArrayList<Score> scores = Database.getScores(maps.current());
            int nLines = Math.min(scores.size() + 1, 6);

            View.drawCenteredText(gc,
                                  "High scores for map '" + maps.current() + "'",
                                  0, nLines, Color.white);
            for (int i = 1; i < nLines; i++) {
                Score s = scores.get(i-1);
                View.drawCenteredText(gc,
                                      s.getName() + ": " + s.getScore(),
                                      i, nLines, Color.white);
            }
        } catch (LemmingsException e) {
            View.drawCenteredText(gc, "Error: " + e.getMessage());
        }
    }

    public void update(GameContainer gc, StateBasedGame game, int delta) {
    }

    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ENTER || key == Input.KEY_ESCAPE)
            game.enterState(Menu.ID);
    }
}
