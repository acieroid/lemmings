package model;

import view.View;
import util.LemmingsException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import java.io.Serializable;

public class Model implements Serializable {
    private static int MAXSPEED = 5;
    private static int LEMMING_RELEASE_TIMEOUT = 1000;

    private transient View view;
    private Map map;
    private ArrayList<Character> characters;
    private transient Timer timer;
    private int lemmingReleaseTimer = 0;

    private int lemmingsReleased, lemmingsRescued;
    private boolean running;
    private int speed;


    public Model() {
        speed = 1;
        running = false;

        view = null;
        characters = new ArrayList<Character>();

        createTimer();
        reset();
    }

    private void createTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                private long lastTime = new Date().getTime();
                public void run() {
                    long now = new Date().getTime();
                    update(speed*(now - lastTime));
                    lastTime = now;
                }
            }, 20, 20);
    }

    /**
     * Get the current map
     */
    public Map getMap() {
        return map;
    }

    /**
     * Set the current map
     */
    public void setMap(Map map)
        throws LemmingsException {
        reset();
        this.map = map;
        map.setView(view);
    }

    /**
     * Set the current map from its name
     */
    public void setMap(String name)
        throws LemmingsException {
        setMap(ResourceManager.getMap(name));
    }

    /**
     * Get all the characters
     */
    public ArrayList<Character> getCharacters() {
        return characters;
    }

    /**
     * Set the view associated with this model
     */
    public void setView(View v) {
        view = v;
    }

    /**
     * @return the view associated with this model
     */
    public View getView() {
        return view;
    }

    /**
     * Delete a character
     */
    public void deleteCharacter(Character c) {
        int index = characters.indexOf(c);
        if (index != -1) {
            characters.remove(index);
            view.characterChanged(c, Character.CHANGE_DELETED);
        }
    }

    /**
     * Delete all the characters from the model
     */
    public void clearCharacters() {
        characters.clear();
        if (view != null)
            view.charactersCleared();
    }

    /**
     * Reinitializes the state of the model
     */
    public void reset() {
        clearCharacters();
        lemmingsRescued = 0;
        lemmingsReleased = 0;
    }

    /**
     * Return true if the player has won, false if not
     */
    public boolean hasWon() {
        return !shouldReleaseLemming() && lemmingsRescued >= map.getLemmingsToRescue();
    }

    /**
     * Return the number of lemmings released
     */
    public int getLemmingsReleased() {
        return lemmingsReleased;
    }

    /**
     * @return true if we should still release lemmings
     */
    public boolean shouldReleaseLemming() {
        return lemmingsReleased < map.getLemmingsToRelease();
    }

    /**
     * Return the number of lemmings rescued
     */
    public int getLemmingsRescued() {
        return lemmingsRescued;
    }

    /**
     * A lemming has been rescued
     */
    public void lemmingRescued() {
        lemmingsRescued++;
    }

    /**
     * Release a lemming at the entrance
     */
    public void releaseLemming()
        throws LemmingsException {
        Character c = new Character(0, 0, 32, 32, "walker");
        c.setView(view);
        c.setX(map.getEntranceX() - c.getWidth()/2);
        c.setY(map.getEntranceY() - c.getHeight()/2);
        c.setFalling(true);
        c.setBehavior(new model.behaviors.Walker(this, c));
        lemmingsReleased++;
        characters.add(c);
        view.characterAdded(c);
    }

    /**
     * @return the pause state of the game
     */
    public boolean isPaused() {
        return !running;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = Math.max(1, Math.min(MAXSPEED, speed));
    }

    /**
     * Start the game with a certain map
     */
    public void start() {
        running = true;
    }

    /**
     * Pause/unpause the game
     */
    public void pause() {
        running = !running;
    }

    /**
     * Stop the game
     */
    public void stop() {
        running = false;
        clearCharacters();
    }

    /**
     * Update the game
     */
    public void update(long delta) {
        if (!isPaused()) {
            long now = new Date().getTime();
            lemmingReleaseTimer += delta;
            if (lemmingReleaseTimer > LEMMING_RELEASE_TIMEOUT &&
                shouldReleaseLemming()) {
                try {
                    releaseLemming();
                    lemmingReleaseTimer = 0;
                } catch (LemmingsException e) {
                    System.out.println("Error when releasing a lemming: " + e.getMessage());
                }
            }

            ArrayList<Character> cs;
            synchronized (characters) {
                /* Avoid having characters locked during all the calls
                 * to update. But maybe it isn't the best solution
                 * (TODO) */
                cs = new ArrayList<Character>(characters);
            }

            if (!shouldReleaseLemming() && cs.size() == 0) {
                /* Won or lost */
                stop();
                view.finished();
            }

            for (Character c : cs)
                /* TODO: use the delta */
                for (int i = 0; i < speed; i++)
                    if (c.getBehavior() != null)
                        c.getBehavior().update(map, delta);
                    else
                        System.out.println("Character has no behavior !!!");
        }
    }
}
