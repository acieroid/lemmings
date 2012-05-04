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
        addState(menu);
        addState(view);
        addState(new LevelEnd(view));

        view.setMapSelector(menu.getMapSelector());
    }

    public View getView() {
        return view;
    }
}