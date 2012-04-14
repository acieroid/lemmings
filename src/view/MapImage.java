package view;

import util.LemmingsException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class MapImage {
    private Image background, foreground;
    
    public MapImage(String directory)
        throws SlickException {
        this.background = new Image(directory + "/background.png");
        this.foreground = new Image(directory + "/foreground.png");
    }

    public Image getBackground() {
        return background;
    }

    public Image getForeground() {
        return foreground;
    }
}
