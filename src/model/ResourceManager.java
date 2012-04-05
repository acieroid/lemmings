package model;

import util.LemmingsException;

import java.util.LinkedList;
import java.io.File;

import org.newdawn.slick.SlickException;

public class ResourceManager {
    private String dir;

    public ResourceManager(String directory) {
        this.dir = directory;
    }

    public LinkedList<String> getAllMaps() {
        File mapsDir = new File(dir + "/maps/");
        LinkedList<String> l = new LinkedList<String>();
        for (File mapDir : mapsDir.listFiles())
            l.add(mapDir.getName());
        return l;
    }

    public Map getMap(String name)
        throws LemmingsException {
        try {
            return new Map(dir + "/maps/" + name + "/image.png",
                           dir + "/maps/" + name + "/collision.png");
        } catch (SlickException e) {
            throw new LemmingsException("resourcemanager",
                                        "Error when loading map '" + name + "': " +
                                        e.toString());
        }
    }

    public Character getCharacter(String name)
        throws LemmingsException {
        try {
            return new Character(0, 0,
                                 dir + "/characters/" + name + "/image.png",
                                 dir + "/characters/" + name + "/collision.png");
        } catch (SlickException e) {
            throw new LemmingsException("resourcemanager",
                                        "Error when loading map '" + name + "': " +
                                        e.toString());
        }
    }
}                        
