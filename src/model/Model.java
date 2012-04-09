package model;

import view.View;
import util.LemmingsException;

import java.util.ArrayList;
import java.util.LinkedList;

public class Model {
    private View view;
    private Map map;
    private ArrayList<Character> characters;
    private ResourceManager manager;

    public Model() {
        characters = new ArrayList<Character>();
        manager = new ResourceManager("../data");
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
    public void setMap(String map)
        throws LemmingsException {
        this.map = manager.getMap(map);
    }

    /**
     * Get all the characters
     */
    public ArrayList<Character> getCharacters() {
        return characters;
    }

    /**
     * Return a list of all the maps' names
     */
    public LinkedList<String> getAllMaps() {
        return manager.getAllMaps();
    }

    /**
     * Set the view associated with this model
     */
    public void setView(View v) {
        view = v;
    }

    /**
     * Add a character at a certain position
     * @param x: the x value of the character position
     * @param y: the y value of the character position
     * @param name: the name of the type of character
     * @return the character that has been added
     */
    public Character addCharacter(int x, int y, String name)
        throws LemmingsException {
        Character c = manager.getCharacter(x, y, name);
        characters.add(c);
        c.setView(view);
        view.characterAdded(c);
        return c;
    }

    /**
     * Delete all the characters from the model
     */
    public void clearCharacters() {
        characters.clear();
        view.charactersCleared();
    }
}