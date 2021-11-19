package gui;

import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;

/**
 * Diese Klasse implementiert und erweitert das {@link ShaderProgram} für die Darstellung der GUI.
 */
public class GuiShader extends ShaderProgram {

    /**
     * Referenz auf die GUI Vertex Shader Datei
     */
    private static final String VERTEX_FILE = "src/gui/guiVertexShader.txt";
    /**
     * Referenz auf die GUI Fragment Shader Datei
     */
    private static final String FRAGMENT_FILE = "src/gui/guiFragmentShader.txt";

    /**
     * Referenz auf die Transformationsmatrix in dem Shader Programm
     */
    private int locationTransformationMatrix;


    /**
     * Es wird eine Shaderrepräsentation mit den zugehörigen Vertex- und Fragmentshadern
     * in der OpenGL-Shader-Language für die Skybox erstellt.
     */
    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Diese Funktion hinterlegt eine {@link Matrix4f} als Transformationsmatrix.
     *
     * @param matrix Transformationsmatrix ({@link Matrix4f})
     */
    public void loadTransformation(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

    /**
     * Diese Funktion bestimmt die Referenzen auf genutzte OpenGL Ressourcen, um Daten aus Java in den Shader zu laden.
     */
    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    /**
     * Diese Funktion macht die Positionen der Verticies im Shader zugängig.
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }


}