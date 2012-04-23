package view;

import util.LemmingsException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class MapImage extends Image{
    private Image foreground;
    
    public MapImage(String directory)
        throws SlickException {
        super(directory + "/background.png");
        this.foreground = new Image(directory + "/foreground.png");
    }

    public Image getBackground() {
        return this;
    }

    public Image getForeground() {
        return foreground;
    }

    /**
     * Destroys some pixels on the map
     */
    public void destroy(int x, int y, int w, int h) {
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
                destroyPixel(x+i, y+j);
    }

    /**
     * Destroy a zone of pixel on the map
     */
    public void destroy(int[] zone, int x, int y, int w, int h) {
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
                if (zone[j*w + i] != 0)
                    destroyPixel(x+i, y+j);
    }

    /**
     * Destroy a pixel on the map
     */
    public void destroyPixel(int x, int y) {
        /* TODO: change the texture */
    }
}
