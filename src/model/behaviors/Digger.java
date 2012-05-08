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

public class Digger extends SimpleBehavior {
    private static int width, height;
    private static int[] collisionData = null;

    public Digger(Behavior b)
        throws LemmingsException {
        super(b);

        if (collisionData == null) {
            try {
                BufferedImage image = ImageIO.read(new File("../data/characters/digger/radius.png"));
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
        return "digger";
    }

    public  void update(Map map, long delta) {
        int x = getCharacter().getX();
        int y = getCharacter().getY();
        int w = getCharacter().getWidth();
        int h = getCharacter().getHeight();

                    getCharacter().setY(y+1);

        /* Can we dig ? */
        if (map.destroy(collisionData, x - (width/2 - w/2), y - (height - h) + 2,
                        width, height)) {
            getCharacter().setY(y+1);
        }
        else {
            getModel().changeBehavior(getCharacter(), "walker");
        }
    }
}

