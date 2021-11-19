package toolbox;

import entities.Camera;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import terrain.Terrain;

/**
 * Diese Klasse ermöglicht den Punkt auf dem {@link MousePicker#terrain},
 * welcher auf der genauen Blickachse des Spielers liegt, zu bestimmen.
 */
public class MousePicker {

    /**
     * maximale Tiefe der Rekursion der {@link MousePicker#binarySearch}-Funktion
     */
    private static final int RECURSION_COUNT = 200;
    /**
     * maximale Strahlenlänge der Maus
     */
    private static final float RAY_RANGE = 600;

    /**
     * Projektionsmatrix zur Umwandlung der Koordinaten
     */
    private final Matrix4f projectionMatrix;
    /**
     * {@link Camera} des Spielers
     */
    private final Camera camera;

    /**
     * {@link Terrain}, auf dem sich der Spieler "bewegt"
     */
    private final Terrain terrain;
    /**
     * Punkt auf dem {@link MousePicker#terrain}, welcher auf der genauen Blickachse des Spielers liegt
     */
    private Vector3f currentTerrainPoint;

    /**
     * Ein {@link MousePicker} wird mithilfe der {@link Camera}, der aktuellen Projektionsmatrix
     * und dem genutzten {@link Terrain} angelegt.
     *
     * @param cam        {@link Camera} des Spielers
     * @param projection aktuelle Projektionsmatrix
     * @param terrain    {@link Terrain}
     */
    public MousePicker(Camera cam, Matrix4f projection, Terrain terrain) {
        camera = cam;
        projectionMatrix = projection;
        this.terrain = terrain;
    }

    /**
     * Diese Funktion übergibt Punkt auf dem {@link MousePicker#terrain}, welcher auf der genauen Blickachse des Spielers liegt.
     *
     * @return Punkt auf dem {@link MousePicker#terrain}
     */
    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }


    /**
     * Diese Funktion aktualisiert die Ansichtsmatrix der {@link Camera} und generiert den {@link MousePicker#currentTerrainPoint}.
     */
    public void update() {
        Vector3f currentRay = calculateMouseRay();
        currentTerrainPoint = intersectionInRange(0, RAY_RANGE, currentRay) ? pointOnTerrain(currentRay) : null;
    }


    /**
     * Diese Funktion generiert den Strahl der Maus in Weltkoordinaten.
     *
     * @return Strahl der Maus
     */
    private Vector3f calculateMouseRay() {
        float mouseX = (float) Display.getWidth() / 2;
        float mouseY = (float) Display.getHeight() / 2;
        Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        return toWorldCoords(eyeCoords);
    }

    /**
     * Diese Funktion generiert mithilfe der Mausposition (x, y) die normalisierten Gerätekoordinaten.
     *
     * @param mouseX x-Koordinate der Maus
     * @param mouseY y-Koordinate der Maus
     * @return normalisierten Gerätekoordinaten
     */
    private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
        float x = (2.0f * mouseX) / Display.getWidth() - 1f;
        float y = (2.0f * mouseY) / Display.getHeight() - 1f;
        return new Vector2f(x, y);
    }

    /**
     * Diese Funktion wandelt die Clipkoordinaten in die Ansichtskoordinaten um.
     *
     * @param clipCoords Clipkoordinaten
     * @return Ansichtskoordinaten
     */
    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    /**
     * Diese Funktion wandelt die Ansichtskoordinaten in den dazugehörigen Strahl in Weltkoordinaten um.
     *
     * @param eyeCoords Ansichtskoordinaten
     * @return Strahl in Weltkoordinaten
     */
    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = Matrix4f.invert(Maths.createViewMatrix(camera), null);
        Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalise();
        return mouseRay;
    }


    /**
     * Diese Funktion übergibt einen Punkt entlang des gegebenen Strahls im definierten Abstand von der {@link Camera}.
     *
     * @param ray      Strahl ({@link Vector3f})
     * @param distance Distanz des Punktes von der {@link Camera}
     * @return Punkt ({@link Vector3f})
     */
    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.getPosition();
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        return Vector3f.add(start, scaledRay, null);
    }


    /**
     * Diese Funktion bestimmt den Punkt entlang des gegebenen Strahls auf dem Terrain rekursiv.
     *
     * @param ray Strahl ({@link Vector3f})
     * @return Punkt auf dem Terrain
     */
    private Vector3f pointOnTerrain(Vector3f ray) {
        return binarySearch(0, (float) 0, RAY_RANGE, ray);
    }

    /**
     * Diese rekursive Funktion übergibt einen Punkt entlang des gegebenen Strahls auf dem Terrain.
     *
     * @param count  Tiefe der Rekursion
     * @param start  Startpunkt auf dem Strahl
     * @param finish Endpunkt auf dem Strahl
     * @param ray    Strahl ({@link Vector3f})
     * @return Punkt auf dem Terrain
     */
    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            if (terrain != null) {
                return endPoint;
            } else {
                return null;
            }
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    /**
     * Diese Funktion übergibt die Information, ob ein gesamter Abschnitt eines Strahls
     * unter der durch die Höhenkarte definierte Höhe liegt.
     *
     * @param start  Startpunkt auf dem Strahl
     * @param finish Endpunkt auf dem Strahl
     * @param ray    Strahl ({@link Vector3f})
     * @return Information, ob der gesamter Abschnitt unter der Höhenkarte liegt
     */
    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        return !isUnderGround(startPoint) && isUnderGround(endPoint);
    }

    /**
     * Diese Funktion übergibt die Information, ob ein Punkt unter der durch die Höhenkarte definierte Höhe liegt.
     *
     * @param point zu überprüfender Punkt
     * @return Information, ob der Punkt unter der Höhenkarte liegt
     */
    private boolean isUnderGround(Vector3f point) {
        float height = 0;
        if (terrain != null) {
            height = terrain.getHeight(point.getX(), point.getZ());
        }
        return point.y < height;
    }
}