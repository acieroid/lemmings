package model;

import view.View;
import parser.SpriteFile;
import util.LemmingsException;

import java.util.Observable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Model extends Observable {
    private View view;
    private Map map;
    private ArrayList<Character> characters;
    private ResourceManager manager;
    private boolean running;

    public Model() {
        characters = new ArrayList<Character>();
        manager = new ResourceManager("../data/");
        running = false;
    }

    /**
     * Get the current map
     */
    public Map getMap() {
        return map;
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
        addObserver(v);
    }

    /**
     * Start the game with a certain map
     */
    public void start(String map)
        throws LemmingsException {
        running = true;
        this.map = manager.getMap(map);
        System.out.println("Loading walker");
        new SpriteFile("../data/characters/walker/left.sprite");
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
    }

    /**
     * Update the game
     */
    public void update() {
        /* TODO */
    }

    /**
     * @return the pause state of the model
     */
    public boolean isPaused() {
        return !running;
    }
}
    