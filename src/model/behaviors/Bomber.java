package model.behaviors;

import model.Model;
import model.Character;
import model.Behavior;
import model.Map;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class Bomber extends SimpleBehavior {
    private static int TIMEOUT_EXPLODE = 900;
    private static int TIMEOUT_DESTROY = 1200;
    private Timer timer;

    private int width, height;
    private int[] collisionData;

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
        /* TODO: only load it once ? And use the resource manager */
        try {
            BufferedImage image = ImageIO.read(new File("../data/characters/bomber/radius.png"));
            width = image.getWidth();
            height = image.getHeight();
            collisionData = new int[width*height];
            image.getRGB(0, 0, width, height, collisionData, 0, width);
        } catch (java.io.IOException e) {
            /* TODO */
            /*throw new LemmingsException("model",
              "Can't load radius for bomber");*/
        }
    }

    public String getName() {
        return "bomber";
    }

    public void explode() {
        int x = getCharacter().getX();
        int y = getCharacter().getY();
        int w = getCharacter().getWidth();
        int h = getCharacter().getHeight();
        getModel().getMap().destroy(collisionData,
                                    x - (width/2 - w/2),
                                    y - (width/2 - w/2),
                                    width,
                                    height);
    }

    public void destroy() {
        getModel().deleteCharacter(getCharacter());
    }
}