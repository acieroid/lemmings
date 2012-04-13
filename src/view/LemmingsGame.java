package view;

import util.MapSelector;

import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.GameContainer;

public class LemmingsGame extends StateBasedGame {
    private View view;

    public LemmingsGame(View view) {
        super("Lemmings");
        this.view = view;
    }

    public void initStatesList(GameContainer container) {
        addState(new Menu(view.getMapSelector()));
        addState(view);
        addState(new LevelEnd(view.getModel()));
    }

    public View getView() {
        return view;
    }
}