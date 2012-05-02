package view;

import util.LemmingsException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.PNGImageData;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.lwjgl.opengl.GL11;

import java.io.FileInputStream;
import java.io.File;
import java.nio.ByteBuffer;

public class MapImage {
    private Image background;
    private Image foreground;
    private ByteBuffer buffer;
    private TextureImpl texture;

    static private SGL GL = Renderer.get();
    /* If slick is too old, those constants are not defined in SGL */
    static private int GL_TEXTURE_MAG_FILTER = 0x2800;
    static private int GL_TEXTURE_MIN_FILTER = 0x2801;
    private static int GL_RGBA8 = 0x8058;
    
    public MapImage(String directory)
        throws SlickException {
        this.background = new Image(directory + "/background.png");
        this.foreground = new Image(directory + "/foreground.png");

        String bg = directory + "/background.png";
        try {
            buffer = new PNGImageData().loadImage(new FileInputStream(bg));
            texture = (TextureImpl) InternalTextureLoader.get().getTexture(new File(bg),
                                                                           false, SGL.GL_NEAREST);
            reloadTexture();
        } catch (java.io.IOException e) {
            throw new SlickException(e.getMessage());
        }
    }

    public int getWidth() {
        return background.getWidth();
    }

    public int getHeight() {
        return background.getHeight();
    }

    public Image getBackground() {
        return background;
    }

    public Image getForeground() {
        return foreground;
    }

    /**
     * Destroys some pixels on the map
     */
    public void destroy(int x, int y, int w, int h) {
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
                destroyPixel(x+i, y+j);
        reloadTexture();
    }

    /**
     * Destroy a zone of pixel on the map
     */
    public void destroy(int[] zone, int x, int y, int w, int h) {
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
                if (zone[j*w + i] != 0)
                    destroyPixel(x+i, y+j);
        reloadTexture();
    }

    /**
     * Destroy a pixel on the map
     */
    public void destroyPixel(int x, int y) {
        int offset = 4*(y*get2Fold(getWidth()) + x);
        buffer.put(offset, (byte) 0);
        buffer.put(offset+1, (byte) 0);
        buffer.put(offset+2, (byte) 0);
        buffer.put(offset+3, (byte) 0);
    }

    /**
     * Reload OpenGL texture
     */
    private void reloadTexture() {
        /* TODO: this don't work with some version of slick
         * (introduced in revision 1490) */
        /* texture.setTextureData(SGL.GL_RGBA, 4, SGL.GL_NEAREST, SGL.GL_NEAREST, buffer); */

        texture.bind();
        GL11.glTexImage2D(SGL.GL_TEXTURE_2D, 0, GL_RGBA8,
                          get2Fold(getWidth()), get2Fold(getHeight()),
                          0, SGL.GL_RGBA, SGL.GL_UNSIGNED_BYTE, buffer);
        background.setTexture(texture);
    }

    /**
     * Get the closest greater power of 2 to the fold number
     */
    private static int get2Fold(int fold) {
        int ret = 2;
        while (ret < fold)
            ret *= 2;
        return ret;
    }
}
