package view;

import java.util.LinkedList;
import java.util.ListIterator;

import org.newdawn.slick.Renderable;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

public class Menu {
    private boolean activated;
    private int width, height;
    private Font font;

    private LinkedList<String> maps;
    private ListIterator<String> mapItem;

    public Menu(int w, int h,
                Font font,
                LinkedList<String> maps) {
        this.maps = maps;
        this.font = font;
        width = w;
        height = h;
        mapItem = maps.listIterator();
    }

    private void drawCenteredText(String text) {
        font.drawString(width/2 - font.getWidth(text)/2,
                        height/2 - font.getLineHeight()/2,
                        text, Color.white);
    }

    public void draw() {
        drawCenteredText("< " + mapItem.next() + " >");
        mapItem.previous();
    }

    /**
     * Handle a key press
     * @return true if we should launch the game
     */
    public boolean keyPressed(int key) {
        if (key == Input.KEY_LEFT) {
            if (mapItem.hasPrevious())
                mapItem.previous();
        }
        else if (key == Input.KEY_RIGHT) {
            mapItem.next();
            if (!mapItem.hasNext())
                /* We're on the last element, so we go back from one */
                mapItem.previous();
        }
        else if (key == Input.KEY_ENTER) {
            activated = false;
            return true;
        }
        return false;
    }

    /**
     * @return true if the menu is activated
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Activates the menu
     */
    public void activate() {
        activated = true;
    }

    /**
     * Return the name of the current selected map
     */
    public String getMapName() {
        String mapName = mapItem.next();
        mapItem.previous(); /* srsly java ?! */
        return mapName;
    }
}
        