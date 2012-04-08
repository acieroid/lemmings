package view;

import model.Character;
import util.LemmingsException;
import parser.LispFile;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class CharacterAnimation extends Animation {
    private Character character;
    private LispFile definition;
    private LispFile sprite;
    private SpriteSheet sheet;

    public CharacterAnimation(Character c, String directory)
        throws LemmingsException {
        character = c;
        definition = new LispFile(directory + "/" +
                                  c.getName() + ".character");

        /* TODO: put direction in model.Character instead of
         * behavior and use it to determine sprite */

        sprite = new LispFile(directory + "/" +
                              definition.getStringProperty("right-sprite"));

        try {
            sheet = new SpriteSheet(directory + "/" +
                                    sprite.getStringProperty("image"),
                                    sprite.getNumberProperty("size", 0),
                                    sprite.getNumberProperty("size", 1));
        } catch (SlickException e) {
            throw new LemmingsException("view",
                                        "Can't load spritesheet for character '" +
                                        c.getName() + ": " + e.getMessage());
        }
        
        for (int i = 0; i < sprite.getNumberProperty("array", 0); i++) {
            addFrame(sheet.getSprite(i, 0), /* TODO: position */
                     sprite.getNumberProperty("speed"));
        }
        setAutoUpdate(true);
    }

    public int getX() {
        return character.getX();
    }

    public int getY() {
        return character.getY();
    }
}
