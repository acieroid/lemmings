package model.behaviors;

import model.Model;
import model.Character;
import model.Behavior;
import model.Map;
import util.LemmingsException;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Date;

public class Bomber extends SimpleBehavior {
    private static int TIMEOUT_EXPLODE = 850;
    private static int TIMEOUT_DESTROY = 910;
    private int timer;
    private boolean destroyed, exploded;

    private static int width, height;
    private static int[] collisionData = null;

    public Bomber(Behavior b)
        throws LemmingsException {
        super(b);
        getCharacter().setDirection(Character.DONT_MOVE);
        timer = 0;
        destroyed = false;
        exploded = false;
        /* TODO: isolate this in the resource manager, or in a parent class ? */
        if (collisionData == null) {
            try {
                BufferedImage image = ImageIO.read(new File("../data/characters/bomber/radius.png"));
                width = image.getWidth();
                height = image.getHeight();
                collisionData = new int[width*height];
                image.getRGB(0, 0, width, height, collisionData, 0, width);
            } catch (java.io.IOException e) {
                throw new LemmingsException("model",
                                            "Can't load radius for bomber");
            }
        }
    }

    public String getName() {
        return "bomber";
    }

    public void update(Map map, long delta) {
        fall(map, delta, false);

        if (!destroyed) {
            timer += delta;

            if (timer > TIMEOUT_EXPLODE && !exploded)
                explode();
            if (timer > TIMEOUT_DESTROY)
                destroy();
        }
    }

    public void explode() {
        int x = getCharacter().getX();
        int y = getCharacter().getY();
        int w = getCharacter().getWidth();
        int h = getCharacter().getHeight();
        exploded = true;
        getModel().getMap().destroy(collisionData,
                                    x - (width/2 - w/2),
                                    y - (height/2 - h/2),
                                    width,
                                    height);
    }

    public void destroy() {
        destroyed = true;
        getModel().deleteCharacter(getCharacter());
    }
}
