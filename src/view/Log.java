package view;

import java.util.Vector;

import org.newdawn.slick.Renderable;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;

class Log implements Renderable {
    private Vector<String> lines;
    private TrueTypeFont font;
    private int maxMessages;
    private int width, height;

    private static int offset = 1;

    public Log(int width, int height, TrueTypeFont font) {
        this.font = font;
        this.width = width;
        this.height = height;
        lines = new Vector<String>();
        maxMessages = height/(font.getHeight() + 2*offset);
    }

    public void add(String message) {
        lines.add(0, message);
        lines.setSize(Math.min(lines.size(), maxMessages));
    }

    public void draw(float x, float y) {
        Graphics g = new Graphics();
        g.drawRect(x, y, width, height);
        int curY = (int) y + height - offset - font.getHeight();
        for (String str : lines) {
            font.drawString(x + 1, curY, str, Color.white);
            curY -= font.getHeight() + offset;
        }
    }
}