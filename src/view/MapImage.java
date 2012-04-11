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

    public void drawBackground(int x, int y) {
        background.draw(x, y);
    }

    public void drawForeground(int x, int y) {
        foreground.draw(x, y);
    }
}
