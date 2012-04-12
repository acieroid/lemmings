package controller;

import model.Character;
import controller.Controller;

public interface Behavior {
    public String getName();
    public Controller getController();
    public Character getCharacter();
    public void update(CollisionMap colMap);
}