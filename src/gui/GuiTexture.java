package gui;

import org.lwjgl.util.vector.Vector2f;


/**
 * Wrapper Class for a GUI element.
 */
public class GuiTexture {

    // Speicher Textur Id
    // Position und Scale
    private final int texture;
    private final Vector2f position;
    private final Vector2f scale;

    // Constructor
    public GuiTexture(int texture, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    // getter
    public int getTexture() {
        return texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }
}
