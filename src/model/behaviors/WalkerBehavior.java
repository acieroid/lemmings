package model.behaviors;

import model.CharacterBehavior;
import model.Character;
import model.Map;

public class WalkerBehavior implements CharacterBehavior {
    private static int LEFT = -1;
    private static int RIGHT = 1;

    private int direction;
    private boolean isFalling;
    
    public WalkerBehavior() {
        direction = RIGHT;
        isFalling = false;
    }

    public String getName() {
        return "walker";
    }

    public void update(Character c, Map map) {
        int x = c.getX();
        int y = c.getY();
        int width = c.getWidth();
        int height = c.getHeight();

        if (map.isCollisionFree(x, y+1, width, height)) {
            c.setPosition(x, y+1);
            isFalling = true;
        }
        else if (isFalling) {
            isFalling = false;
        }

        if (!isFalling) {
            int dx = direction;

            if (map.isCollisionFree(x+dx, y, width, height))
                c.setPosition(x+dx, y);
            else
                direction *= -1;
        }
    }
}
        