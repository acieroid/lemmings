package model;

import view.View;
import util.LemmingsException;

import java.util.ArrayList;
import java.util.LinkedList;

public class Model {
    private View view;
    private Map map;
    private ArrayList<Character> characters;
    private int lemmingsReleased, lemmingsToRelease;
    private int lemmingsRescued, lemmingsToRescue;

    public Model() {
        view = null;
        characters = new ArrayList<Character>();
        reset();
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
     * Add a character at a certain position
     * @param c: the haracter to be added
     * @return the character that has been added
     */
    public Character addCharacter(Character c)
        throws LemmingsException {
        c.setView(view);
        characters.add(c);
        view.characterAdded(c);
        return c;
    }

    /**
     * Delete a character
     */
    public void deleteCharacter(Character c) {
        characters.remove(characters.indexOf(c));
        view.characterChanged(c, Character.CHANGE_DELETED);
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
     * The player has won or lost
     */
    public void setFinished() {
        view.finished();
    }

    /**
     * Return true if the player has won, false if not
     */
    public boolean hasWon() {
        return !shouldReleaseLemming() && lemmingsRescued >= lemmingsToRescue;
    }

    /**
     * Return the number of lemmings to release
     */
    public int getLemmingsToRelease() {
        return lemmingsToRelease;
    }

    /**
     * Set the number of lemmings to release
     */
    public void setLemmingsToRelease(int n) {
        lemmingsToRelease = n;
    }


    /**
     * Return the number of lemmings released
     */
    public int getLemmingsReleased() {
        return lemmingsReleased;
    }

    /**
     * A lemming has been released
     */
    public void lemmingReleased() {
        lemmingsReleased++;
    }

    /**
     * @return true if we should still release lemmings
     */
    public boolean shouldReleaseLemming() {
        return lemmingsReleased < lemmingsToRelease;
    }

    /**
     * Return the number of lemmings to rescue
     */
    public int getLemmingsToRescue() {
        return lemmingsToRescue;
    }

    /**
     * Set the number of lemmings to rescue
     */
    public void setLemmingsToRescue(int n) {
        lemmingsToRescue = n;
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
}
