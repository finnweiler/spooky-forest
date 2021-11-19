package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

/**
 * Diese Klasse repräsentiert eine bewegliches Objekt in der Szene mit dem dazugehörigen Modell.
 */
public class Entity {

    /**
     * Index der Textur
     */
    private final static int TEXTURE_INDEX = 0;

    /**
     * texturiertes 3D Modell der Entität
     */
    private final TexturedModel model;
    /**
     * globale Position der Entität
     */
    private Vector3f position;
    /**
     * Rotation der Entität
     */
    private float rotX, rotY, rotZ;
    /**
     * Skalierung der Entität
     */
    private final float scale;


    /**
     * Eine Entität bestehend aus dem {@link TexturedModel}, seiner Position, Rotation und Skalierung wird erstellt.
     *
     * @param model    Modell der Entität
     * @param position Position der Entität
     * @param rotX     Rotation der Entität um die x-Achse
     * @param rotY     Rotation der Entität um die y-Achse
     * @param rotZ     Rotation der Entität um die z-Achse
     * @param scale    Skalierung der Entität
     */
    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    /**
     * Diese Funktion verschiebt die Koordinaten des Entities um die übergebene Translation.
     *
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
     * Diese Funktion rotiert die Koordinaten der Entität um die übergebene Rotation.
     *
     * @param dx Rotation um die x-Achse
     * @param dy Rotation um die y-Achse
     * @param dz Rotation um die z-Achse
     */
    public void rotate(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    /**
     * Diese Funktion gibt den Texture Offset für ein Object zurück.
     * Dieser wird benötigt, wenn mehrere Texturen für das gleiche Model in einem einzelnen PNG hinterlegt sind.
     *
     * @return Texture Offset
     */
    public Vector2f getTextureOffset() {
        int column = TEXTURE_INDEX % model.getTexture().getNumberOfRows();
        float x = (float) column / (float) model.getTexture().getNumberOfRows();
        int row = TEXTURE_INDEX / model.getTexture().getNumberOfRows();
        float y = (float) row / (float) model.getTexture().getNumberOfRows();
        return new Vector2f(x, y);
    }

    /**
     * Diese Funktion gibt die Transformationsmatrix der Entität zurück,
     * die zur Verarbeitung im Shader benötigt wird.
     *
     * @return Transformationsmatrix
     */
    public Matrix4f getTransformationMatrix() {
        return Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale);
    }

    /**
     * Diese Funktion übergibt das texturierte 3d-Modell der Entität.
     *
     * @return texturiertes 3d-Modell
     */
    public TexturedModel getModel() {
        return model;
    }

    /**
     * Diese Funktion übergibt die globale Position der Entität.
     *
     * @return Position der Entität
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Diese Funktion hinterlegt eine neue Position der Entität.
     *
     * @param position Position
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    /**
     * Diese Funktion übergibt die Rotation der Entität um die y-Achse.
     *
     * @return Rotation um die y-Achse
     */
    public float getRotY() {
        return rotY;
    }

    /**
     * Diese Funktion hinterlegt eine neue Rotation der Entität um die y-Achse.
     *
     * @param rotY Rotation um die y-Achse
     */
    public void setRotY(float rotY) {
        this.rotY = rotY;
    }
}

