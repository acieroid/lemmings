package model;

public class ResourceManager {
    private String dir;

    public ResourceManager(String directory) {
        this.dir = directory;
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
