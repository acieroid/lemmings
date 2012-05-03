package model.behaviors;

import model.Model;
import model.Character;
import model.Behavior;
import model.Map;

import java.util.Date;

public class Exit extends SimpleBehavior {
    private static int TIMEOUT = 500;
    private int timer;
    private boolean destroyed;

    public Exit(Behavior b) {
        super(b);
        timer = 0;
        destroyed = false;
    }

    public String getName() {
        return "exit";
    }

    public void update(Map map, long delta) {
        /* don't move nor fall, so don't call super.update */
        timer += delta;
        if (!destroyed && timer > TIMEOUT)
            destroy();
    }

    public void destroy() {
        destroyed = true;
        getModel().deleteCharacter(getCharacter());
        getModel().lemmingRescued();
    }
}
