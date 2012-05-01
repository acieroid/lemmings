package view;

import util.LemmingsException;
import model.Character;

import java.awt.Color;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Font;
import org.newdawn.slick.AngelCodeFont;

class GraphicsResourceManager {
    private static String dir = "../data/";

    public static MapImage getMap(String name)
        throws LemmingsException {
        try {
            return new MapImage(dir + "/maps/" + name + "/");
        } catch (SlickException e) {
            throw new LemmingsException("view/rm",
                                        "Can't load map '" + name + "': " +
                                        e.getMessage());
        }
    }

    public static Font getFont(String fontName)
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

    public static CharacterAnimation getAnimation(Character c)
        throws LemmingsException {
        return new CharacterAnimation(c, dir, c.getName());
    }
}