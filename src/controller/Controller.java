package controller;

import view.View;
import model.Model;
import model.Character;
import model.Behavior;
import util.LemmingsException;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;


public class Controller {
    private Model model;
    private View view;
    private String selectedBehavior;

    public Controller() {
        selectedBehavior = "";
    }

    public void setModel(Model m) {
        model = m;
    }

    public Model getModel() {
        return model;
    }

    public void setView(View view) {
        this.view = view;
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
        Behavior b = null;
        if (selectedBehavior == "bomber")
            b = new model.behaviors.Bomber(c.getBehavior());
        else if (selectedBehavior == "blocker")
            b = new model.behaviors.Blocker(c.getBehavior());

        if (b != null)
            c.setBehavior(b);

        /* TODO: do not change the behavior if it should not be
         * accepted (eg. faller -> blocker) */
    }

    public void setSelectedBehavior(String behavior) {
        selectedBehavior = behavior;
    }

    public void save()
        throws LemmingsException {
        synchronized (model) {
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

    public void load()
        throws LemmingsException {
        try {
            model.stop();
            FileInputStream file = new FileInputStream("/tmp/save.lem");
            ObjectInputStream in = new ObjectInputStream(file);
            model = (Model) in.readObject();
            in.close();
            file.close();
            model.setView(view);
            view.setModel(model);
            model.start();
        } catch (java.io.IOException e) {
            throw new LemmingsException("controller", "Can't load game: " + e);
        } catch (ClassNotFoundException e) {
            throw new LemmingsException("controller", "Can't load game: " + e);
        }
    }
}