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
    private boolean running, nuked;
    private int speed;


    public Model() {
        speed = 1;
        running = false;

        view = null;
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
    public void setView(View view) {
        this.view = view;
        if (map != null)
            map.setView(view);
        for (Character c : characters)
            c.setView(view);
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
        c.setView(view);
        c.setX(map.getEntranceX() - c.getWidth()/2);
        c.setY(map.getEntranceY() - c.getHeight()/2);
        c.setBehavior(new model.behaviors.Walker(this, c));
        c.setFalling(true);
        c.setName("faller");
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
        getView().speedChanged(speed);
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

            if (!shouldReleaseLemming() && characters.size() == 0) {
                /* Won or lost */
                stop();
                view.finished();
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
     * @TODO: have a little timeout to avoid closing harshly
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
     * @TODO: behavior manager + can't do something like faller -> blocker
     */
    public void changeBehavior(Character c, String behavior) {
        Behavior b = null;
        try {
            b = (Behavior)
                Class.forName("model.behaviors." +
                              behavior.substring(0, 1).toUpperCase() +
                              behavior.substring(1))
                .getDeclaredConstructors()[0]
                .newInstance(c.getBehavior());
            c.setBehavior(b);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InstantiationException e) {
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        } catch (java.lang.reflect.InvocationTargetException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Return the list of available character behaviors
     */
    public String[] getCharacterBehaviors() {
        String[] res = { "bomber", "blocker" };
        return res;
    }
}
