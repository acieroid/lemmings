package model;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;
import org.newdawn.slick.Color;

public class ModifiableImage extends Image {
    public ModifiableImage(String ref) throws SlickException{
        super(ref);
    }

    /**
     * @sa Image.translate
     * @todo The converse.
     */
    private int translate(byte b) {
        if (b < 0)
            return 256 + b;
        return b;
    }

    /**
     * Set the color of a pixel at a specified location in the image
     * @param x: The x coordinate of the pixel
     * @param y: The y coordinate of the pixel
     * @param color: The new color of the pixel
     */
    public void setColor(int x, int y, Color color) {
        if (pixelData == null) {
            pixelData = texture.getTextureData();
        }

        int xo = (int) (textureOffsetX * texture.getTextureWidth());
        int yo = (int) (textureOffsetY * texture.getTextureHeight());

        if (textureWidth < 0) { 
            x = xo - x; 
        } else {
            x = xo + x; 
        } 

        if (textureHeight < 0) { 
            y = yo - y; 
        } else {
            y = yo + y; 
        }

        int offset = x + (y * texture.getTextureWidth());
        offset *= texture.hasAlpha() ? 4 : 3; 

        pixelData[offset] = (byte) color.getRed();
        pixelData[offset+1] = (byte) color.getGreen();
        pixelData[offset+2] = (byte) color.getBlue();

        if (texture.hasAlpha())
            pixelData[offset+3] = (byte) color.getAlpha();
    }
}
 