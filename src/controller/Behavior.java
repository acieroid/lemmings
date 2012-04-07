package controller;

import model.Character;

public interface Behavior {
    public String getName();
    public void update(CollisionMap colMap);
}