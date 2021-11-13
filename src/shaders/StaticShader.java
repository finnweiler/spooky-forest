package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

import java.util.List;

public class StaticShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/shaders/vertexShader";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader";

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationLightPosition[];
    private int locationLightColor[];
    private int locationAttenuation[];
    private int locationShineDamper;
    private int locationReflectivity;
    private int locationIsFakeLit;
    private int locationSkyColor;
    private int locationFogDensity;
    private int locationFogGradient;
    private int locationNumberOfRows;
    private int locationOffset;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationShineDamper = super.getUniformLocation("shineDamper");
        locationReflectivity = super.getUniformLocation("reflectivity");
        locationIsFakeLit = super.getUniformLocation("isFakeLit");
        locationSkyColor = super.getUniformLocation("skyColor");
        locationFogDensity = super.getUniformLocation("density");
        locationFogGradient = super.getUniformLocation("gradient");
        locationNumberOfRows = super.getUniformLocation("numberOfRows");
        locationOffset = super.getUniformLocation("offset");

        locationLightPosition = new int[MAX_LIGHTS];
        locationLightColor = new int[MAX_LIGHTS];
        locationAttenuation = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            locationLightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
            locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    public void  loadNumberOfRows(int numberOfRows) {
        super.loadFloat(locationNumberOfRows, numberOfRows);
    }

    public void loadOffset(Vector2f offset) {
        super.loadVector2(locationOffset, offset);
    }

    public void loadFog(float density, float gradient) {
        super.loadFloat(locationFogDensity, density);
        super.loadFloat(locationFogGradient, gradient);
    }

    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(locationSkyColor, new Vector3f(r,g,b));
    }

    public void loadIsFakeLit(boolean isFakeLit) {
        super.loadBoolean(locationIsFakeLit, isFakeLit);
    }

    public void loadShineVariables(float damper, float reflection) {
        super.loadFloat(locationShineDamper, damper);
        super.loadFloat(locationReflectivity, reflection);
    }

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                super.loadVector(locationLightPosition[i], lights.get(i).getPosition());
                super.loadVector(locationLightColor[i], lights.get(i).getColor());
                super.loadVector(locationAttenuation[i], lights.get(i).getAttenuation());
            } else {
                super.loadVector(locationLightPosition[i], new Vector3f(0,0,0));
                super.loadVector(locationLightColor[i], new Vector3f(0,0,0));
                super.loadVector(locationAttenuation[i], new Vector3f(1,0,0));
            }
        }
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(locationViewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(locationProjectionMatrix, matrix);
    }
}
