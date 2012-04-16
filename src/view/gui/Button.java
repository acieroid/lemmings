package view.gui;

import util.LemmingsException;
import parser.LispFile;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class Button {
    private static Image disabledBackground = null, enabledBackground = null;
    private Image image;
    private Animation animation;
    private boolean enabled, animated;
    private int x, y;
    private ButtonBehavior behavior;
    private LispFile sprite;
    private SpriteSheet sheet;

    Button(int x, int y, ButtonBehavior b)
        throws SlickException {
        if (disabledBackground == null || enabledBackground == null) {
            disabledBackground = new Image(GUI.RESOURCE_DIR + "/hbuttonbgb.png");
            enabledBackground = new Image(GUI.RESOURCE_DIR + "/hbuttonbg.png");
        }

        this.x = x;
        this.y = y;
        behavior = b;
        behavior.setButton(this);
        enabled = false;
    }

    public Button(String image, int x, int y, ButtonBehavior b, boolean animated)
        throws SlickException, LemmingsException {
        this(x, y, b);
        this.animated = animated;
        if (!animated) {
            this.image = new Image(GUI.RESOURCE_DIR + "/" + image);
        }
        else {
            /* TODO: isolate this code somewhere else ? In the resource manager ? */
            sprite = new LispFile(GUI.RESOURCE_DIR + "/" + image);
            sheet = new SpriteSheet(GUI.RESOURCE_DIR + "/" +
                                    sprite.getStringProperty("image"),
                                    sprite.getNumberProperty("size", 0),
                                    sprite.getNumberProperty("size", 1));
            int sizeY = sprite.getNumberProperty("size", 1);
            int positionYoffset = sprite.getNumberProperty("position", 1, 0);
            animation = new Animation();
            for (int i = 0; i < sprite.getNumberProperty("array", 0); i++)
                animation.addFrame(sheet.getSprite(i, positionYoffset/sizeY),
                                   sprite.getNumberProperty("speed"));

            animation.setLooping(sprite.getBooleanProperty("loop"));
            animation.setAutoUpdate(true);
            animation.stop();
        }
    }

    public Button(String image, int x, int y, ButtonBehavior b)
        throws SlickException, LemmingsException {
        this(image, x, y, b, false);
    }

    public void draw() {
        if (enabled)
            enabledBackground.draw(x, y);
        else
            disabledBackground.draw(x, y);

        if (animated)
            animation.draw(x, y);
        else
            image.draw(x, y);
    }

    public boolean contains(int x, int y) {
        return (x >= this.x && x <= this.x + enabledBackground.getWidth() &&
                y >= this.y && y <= this.y + enabledBackground.getHeight());
    }

    public void enable() {
        enabled = true;
        if (animated)
            animation.start();
    }

    public void disable() {
        enabled = false;
        if (animated)
            animation.stop();
    }

    public void pressed() {
        behavior.pressed();
    }

    public void released() {
        behavior.released();
    }
}