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
    private int speed;

    public Controller() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    /* TODO: delta */
                    if (!isPaused())
                        update();
                }
            }, 300, 100);
        behaviors = new ArrayList<Behavior>();
        manager = new ResourceManager("../data");
        speed = 5;
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
        synchronized (behaviors) {
            behaviors.clear();
        }
        model.clearCharacters();
    }

    public void update() {
        if (running) {
            ArrayList<Behavior> bs;
            synchronized (behaviors) {
                /* Avoid having behaviors locked during all the calls
                 * to update. But maybe it isn't the best solution
                 * (TODO) */
                bs = new ArrayList<Behavior>(behaviors);
            }
            for (Behavior b : bs)
                for (int i = 0; i < speed; i++)
                    b.update(colMap);
        }
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
        this.speed = speed;
    }

    public void increaseSpeed() {
        setSpeed(getSpeed() + 1);
    }

    public void decreaseSpeed() {
        setSpeed(Math.max(getSpeed() - 1, 0));
    }

    public void mouseClicked(int x, int y)
        throws LemmingsException {

        Character c = model.addCharacter(colMap.getEntranceX(),
                                         colMap.getEntranceY(),
                                         "walker");
        c.setFalling(true);
        synchronized (behaviors) {
            behaviors.add(new controller.behaviors.Walker(this, c));
        }
    }

    public void characterSelected(Character c) {
        System.out.println("Character selected");
    }

    /**
     * Change the behavior of the character behaving as a to behave as
     * b
     */
    public void changeBehavior(Behavior a, Behavior b) {
        int index = behaviors.indexOf(a);
        if (index != -1) {
            /* Behavior hasn't been changed yet (it might be because
             * of successive calls to a's update */
            behaviors.remove(index);
            behaviors.add(b);
        }
    }
}