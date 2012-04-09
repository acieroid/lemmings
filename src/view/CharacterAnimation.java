package view;

import model.Character;
import util.LemmingsException;
import parser.LispFile;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class CharacterAnimation {
    private Animation animation;
    private Character character;
    private LispFile definition;
    private LispFile sprite;
    private SpriteSheet sheet;
    private String resourceDirectory, directory;
    private boolean changed;

    private String anim;

    public CharacterAnimation(Character c, String directory, String animation)
        throws LemmingsException {
        character = c;
        resourceDirectory = directory;
        setAnimation(animation);
        loadFromDefinition();
    }

    /* TODO: take the spritesheet as argument (will improve
     * performance and memory consumption) */
    public synchronized void setAnimation(String animation)
        throws LemmingsException {
        anim = animation;
        directory = resourceDirectory + "/characters/" + animation;
        System.out.println("Setting animation: " + animation + ", directory: " + directory);
        definition = new LispFile(directory + "/" +
                                  animation + ".character");

        changed = true;
    }

    public void changeDirection()
        throws LemmingsException {
        changed = true;
    }

    private synchronized void loadFromDefinition()
        throws LemmingsException {
        animation = new Animation();

        String direction = character.getDirection() == Character.RIGHT ?
            "right-sprite" : "left-sprite";
        sprite = new LispFile(directory + "/" +
                              definition.getStringProperty(direction));

        try {
            sheet = new SpriteSheet(directory + "/" +
                                    sprite.getStringProperty("image"),
                                    sprite.getNumberProperty("size", 0),
                                    sprite.getNumberProperty("size", 1));
        } catch (SlickException e) {
            throw new LemmingsException("view",
                                        "Can't load spritesheet for character '" +
                                        character.getName() +
                                        ": " + e.getMessage());
        }

        int sizeY = sprite.getNumberProperty("size", 1);
        int positionYoffset = sprite.getNumberProperty("position", 1, 0);
        for (int i = 0; i < sprite.getNumberProperty("array", 0); i++) {
            animation.addFrame(sheet.getSprite(i, positionYoffset/sizeY),
                               sprite.getNumberProperty("speed"));
        }
        animation.setLooping(sprite.getBooleanProperty("loop"));
        animation.setAutoUpdate(true);
        changed = false;
    }

    public void checkIfChanged()
        throws LemmingsException {
        if (changed)
            loadFromDefinition();
    }

    public int getX() {
        return character.getX();
    }

    public int getY() {
        return character.getY();
    }

    public Character getCharacter() {
        return character;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void stop() {
        animation.stop();
    }

    public void start() {
        animation.start();
    }
}
