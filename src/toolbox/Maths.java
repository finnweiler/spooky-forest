package toolbox;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Diese Klasse stellt mathematische Formel bereit, die nicht in der Math Bibliothek enthalten sind.
 */
public class Maths {

    /**
     * Diese Funktion übergibt die Höhe für die Koordinaten (x, z) in einem beliebigen Dreieck.
     *
     * @param p1       1. Eckpunkt des Dreiecks
     * @param p2       2. Eckpunkt des Dreiecks
     * @param p3       3. Eckpunkt des Dreiecks
     * @param position (x, z)-Koordinaten der gesuchten Höhe in einem {@link Vector2f}
     * @return Höhe für die Koordinaten (x, z)
     */
    public static float baryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f position) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (position.x - p3.x) + (p3.x - p2.x) * (position.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (position.x - p3.x) + (p1.x - p3.x) * (position.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    /**
     * Diese Funktion berechnet auf Grundlage von Translation, Rotation und Skalierung die Transformationsmatrix, die im Shader benötigt wird.
     *
     * @param translation Translation ({@link Vector3f})
     * @param rx          Rotation um x-Achse
     * @param ry          Rotation um y-Achse
     * @param rz          Rotation um z-Achse
     * @param scale       Skalierung
     * @return Transformationsmatrix
     */
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        return matrix;
    }

    /**
     * Diese Funktion berechnet auf Grundlage von Translation und Skalierung die Transformationsmatrix, die im Shader benötigt wird.
     * Diese Matrix ist speziell für zwei Dimensionale GUIs.
     *
     * @param translation 2D-Translation
     * @param scale       2D-Skalierung
     * @return Transformationsmatrix
     */
    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
        return matrix;
    }

    /**
     * Diese Funktion berechnet auf Grundlage der {@link Camera} die Ansichtsmatrix, die im Shader benötigt wird.
     *
     * @param camera Kamera, von der aus die Szene betrachtet wird
     * @return Ansichtsmatrix
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
