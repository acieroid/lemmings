package model;

import util.LemmingsException;

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

    public static Map getMap(String name)
        throws LemmingsException {
        return new Map(name, dir + "/maps/" + name);
    }
}
