package textures;

import java.nio.ByteBuffer;

/**
 * Diese Klasse enthält eine Bufferrepräsentation von Texturdaten und deren Metadaten
 */
public class TextureData {

    private int width;          // Breite der Textur in Pixeln
    private int height;         // Höhe der Textur in Pixeln
    private ByteBuffer buffer;  // Texturbuffer

    public TextureData(int width, int height, ByteBuffer buffer) {
        this.width = width;
        this.height = height;
        this.buffer = buffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }
}
