package view;

import java.util.Vector;

import org.newdawn.slick.Renderable;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;

class Log implements Renderable {
    private Vector<String> lines;
    private UnicodeFont font;
    private int maxMessages;
    private int width, height;
    private boolean activated;

    private static int offset = 1;

    public Log(int width, int height, UnicodeFont font) {
        this.font = font;
        this.width = width;
        this.height = height;
        lines = new Vector<String>();
        maxMessages = height/(font.getLineHeight() + 2*offset);
        activated = false;
    }

    public void add(String message) {
        System.out.println(message);
        lines.add(0, message);
        lines.setSize(Math.min(lines.size(), maxMessages));
    }

    public void draw(float x, float y) {
        if (activated) {
            Graphics g = new Graphics();
            g.drawRect(x, y, width, height);
            int curY = (int) y + height - offset - font.getLineHeight();
            for (String str : lines) {
                font.drawString(x + 1, curY, str, Color.white);
                curY -= font.getLineHeight() + offset;
            }
        }
    }

    public void toggle() {
        activated = !activated;
    }
}