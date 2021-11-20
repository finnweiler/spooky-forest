package renderEngine;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import terrain.Terrain;
import terrain.TerrainShader;
import textures.TerrainTexturePack;
import toolbox.Maths;

import java.util.List;

/**
 * Diese Klasse verwaltet den Renderprozess des Terrains.
 */
public class TerrainRenderer {

    private TerrainShader shader;

    /**
     * Diese Funktion erstellt einen neuen TerrainRenderer, der zum Rendern von Terrainstücken benötigt wird.
     * @param shader TerrainShader der zum Rendern verwendet werden soll.
     * @param projectionMatrix Projektionsmatrix
     */
    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
    }

    /**
     * Diese Funktion rendert eine Liste von Terrains aus dem Gitter.
     * In der Beispielszene wird nur ein Terrain verwendet.
     *
     * @param terrains Alle Terrains aus dem Gitter, die dargestellt werden sollen.
     */
    public void render(List<Terrain> terrains) {
        for (Terrain terrain : terrains) {
            prepareTerrain(terrain);
            loadTranslationMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTexturedModel();
        }
    }

    /**
     * Diese Funktion binded die Modell- und Texturdaten für ein Terrain Objekt in OpenGl,
     * um dieses danach rendern zu können.
     *
     * @param terrain Terrain
     */
    private void prepareTerrain(Terrain terrain) {
        RawModel model = terrain.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        bindTextures(terrain);
        shader.loadShineVariables(1, 0);
    }

    /**
     * Das das Terrain mit bis zu vier verschiedenen Texturen texturiert werden kann, bindet diese Funktion
     * vier mögliche Texturen aus dem ShaderPack und die BlendMap, die aussagt, wie diese Texturen verblendet
     * werden sollen.
     *
     * @param terrain Terrain
     */
    private void bindTextures(Terrain terrain) {
        TerrainTexturePack texturePack = terrain.getTexturePack();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getId());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getRTexture().getId());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getGTexture().getId());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBTexture().getId());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getId());
    }

    /**
     * Diese Funktion unbindend die Modell- und Texturdaten für ein Terrain Objekt in OpenGL,
     * wenn diese nach dem Rendern nicht mehr benötigt werden.
     */
    private void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    /**
     * Diese Funktion lädt die Transformationsmatrix für ein Terrain Objekt in den Shader.
     * Dabei handelt es sich praktisch gesehen um eine Translationsmatrix, da die Gitterstücke aus
     * dem Terrain nur verschoben werden und nie skaliert oder rotiert,
     * @param terrain Entity
     */
    private void loadTranslationMatrix(Terrain terrain) {
        Matrix4f transformationMatrix =
                Maths.createTransformationMatrix(
                        new Vector3f(terrain.getX(), 0, terrain.getZ()),
                        0, 0, 0, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
