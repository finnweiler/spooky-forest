package skybox;

import entities.Camera;
import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;

/**
 * Diese Klasse ist ein Renderer zum Darstellen der Skybox.
 */
public class SkyboxRenderer {

    /**
     * Größe der Skybox
     */
    private static final float SIZE = 500f;

    /**
     * Eckpunkt der Skybox
     */
    private static final float[] VERTICES = {
            -SIZE, SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,

            -SIZE, -SIZE, SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,
            -SIZE, SIZE, SIZE,
            -SIZE, -SIZE, SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE, SIZE,
            -SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, -SIZE, SIZE,
            -SIZE, -SIZE, SIZE,

            -SIZE, SIZE, -SIZE,
            SIZE, SIZE, -SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            -SIZE, SIZE, SIZE,
            -SIZE, SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE, SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE, SIZE,
            SIZE, -SIZE, SIZE
    };

    /**
     * Files für die Darstellung der Tagestextur der Skybox
     */
    private static final String[] TEXTURE_FILES = {
            "skybox/right",
            "skybox/left",
            "skybox/top",
            "skybox/bottom",
            "skybox/back",
            "skybox/front"};
    /**
     * Files für die Darstellung der Nachttextur der Skybox
     */
    private static final String[] TEXTURE_FILES_NIGHT = {
            "skyboxN/right",
            "skyboxN/left",
            "skyboxN/top",
            "skyboxN/bottom",
            "skyboxN/back",
            "skyboxN/front"
    };

    /**
     * Modell der Skybox
     */
    private final RawModel cube;
    /**
     * Referenz auf die Tagestextur der Skybox
     */
    private final int texture;
    /**
     * Referenz auf die Nachttextur der Skybox
     */
    private final int textureNight;

    /**
     * Shader der Skybox
     */
    private final SkyboxShader shader;

    /**
     * Vermischungsfaktor der Tages- und Nachttextur der Skybox
     */
    private float fade = 0;

    /**
     * Ein Renderer für die Skybox mit dem dazugehörigen Shader wird erstellt.
     *
     * @param loader           {@link Loader} zum Laden der Tages- und Nachttexturen
     * @param projectionMatrix Projektionsmatrix der Skybox
     */
    public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
        cube = loader.loadToVAO(VERTICES, 3);
        texture = loader.loadCubeMap(TEXTURE_FILES);
        textureNight = loader.loadCubeMap(TEXTURE_FILES_NIGHT);
        shader = new SkyboxShader();
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }


    /**
     * Diese Funktion rendert die Skybox.
     *
     * @param camera {@link Camera}-Perspektive
     * @param r      Rotwert des Nebels
     * @param g      Grünwert des Nebels
     * @param b      Blauwert des Nebels
     */
    public void render(Camera camera, float r, float g, float b) {
        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadFogColor(r, g, b);
        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        bindTextures();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    /**
     * Diese Funktion übergibt den Shader der Skybox.
     *
     * @return {@link SkyboxShader}
     */
    public SkyboxShader getShader() {
        return shader;
    }

    /**
     * Diese Funktion hinterlegt die Tages- und Nachttextur für die Skybox
     * sowie den Vermischungsfaktor der beiden.
     */
    private void bindTextures() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureNight);
        shader.loadBlendFactor(fade);
    }

    /**
     * Diese Funktion hinterlegt Vermischungsfaktor der Tages- und Nachttextur für die Skybox.
     *
     * @param fade Vermischungsfaktor der Tages- und Nachttextur
     */
    public void setFade(float fade) {
        this.fade = fade;
    }
}
