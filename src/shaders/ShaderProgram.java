package shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * Diese Klasse ist eine Oberklasse für alle Shaderrepräsentationen und enthält grundlegende Funktionen,
 * um den Shaderquellcode und verschiedene Java-Datentypen auf die GPU zu laden.
 */
public abstract class ShaderProgram {

    /**
     * maximale Anzahl an Lichtern in einem Shader
     */
    protected static final int MAX_LIGHTS = 4;

    /**
     * Referenz auf das Programm mit beiden Shadern
     */
    private final int programId;
    /**
     * Referenz auf den Vertex Shader
     */
    private final int vertexShaderId;
    /**
     * Referenz auf den Fragment Shader
     */
    private final int fragmentShaderId;

    /**
     * Buffer zum Laden einer Matrix
     */
    private static final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    /**
     * Es wird eine Shaderrepräsentation mit den zugehörigen Vertex- und Fragmentshadern
     * in der OpenGL-Shader-Language erstellt.
     *
     * @param vertexFile   Dateiname des zu ladenden Vertex Shaders
     * @param fragmentFile Dateiname des zu ladenden Fragment Shaders
     */
    public ShaderProgram(String vertexFile, String fragmentFile) {
        vertexShaderId = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShaderId);
        GL20.glAttachShader(programId, fragmentShaderId);
        bindAttributes();
        GL20.glLinkProgram(programId);
        GL20.glValidateProgram(programId);
        getAllUniformLocations();
    }

    /**
     * Diese Funktion bestimmt die Referenzen auf genutzte OpenGL Ressourcen, um Daten aus Java in den Shader zu laden.
     */
    protected abstract void getAllUniformLocations();

    /**
     * Diese Funktion bestimmt die Referenz auf eine genutzte OpenGL Ressource, um Daten aus Java in den Shader zu laden.
     *
     * @param uniformName Name der Ressource
     * @return Referenz auf eine genutzte OpenGL Ressource
     */
    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programId, uniformName);
    }

    /**
     * Diese Funktion hinterlegt einen int-Wert an der angegebenen Referenz.
     *
     * @param location Referenz
     * @param value    zu hinterlegender Wert
     */
    protected void loadInt(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    /**
     * Diese Funktion hinterlegt einen float-Wert an der angegebenen Referenz.
     *
     * @param location Referenz
     * @param value    zu hinterlegender Wert
     */
    protected void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    /**
     * Diese Funktion hinterlegt einen 3D-Vektor an der angegebenen Referenz.
     *
     * @param location Referenz
     * @param vector   zu hinterlegender Vektor
     */
    protected void loadVector(int location, Vector3f vector) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    /**
     * Diese Funktion hinterlegt einen 2D-Vektor an der angegebenen Referenz.
     *
     * @param location Referenz
     * @param vector   zu hinterlegender Vektor
     */
    protected void loadVector2(int location, Vector2f vector) {
        GL20.glUniform2f(location, vector.x, vector.y);
    }

    /**
     * Diese Funktion hinterlegt einen boolean-Wert an der angegebenen Referenz.
     *
     * @param location Referenz
     * @param value    zu hinterlegender Wert
     */
    protected void loadBoolean(int location, boolean value) {
        GL20.glUniform1f(location, value ? 1f : 0f);
    }

    /**
     * Diese Funktion hinterlegt einen 4D-Matrix an der angegebenen Referenz.
     *
     * @param location Referenz
     * @param matrix   zu hinterlegende Matrix
     */
    protected void loadMatrix(int location, Matrix4f matrix) {
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrixBuffer);
    }

    /**
     * Diese Funktion start das Programm, welches die beiden Shader implementiert.
     */
    public void start() {
        GL20.glUseProgram(programId);
    }

    /**
     * Diese Funktion stoppt das Shader-Programm.
     */
    public void stop() {
        GL20.glUseProgram(0);
    }

    /**
     * Diese Funktion löscht das Programm und die zugehörigen Shader.
     */
    public void cleanUp() {
        stop();
        GL20.glDetachShader(programId, vertexShaderId);
        GL20.glDetachShader(programId, fragmentShaderId);
        GL20.glDeleteShader(vertexShaderId);
        GL20.glDeleteShader(fragmentShaderId);
        GL20.glDeleteProgram(programId);
    }

    /**
     * Diese Funktion macht die Eigenschaften im Shader zugängig.
     */
    protected abstract void bindAttributes();

    /**
     * Diese Funktion macht das Attribut unter dem Variablennamen im Shader zugängig.
     *
     * @param attribute    zu bindendes Attribut
     * @param variableName Variablenname
     */
    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programId, attribute, variableName);
    }

    /**
     * Diese Funktion lädt eine Shaderdatei ein, generiert mithilfe von {@link GL20} den Shader und übergibt die Referenz auf diesen Shader.
     *
     * @param file Name der Shaderdatei
     * @param type {@link GL20#GL_VERTEX_SHADER} or {@link GL20#GL_FRAGMENT_SHADER}
     * @return Referenz auf diesen Shader
     */
    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Could not read shader file! " + file);
            e.printStackTrace();
            System.exit(-1);
        }

        int shaderId = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderId, shaderSource);
        GL20.glCompileShader(shaderId);

        if (GL20.glGetShader(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderId, 500));
            System.err.println("Could not compile shader file." + file);
            System.exit(-1);
        }

        return shaderId;
    }
}
