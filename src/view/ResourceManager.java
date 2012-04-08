package view;

import util.LemmingsException;

import java.awt.Color;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Font;
import org.newdawn.slick.AngelCodeFont;

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
                                        e.getMessage());
        }
    }

    public Font getFont(String fontName)
        throws SlickException {
        return new AngelCodeFont(dir + "/" + fontName + ".fnt",
                                 dir + "/" + fontName + ".tga");
    }
}