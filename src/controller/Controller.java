package controller;

import model.Model;
import util.LemmingsException;

import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    private Model model;
    private Timer timer;

    public Controller() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    model.update();
                }   
            }, 300, 100);
    }

    public void setModel(Model m) {
        model = m;
    }

    public void mouseClicked(int x, int y)
        throws LemmingsException {
        System.out.println("Mouse clicked");
        model.addWalker(x, y); /* for development purpose */
    }
}