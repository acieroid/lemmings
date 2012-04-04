package model;

import java.util.LinkedList;

import java.io.File;

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

    public Map getMap(String name) {
        try {
            return new Map(dir + "/maps/" + name + "/image.png",
                           dir + "/maps/" + name + "/collision.png");
        } catch (Exception e) {
            System.out.println("Error when loading map '" + name + "': " +
                               e.toString());
            return null;
        }
    }

    public Character getCharacter(String name) {
        try {
            return new Character(0, 0,
                                 dir + "/characters/" + name + "/image.png",
                                 dir + "/characters/" + name + "/collision.png");
        } catch (Exception e) {
            System.out.println("Error when loading character '" + name + "': " +
                               e.toString());
            return null;
        }
    }
}                        
