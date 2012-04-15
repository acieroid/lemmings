package view.gui;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;

public class Button {
    private static Image disabledBackground = null, enabledBackground = null;
    private Image image;
    private boolean enabled;
    private int x, y;
    private ButtonBehavior behavior;

    public Button(String image, int x, int y, ButtonBehavior b)
        throws SlickException {
        if (disabledBackground == null || enabledBackground == null) {
            disabledBackground = new Image(GUI.RESOURCE_DIR + "/hbuttonbgb.png");
            enabledBackground = new Image(GUI.RESOURCE_DIR + "/hbuttonbg.png");
        }

        this.image = new Image(GUI.RESOURCE_DIR + "/" + image);
        this.x = x;
        this.y = y;
        behavior = b;
        behavior.setButton(this);
        enabled = false;
    }

    public void draw() {
        if (enabled)
            enabledBackground.draw(x, y);
        else
            disabledBackground.draw(x, y);

        image.draw(x, y);
    }

    public boolean contains(int x, int y) {
        return (x >= this.x && x <= this.x + enabledBackground.getWidth() &&
                y >= this.y && y <= this.y + enabledBackground.getHeight());
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public void pressed() {
        behavior.pressed();
    }

    public void released() {
        behavior.released();
    }
}