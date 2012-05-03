package controller;

import model.Model;
import model.Character;
import util.LemmingsException;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Controller {
    private Model model;

    public Controller() {
    }

    public void setModel(Model m) {
        model = m;
    }

    public Model getModel() {
        return model;
    }

    public void start(String map)
        throws LemmingsException {
        model.setMap(map);
        model.start();
    }

    public void pause() {
        model.pause();
    }

    public void stop() {
        model.stop();
    }

    public void increaseSpeed() {
        model.setSpeed(model.getSpeed() + 1);
    }

    public void decreaseSpeed() {
        model.setSpeed(model.getSpeed() - 1);
    }

    public void characterSelected(Character c) {
        /* TODO: do not change the behavior if it should not be
         * accepted (eg. faller -> blocker) */
        c.setBehavior(new model.behaviors.Bomber(c.getBehavior()));
    }

    public void save()
        throws LemmingsException {
        try {
            FileOutputStream file = new FileOutputStream("/tmp/save.lem");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(model);
            out.close();
            file.close();
        } catch (java.io.IOException e) {
            throw new LemmingsException("controller", "Can't save game: " + e);
        }
    }
}