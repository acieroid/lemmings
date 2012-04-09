package view;

import util.LemmingsException;
import model.Character;

import java.awt.Color;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Font;
import org.newdawn.slick.AngelCodeFont;

class ResourceManager {
    private String dir;

    public ResourceManager(String directory) {
        this.dir = directory;
    }

    public MapImage getMap(String name)
        throws LemmingsException {
        try {
            return new MapImage(dir + "/maps/" + name + "/image.png");
        } catch (SlickException e) {
            throw new LemmingsException("view/rm",
                                        "Can't load map '" + name + "': " +
                                        e.getMessage());
        }
    }

    public Font getFont(String fontName)
        throws LemmingsException {
        try {
            return new AngelCodeFont(dir + "/" + fontName + ".fnt",
                                     dir + "/" + fontName + ".tga");
        } catch (SlickException e) {
            throw new LemmingsException("view/rm",
                                        "Can't load font '" + fontName + "': " +
                                        e.getMessage());
        }
    }

    public CharacterAnimation getAnimation(Character c)
        throws LemmingsException {
        return new CharacterAnimation(c, dir, c.getName());
    }
}