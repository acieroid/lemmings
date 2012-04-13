package controller;

import model.Model;
import model.Character;
import util.LemmingsException;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class Controller {
    private Model model;
    private Timer timer, lemmingsTimer;
    private boolean running;
    private ArrayList<Behavior> behaviors;
    private CollisionMap colMap;
    private ResourceManager manager;
    private int speed;
    private static int MAXSPEED = 5;
    private int lemmingsToRelease; /* TODO: put that in the map definition file ? */

    public Controller() {
        speed = 1;
        running = false;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                private int i = 0;
                public void run() {
                    if (!isPaused()) {
                        if (i % (MAXSPEED/speed) == 0)
                            update();
                        i++;
                    }
                }
            }, 20, 20);
        lemmingsTimer = new Timer();
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
        lemmingsToRelease = 20;
        /* release a lemming each 2 seconds */
        /* TODO: provide an interactive way to change the release interval */
        /* TODO: speed *should* interacts with this timer too */
        lemmingsTimer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    if (!isPaused() && lemmingsToRelease > 0) {
                        try {
                            releaseLemming();
                        } catch (LemmingsException e) {
                            System.out.println("Error when releasing a lemming: " +
                                               e.getMessage());
                        }
                        lemmingsToRelease--;
                    }
                }
            }, 2000, 2000);
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
        if (speed <= 0)
            this.speed = 1;
        else if (speed > MAXSPEED)
            this.speed = MAXSPEED;
        else
            this.speed = speed;
    }

    public void increaseSpeed() {
        setSpeed(getSpeed() + 1);
    }

    public void decreaseSpeed() {
        setSpeed(getSpeed() - 1);
    }

    public void mouseClicked(int x, int y) {
    }

    /**
     * Release a lemming at the entrance
     */
    public void releaseLemming()
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

    /**
     * A character has been deleted and its behavior should also be deleted
     */
    public void deleteBehavior(Behavior b) {
        int index = behaviors.indexOf(b);
        if (index != -1) {
            model.deleteCharacter(b.getCharacter());
            behaviors.remove(index);
        }
    }
}