package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class Entity {

    private TexturedModel model;        // Texturiertes 3D Modell des Entities
    private Vector3f position;          // Die globale Position des Entities
    private float rotX, rotY, rotZ;     // Die Rotation des Entities in Grad
    private float scale;                // Die Skalierung des Entities

    private int textureIndex = 0;

    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    /**
     * Diese Funktion verschiebt die Koordinaten des Entities um die übergebene Translation
     * @param dx Translation um die x-Achse
     * @param dy Translation um die y-Achse
     * @param dz Translation um die z-Achse
     */
    public void translate(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    /**
     * Diese Funktion rotiert die Koordinaten des Entities um die übergebene Rotations
     * @param dx
     * @param dy
     * @param dz
     */
    public void rotate(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    /**
     * Diese Funktion gibt den Texture Offset für ein Object zurück.
     * Dieser wird benötigt, wenn mehrere Texturen für das gleiche Model in einem einzelnen PNG hinterlegt sind.
     * @return Texture Offset
     */
    public Vector2f getTextureOffset() {
        int column = textureIndex % model.getTexture().getNumberOfRows();
        float x = (float) column / (float) model.getTexture().getNumberOfRows();
        int row = textureIndex / model.getTexture().getNumberOfRows();
        float y = (float) row / (float) model.getTexture().getNumberOfRows();
        return new Vector2f(x, y);
    }

    /**
     * Diese Funktion gibt die Transformationsmatrix des Entities zurück,
     * die zur Verarbeitung im Shader benötigt wird.
     * @return Transformationsmatrix
     */
    public Matrix4f getTransformationMatrix() {
        return Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale);
    }

    public TexturedModel getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }
}

