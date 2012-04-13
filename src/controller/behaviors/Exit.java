package controller.behaviors;

import model.Character;
import controller.Controller;
import controller.Behavior;
import controller.CollisionMap;

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

    public void update(CollisionMap map) {
        /* don't move */
    }

    public void destroy() {
        getController().deleteBehavior(this);
    }
}
