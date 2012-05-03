package view;

/**
 * Represents a zone to be destroyed
 */
class DestroyInfo {
    public int x, y, w, h;
    public int[] zone;
    public boolean hasZone;

    public DestroyInfo(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.hasZone = false;
    }

    public DestroyInfo(int zone[], int x, int y, int w, int h) {
        this(x, y, w, h);
        this.zone = zone;
        this.hasZone = true;
    }
}   
