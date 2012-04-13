package controller;

import util.LemmingsException;
import parser.LispFile;

class ResourceManager {
    private String dir;

    public ResourceManager(String directory) {
        this.dir = directory;
    }

    public CollisionMap getCollisionMap(String name)
        throws LemmingsException {
        return new CollisionMap(name, dir + "/maps/" + name);
    }
}
