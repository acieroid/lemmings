package model;

import util.LemmingsException;
import parser.LispFile;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.LinkedList;
import java.util.Collections;
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
        Collections.sort(l, String.CASE_INSENSITIVE_ORDER);
        return l;
    }

    public Character getCharacter(int x, int y, String name)
        throws LemmingsException {
        LispFile f = new LispFile(dir + "/characters/" + name +
                                  "/" + name + ".character");
        return new Character(x, y,
                             f.getNumberProperty("size", 0),
                             f.getNumberProperty("size", 1),
                             name);
    }
}
