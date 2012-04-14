package view.gui;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;

public class Button {
    private static Image disabledBackground = null, enabledBackground = null;
    private Image image;
    private boolean enabled;

    public Button(String image)
        throws SlickException {
        if (disabledBackground == null || enabledBackground == null) {
            disabledBackground = new Image(GUI.RESOURCE_DIR + "/hbuttonbgb.png");
            enabledBackground = new Image(GUI.RESOURCE_DIR + "/hbuttonbg.png");
        }

        this.image = new Image(GUI.RESOURCE_DIR + "/" + image);
        enabled = false;
    }

    public void draw(int x, int y) {
        if (enabled)
            enabledBackground.draw(x, y);
        else
            disabledBackground.draw(x, y);

        image.draw(x, y);
    }
}