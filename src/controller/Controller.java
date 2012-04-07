package controller;

import model.Model;
import model.Character;
import util.LemmingsException;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class Controller {
    private Model model;
    private Timer timer;
    private boolean running;
    private ArrayList<Behavior> behaviors;
    private CollisionMap colMap;
    private ResourceManager manager;

    public Controller() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    if (!isPaused())
                        update();
                }
            }, 300, 100);
        behaviors = new ArrayList<Behavior>();
        manager = new ResourceManager("../data");
    }

    public void setModel(Model m) {
        model = m;
    }

    /**
     * Start the game with a certain map
     */
    public void start(String map)
        throws LemmingsException {
        running = true;
        colMap = manager.getCollisionMap(map);
        model.setMap(map);
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

    public void update() {
        if (running) {
            for (Behavior b : behaviors)
                b.update(colMap);
        }
    }

    /**
     * @return the pause state of the game
     */
    public boolean isPaused() {
        return !running;
    }

    public void mouseClicked(int x, int y)
        throws LemmingsException {
        System.out.println("Mouse clicked");
        Character c = model.addCharacter(x, y, "walker");
        behaviors.add(new controller.behaviors.Walker(c));
    }
}