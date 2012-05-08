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
    private LispFile sprite;
    private SpriteSheet sheet;
    private String resourceDirectory, directory;
    private boolean changed;
    private int speed = 1;

    private String anim;

    public CharacterAnimation(Character c, String directory, String animation)
        throws LemmingsException {
        character = c;
        resourceDirectory = directory;
        setAnimation(animation);
        loadFromDefinition();
    }

    private String getStrDirection() {
        if (character.getDirection() == Character.LEFT)
            return "left";
        else
            return "right";
    }

    /* TODO: take the spritesheet as argument (will improve
     * performance and memory consumption) */
    public synchronized void setAnimation(String animation) {
        anim = animation;
        directory = resourceDirectory + "/characters/" + animation;
        changed = true;
    }

    public void changeDirection() {
        changed = true;
    }

    private synchronized void loadFromDefinition()
        throws LemmingsException {
        animation = new Animation();

        String direction = character.getDirection() == Character.RIGHT ?
            "right-sprite" : "left-sprite";
        sprite = new LispFile(directory + "/" +
                              getStrDirection() + ".sprite");

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
        animation.setSpeed(speed);
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

    public int getWidth() {
        return character.getWidth();
    }

    public int getHeight() {
        return character.getHeight();
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

    public boolean isStopped() {
        return animation.isStopped();
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        animation.setSpeed(speed);
    }
}
