package view;

import util.LemmingsException;

import java.awt.Font;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

class ResourceManager {
    private String dir;

    public ResourceManager(String directory) {
        this.dir = directory;
    }

    public Map getMap(String name)
        throws LemmingsException {
        try {
            return new Map(dir + "/maps/" + name + "/image.png");
        } catch (SlickException e) {
            throw new LemmingsException("view/rm",
                                        "Can't load map '" + name + "': " +
                                        e.toString());
        }
    }

    public TrueTypeFont getTTF(String fontName, int spec, int size) {
        return new TrueTypeFont(new Font(dir + "/" + fontName, spec, size), true);
    }
}