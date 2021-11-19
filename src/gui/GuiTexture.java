package gui;

import org.lwjgl.util.vector.Vector2f;


/**
 * Diese Klasse ermöglicht die Referenz auf eine Textur, die für die GUI verwendet wird.
 */
public class GuiTexture {

    /**
     * Referenz auf die Textur
     */
    private final int texture;
    /**
     * Position der Textur
     */
    private final Vector2f position;
    /**
     * Skalierung der Textur
     */
    private final Vector2f scale;


    /**
     * Eine {@link GuiTexture} wird mit der Referenz auf die Textur, deren Position und Skalierung erstellt.
     *
     * @param texture  Referenz auf die Textur
     * @param position Position der Textur
     * @param scale    Skalierung der Textur
     */
    public GuiTexture(int texture, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    /**
     * Diese Funktion übergibt die Referenz auf die Textur.
     *
     * @return Referenz auf die Textur
     */
    public int getTexture() {
        return texture;
    }

    /**
     * Diese Funktion übergibt die Position der Textur.
     *
     * @return Position der Textur
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * Diese Funktion übergibt die Skalierung der Textur.
     *
     * @return Skalierung der Textur
     */
    public Vector2f getScale() {
        return scale;
    }
}
