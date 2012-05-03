package model;

import model.Character;

public interface Behavior {
    public String getName();
    public Model getModel();
    public Character getCharacter();
    public void update(Map map, long delta);
}