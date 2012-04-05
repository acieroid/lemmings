package controller;

import model.Model;

import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    private Model model;

    public Controller() {
    }

    public void setModel(Model m) {
        model = m;
    }

    public void mouseClicked(int x, int y) {
        System.out.println("Mouse clicked");
        //m.addWalker(x, y); /* for development purpose */
    }
}