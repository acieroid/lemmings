package controller;

import util.LemmingsException;
import parser.LispFile;

import java.util.LinkedList;
import java.util.Collections;
import java.io.File;

public class ResourceManager {
    private static String dir = "../data/";

    public static LinkedList<String> getAllMaps() {
        File mapsDir = new File(dir + "/maps/");
        LinkedList<String> l = new LinkedList<String>();
        for (File mapDir : mapsDir.listFiles())
            l.add(mapDir.getName());
        Collections.sort(l, String.CASE_INSENSITIVE_ORDER);
        return l;
    }

    public static CollisionMap getCollisionMap(String name)
        throws LemmingsException {
        return new CollisionMap(name, dir + "/maps/" + name);
    }
}
