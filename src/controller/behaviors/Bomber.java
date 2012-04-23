package controller.behaviors;

import model.Character;
import controller.Controller;
import controller.Behavior;
import controller.CollisionMap;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Timer;
import java.util.TimerTask;

public class Bomber extends SimpleBehavior {
    private static int TIMEOUT_EXPLODE = 900;
    private static int TIMEOUT_DESTROY = 1200;
    private Timer timer;
    private CollisionMap colMap;

    public Bomber(Behavior b) {
        super(b);
        getCharacter().setDirection(Character.DONT_MOVE);
        timer = new Timer();
        timer.schedule(new TimerTask() {
                public void run() {
                    explode();
                }
            }, TIMEOUT_EXPLODE);
        timer.schedule(new TimerTask() {
                public void run() {
                    destroy();
                }
            }, TIMEOUT_DESTROY);
    }

    public String getName() {
        return "bomber";
    }

    public void update(CollisionMap colMap) {
        super.update(colMap);
        this.colMap = colMap;
    }

    public void explode() {
        //colMap.destroy(
        /* TODO: the colMap should in fact be something in the model */
    }

    public void destroy() {
        getController().deleteBehavior(this);
    }
}