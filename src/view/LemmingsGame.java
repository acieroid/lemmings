package view;

import util.Selector;

import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.GameContainer;

public class LemmingsGame extends StateBasedGame {
    private View view;

    public LemmingsGame(View view) {
        super("Lemmings");
        this.view = view;
    }

    public void initStatesList(GameContainer container) {
        Menu menu = new Menu();
        Highscores highscores = new Highscores();
        addState(menu);
        addState(view);
        addState(new LevelEnd(view));
        addState(highscores);

        view.setMapSelector(menu.getMapSelector());
        highscores.setMapSelector(menu.getMapSelector());
    }

    public View getView() {
        return view;
    }
}