package toolbox;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Diese Klasse stellt mathematische Formel bereit, die nicht in der Math Bibleothek enthalten sind.
 */
public class Maths {

    /**
     * Formel zu Berechnung der Y Koordinaten in einem beliebigen Dreieck
     * @param p1 Erster Eckpunkt des Dreiecks
     * @param p2 Zweiter Eckpunkt des Dreiecks
     * @param p3 Dritter Eckpunkt des Dreiecks
     * @param pos X und Z Koordinate des gesuchten Y Koordinate
     * @return Y Koordinate bei pos
     */
    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    /**
     * Berechnet auf Grundlage von Translation, Rotation und Skalierung die Transformationsmatrix, die im Shader benötigt wird.
     * @param translation Translation
     * @param rx Rotation um x-Achse
     * @param ry Rotation um y-Achse
     * @param rz Rotation um z-Achse
     * @param scale Skalierung
     * @return Transformationsmatrix
     */
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        return matrix;
    }

    /**
     * Berechnet auf Grundlage von Translation und Skalierung die Transformationsmatrix, die im Shader benötigt wird.
     * Diese Matrix ist speziell für zwei Dimensionale GUIs.
     * @param translation Translation
     * @param scale Scale
     * @return
     */
    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
        return matrix;
    }

    /**
     * Berechnet auf Grundlage der Kamera, die Sichtmatrix, die im Shader benötigt wird.
     * @param camera Kamera, von der die Szene betrachtet wird.
     * @return Sichtmatrix
     */
    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), matrix, matrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPose = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f.translate(negativeCameraPose, matrix, matrix);
        return matrix;
    }
}
