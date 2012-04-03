package model;

import view.View;

import java.util.Observable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Model extends Observable {
    private Timer timer;
    private View view;
    private Map map;
    private ArrayList<Character> characters;
    private ResourceManager manager;

    public Model() {
        characters = new ArrayList<Character>();
        this.manager = new ResourceManager("../data/");
    }

    public Map getMap() {
        return map;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    /**
     * Set the view associated with this model
     */
    public void setView(View v) {
        view = v;
        addObserver(v);
    }

    /**
     * Start the game
     */
    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    update();
                }   
            }, 300, 100);
    }

    public void init() {
        map = manager.getMap("test");
    }

    /**
     * Update the game
     */
    public void update() {
        /* TODO */
    }
}
    