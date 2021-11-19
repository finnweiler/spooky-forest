package textures;

import java.nio.ByteBuffer;

/**
 * Diese Klasse enthält eine Bufferrepräsentation von Texturdaten und deren Metadaten.
 */
public class TextureData {

    /**
     * Breite der Textur in Pixeln
     */
    private final int width;
    /**
     * Höhe der Textur in Pixeln
     */
    private final int height;
    /**
     * Buffer der Textur
     */
    private final ByteBuffer buffer;


    /**
     * Die Bufferrepräsentation der Texturdaten wird erstellt.
     *
     * @param width  Breite der Textur in Pixeln
     * @param height Höhe der Textur in Pixeln
     * @param buffer Buffer der Textur
     */
    public TextureData(int width, int height, ByteBuffer buffer) {
        this.width = width;
        this.height = height;
        this.buffer = buffer;
    }

    /**
     * Diese Funktion übergibt die Breite der Textur.
     *
     * @return Breite der Textur in Pixeln
     */
    public int getWidth() {
        return width;
    }

    /**
     * Diese Funktion übergibt die Höhe der Textur.
     *
     * @return Höhe der Textur in Pixeln
     */
    public int getHeight() {
        return height;
    }

    /**
     * Diese Funktion übergibt den Buffer der Textur.
     *
     * @return Buffer der Textur
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }
}
