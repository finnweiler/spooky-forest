package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class Entity {

    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;

    private int textureIndex = 0;

    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public Vector2f getTextureOffset() {
        int column = textureIndex % model.getTexture().getNumberOfRows();
        float x = (float) column / (float) model.getTexture().getNumberOfRows();
        int row = textureIndex / model.getTexture().getNumberOfRows();
        float y = (float) row / (float) model.getTexture().getNumberOfRows();
        return new Vector2f(x, y);
    }

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

