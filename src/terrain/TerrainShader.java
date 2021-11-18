package terrain;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;
import textures.TerrainTexturePack;
import toolbox.Maths;

import java.util.List;


/**
 * Diese Klasse implementiert und erweitert das {@link ShaderProgram} für die Darstellung des Terrains.
 */
public class TerrainShader extends ShaderProgram {

    /**
     * Referenz auf die Terrain Vertex Shader Datei
     */
    private static final String VERTEX_FILE = "src/terrain/terrainVertexShader";
    /**
     * Referenz auf die Terrain Fragment Shader Datei
     */
    private static final String FRAGMENT_FILE = "src/terrain/terrainFragmentShader";

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationLightPosition[];
    private int locationLightColor[];
    private int locationAttenuation[];
    private int locationShineDamper;
    private int locationReflectivity;
    private int locationSkyColor;
    private int locationFogDensity;
    private int locationFogGradient;
    private int locationBackgroundTexture;
    private int locationRTexture;
    private int locationGTexture;
    private int locationBTexture;
    private int locationBlendMap;

    /**
     * Es wird eine Shaderrepräsentation mit den zugehörigen Vertex- und Fragmentshadern
     * in der OpenGL-Shader-Language für das Terrain erstellt.
     */
    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Diese Funktion macht die Positionen, Texturkoordinaten und Normalenvektoren der Verticies im Shader zugängig.
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
    }

    /**
     * Diese Funktion bestimmt die Referenzen auf genutzte OpenGL Ressourcen, um Daten aus Java in den Shader zu laden.
     */
    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationShineDamper = super.getUniformLocation("shineDamper");
        locationReflectivity = super.getUniformLocation("reflectivity");
        locationSkyColor = super.getUniformLocation("skyColor");
        locationFogDensity = super.getUniformLocation("density");
        locationFogGradient = super.getUniformLocation("gradient");
        locationBackgroundTexture = super.getUniformLocation("backgroundTexture");
        locationRTexture = super.getUniformLocation("rTexture");
        locationGTexture = super.getUniformLocation("gTexture");
        locationBTexture = super.getUniformLocation("bTexture");
        locationBlendMap = super.getUniformLocation("blendMap");

        locationLightPosition = new int[MAX_LIGHTS];
        locationLightColor = new int[MAX_LIGHTS];
        locationAttenuation = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            locationLightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
            locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    /**
     * Diese Funktion hinterlegt das {@link TerrainTexturePack} und die dazugehörige BlendMap.
     */
    public void connectTextureUnits() {
        super.loadInt(locationBackgroundTexture, 0);
        super.loadInt(locationRTexture, 1);
        super.loadInt(locationGTexture, 2);
        super.loadInt(locationBTexture, 3);
        super.loadInt(locationBlendMap, 4);

    }


    /**
     * Diese Funktion hinterlegt alle Positionen, Farben und Dämpfung der Lichtquellen.
     *
     * @param lights die darzustellenden Lichtquellen
     */
    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                super.loadVector(locationLightPosition[i], lights.get(i).getPosition());
                super.loadVector(locationLightColor[i], lights.get(i).getColor());
                super.loadVector(locationAttenuation[i], lights.get(i).getAttenuation());
            } else {
                super.loadVector(locationLightPosition[i], new Vector3f(0, 0, 0));
                super.loadVector(locationLightColor[i], new Vector3f(0, 0, 0));
                super.loadVector(locationAttenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }

    /**
     * Diese Funktion hinterlegt die Dichte und das Gefälle des Nebels.
     *
     * @param density  Dichte des Nebels
     * @param gradient Gefälle des Nebels
     */
    public void loadFog(float density, float gradient) {
        super.loadFloat(locationFogDensity, density);
        super.loadFloat(locationFogGradient, gradient);
    }

    /**
     * Diese Funktion hinterlegt einen RGB-Wert für die Himmelfarbe.
     *
     * @param r Rotwert
     * @param g Grünwert
     * @param b Blauwert
     */
    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(locationSkyColor, new Vector3f(r, g, b));
    }

    /**
     * Diese Funktion hinterlegt die Werte der Glanzreduktion und Reflektion.
     *
     * @param damper     Wert der Glanzreduktion
     * @param reflection Wert der Reflektion
     */
    public void loadShineVariables(float damper, float reflection) {
        super.loadFloat(locationShineDamper, damper);
        super.loadFloat(locationReflectivity, reflection);
    }

    /**
     * Diese Funktion hinterlegt eine {@link Matrix4f} als Tranformationsmatrix.
     *
     * @param matrix Tranformationsmatrix ({@link Matrix4f})
     */
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

    /**
     * Diese Funktion hinterlegt eine {@link Camera} als Ansichtsmatrix.
     *
     * @param camera Kamera ({@link Camera})
     */
    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(locationViewMatrix, viewMatrix);
    }

    /**
     * Diese Funktion hinterlegt eine {@link Matrix4f} als Projektionsmatrix.
     *
     * @param matrix Projektionsmatrix ({@link Matrix4f})
     */
    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(locationProjectionMatrix, matrix);
    }

}
