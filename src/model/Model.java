package model;

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
    private static int END_OF_GAME_TIMEOUT = 1000;

    private transient ArrayList<ModelObserver> observers;
    private Map map;
    private ArrayList<Character> characters;
    private transient Timer timer;
    private int lemmingReleaseTimer = 0, endTimer = 0;

    private int lemmingsReleased, lemmingsRescued;
    private boolean running, nuked;
    private int speed;


    public Model() {
        speed = 1;
        running = false;
        observers = new ArrayList<ModelObserver>();
        characters = new ArrayList<Character>();
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
        endTimer = 0;
        lemmingReleaseTimer = 0;
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
        map.setModel(this);
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
     * Add an observer to this model
     */
    public void addObserver(ModelObserver obs) {
        if (observers == null)
            observers = new ArrayList<ModelObserver>();
        observers.add(obs);
        obs.setModel(this);
    }

    /**
     * Delete a character
     */
    public void deleteCharacter(Character c) {
        int index = characters.indexOf(c);
        if (index != -1) {
            characters.remove(index);
            notifyCharacterChanged(c, Character.CHANGE_DELETED);
        }
    }

    /**
     * Delete all the characters from the model
     */
    public void clearCharacters() {
        characters.clear();
        for (ModelObserver obs : observers)
            obs.charactersCleared();
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
        return !nuked && lemmingsReleased < map.getLemmingsToRelease();
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
        c.setModel(this);
        c.setX(map.getEntranceX() - c.getWidth()/2);
        c.setY(map.getEntranceY() - c.getHeight()/2);
        c.setBehavior(new model.behaviors.Walker(this, c));
        c.setFalling(true);
        c.setName("faller");
        lemmingsReleased++;
        characters.add(c);
        for (ModelObserver obs : observers)
            obs.characterAdded(c);
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
        for (ModelObserver obs : observers)
            obs.speedChanged();
    }

    /**
     * Start the game with a certain map
     */
    public void start() {
        speed = 1;
        running = true;
        nuked = false;
        createTimer();
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
        if (timer != null) {
          timer.cancel();
          timer = null;
        }
    }

    /**
     * Update the game
     */
    public void update(long delta) {
        if (!isPaused()) {
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

            if (!shouldReleaseLemming() && characters.size() == 0) {
                endTimer += delta;
            }
            if (endTimer > END_OF_GAME_TIMEOUT) {
                /* Won or lost */
                stop();
                for (ModelObserver obs : observers)
                    obs.finished();
            }

            for (int i = 0; i < characters.size(); i++) {
                Character c = characters.get(i);
                /* TODO: use the delta */
                for (int j = 0; j < speed; j++)
                    if (c.getBehavior() != null)
                        c.getBehavior().update(map, delta);
            }
        }
    }

    /**
     * Nuke the game: kill all the characters and stop releasing them
     */
    public void nuke()
        throws LemmingsException {
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);
            c.setBehavior(new model.behaviors.Bomber(c.getBehavior()));
        }
        nuked = true;
    }

    /**
     * Change the behavior of a lemming
     * @TODO: can't do something like faller -> blocker
     */
    public void changeBehavior(Character c, String behavior) {
        Behavior b = null;
        try {
            Class types[] = { Class.forName("model.Behavior") };
            b = (Behavior)
                Class.forName("model.behaviors." +
                              behavior.substring(0, 1).toUpperCase() +
                              behavior.substring(1))
                .getConstructor(types)
                .newInstance(c.getBehavior());
            c.setBehavior(b);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (java.lang.reflect.InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the list of available character behaviors
     */
    public String[] getCharacterBehaviors() {
        String[] res = { "bomber", "blocker", "digger" };
        return res;
    }

    /**
     * Notify the observers of changes in a character
     */
    public void notifyCharacterChanged(Character character, int change) {
        for (ModelObserver obs : observers)
            obs.characterChanged(character, change);
    }

    /**
     * Notify the observers a zone is destroyed
     */
    public void notifyDestroyed(int x, int y, int w, int h) {
        for (ModelObserver obs : observers)
            obs.destroyed(x, y, w, h);
    }

    public void notifyDestroyed(int zone[], int x, int y, int w, int h) {
        for (ModelObserver obs : observers)
            obs.destroyed(zone, x, y, w, h);
    }
}
