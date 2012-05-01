package model.behaviors;

import model.Model;
import model.Character;
import model.Behavior;
import model.Map;

import java.util.Timer;
import java.util.TimerTask;

public class Exit extends SimpleBehavior {
    private Timer timer;
    private static int TIMEOUT = 500;

    public Exit(Behavior b) {
        super(b);
        timer = new Timer();
        timer.schedule(new TimerTask() {
                public void run() {
                    destroy();
                }
            }, TIMEOUT);
    }

    public String getName() {
        return "exit";
    }

    public void update(Map map) {
        /* don't move nor fall */
    }

    public void destroy() {
        getModel().deleteCharacter(getCharacter());
        getModel().lemmingRescued();
    }
}
