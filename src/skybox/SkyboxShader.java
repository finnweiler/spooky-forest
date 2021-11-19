package skybox;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import toolbox.Maths;

/**
 * Diese Klasse implementiert und erweitert das {@link ShaderProgram} für die Darstellung der Skybox.
 */
public class SkyboxShader extends ShaderProgram {

    /**
     * Referenz auf die Skybox Vertex Shader Datei
     */
    private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader";
    /**
     * Referenz auf die Skybox Fragment Shader Datei
     */
    private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader";

    /**
     * Referenz auf die Projektionsmatrix in dem Shader Programm
     */
    private int locationProjectionMatrix;
    /**
     * Referenz auf die Ansichtsmatrix in dem Shader Programm
     */
    private int locationViewMatrix;

    /**
     * Referenz auf die Farbe des Nebels in dem Shader Programm
     */
    private int locationFogColor;
    /**
     * Referenz auf die Tagestextur der Skybox in dem Shader Programm
     */
    private int locationCubeMap;
    /**
     * Referenz auf die Nachttextur der Skybox in dem Shader Programm
     */
    private int locationCubeMap2;
    /**
     * Referenz auf den Vermischungsfaktor der Tages- und Nachttextur in dem Shader Programm
     */
    private int locationBlendFactor;


    /**
     * Drehwinkel der Skybox-Textur
     */
    private float currentRotation = 0;
    /**
     * Drehgeschwindigkeit der Skybox-Textur
     */
    private static final float ROTATE_SPEED = 0.0001f;


    /**
     * Es wird eine Shaderrepräsentation mit den zugehörigen Vertex- und Fragmentshadern
     * in der OpenGL-Shader-Language für die Skybox erstellt.
     */
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Diese Funktion hinterlegt einen RGB-Wert für die Farbe des Nebels.
     *
     * @param r Rotwert
     * @param g Grünwert
     * @param b Blauwert
     */
    public void loadFogColor(float r, float g, float b) {
        super.loadVector(locationFogColor, new Vector3f(r, g, b));
    }

    /**
     * Diese Funktion hinterlegt einen Vermischungsfaktor der Tages- und Nachttextur.
     *
     * @param blend Vermischungsfaktor
     */
    public void loadBlendFactor(float blend) {
        super.loadFloat(locationBlendFactor, blend);
    }

    /**
     * Diese Funktion hinterlegt die Tages- und Nachttextur der Skybox.
     */
    public void connectTextureUnits() {
        super.loadInt(locationCubeMap, 0);
        super.loadInt(locationCubeMap2, 1);
    }

    /**
     * Diese Funktion hinterlegt eine {@link Matrix4f} als Projektionsmatrix.
     *
     * @param matrix Projektionsmatrix ({@link Matrix4f})
     */
    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(locationProjectionMatrix, matrix);
    }

    /**
     * Diese Funktion hinterlegt eine {@link Camera} als Ansichtsmatrix.
     *
     * @param camera Kamera ({@link Camera})
     */
    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        currentRotation += ROTATE_SPEED * DisplayManager.getFrameTime();
        Matrix4f.rotate((float) Math.toRadians(currentRotation), new Vector3f(0, 1, 0), matrix, matrix);
        super.loadMatrix(locationViewMatrix, matrix);
    }

    /**
     * Diese Funktion bestimmt die Referenzen auf genutzte OpenGL Ressourcen, um Daten aus Java in den Shader zu laden.
     */
    @Override
    protected void getAllUniformLocations() {
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationFogColor = super.getUniformLocation("fogColor");
        locationCubeMap = super.getUniformLocation("cubeMap");
        locationCubeMap2 = super.getUniformLocation("cubeMap2");
        locationBlendFactor = super.getUniformLocation("blendFactor");
    }

    /**
     * Diese Funktion macht die Positionen der Verticies im Shader zugängig.
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
