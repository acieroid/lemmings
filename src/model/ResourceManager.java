package model;

import util.LemmingsException;

import java.util.LinkedList;
import java.io.File;

class ResourceManager {
    private String dir;

    public ResourceManager(String directory) {
        this.dir = directory;
    }

    public LinkedList<String> getAllMaps() {
        File mapsDir = new File(dir + "/maps/");
        LinkedList<String> l = new LinkedList<String>();
        for (File mapDir : mapsDir.listFiles())
            l.add(mapDir.getName());
        //l.sort(); /* TODO */
        return l;
    }

    public Map getMap(String name)
        throws LemmingsException {
        return new Map(dir + "/maps/" + name + "/collision.png");
    }

    public Character getCharacter(int x, int y, CharacterBehavior b)
        throws LemmingsException {
        return new Character(x, y, b,
                             dir + "/characters/" + b.getName() +
                             "/" + b.getName() + ".character");
    }
}                        
