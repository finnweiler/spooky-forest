package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.ModelTexture;

import java.util.List;
import java.util.Map;

/**
 * Diese Klasse beinhaltet die Renderlogik für alle Enitity Objekte.
 */
public class EntityRenderer {

    /**
     * Shader, der die Logik zum Rendern der Entities enthält
     */
    private final StaticShader shader;

    /**
     * Ein {@link EntityRenderer} wird mit einem {@link StaticShader} für Entities und einer Projektionsmatrix erstellt.
     *
     * @param shader           {@link StaticShader} für Entities
     * @param projectionMatrix Projektionsmatrix
     */
    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Diese Funktion rendert eine Hashmap gefüllt Listen von Entities. Entities sind hier nach Ihren TexturedModels gehashed, um diese
     * nur einmal Modell- und Texturendaten nur einmal pro Textur- und Modell zu Binden und Unbinden.
     *
     * @param entities Entities in Hashmap
     */
    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel model : entities.keySet()) {
            // Binded die Modelldaten und Texturen für ein Textured Modell
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity : batch) {
                // Rendert alle Entities in Ihrer individuellen Transformation
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    /**
     * Diese Funktion bindet die Modell- und Texturdaten für ein TexturedModel Objekt in OpenGl,
     * um dieses danach rendern zu können.
     *
     * @param texturedModel Texturiertes Modell
     */
    private void prepareTexturedModel(TexturedModel texturedModel) {
        RawModel model = texturedModel.getRawModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture texture = texturedModel.getTexture();
        shader.loadNumberOfRows(texture.getNumberOfRows());
        if (texture.isTransparent()) {
            MasterRenderer.disableCulling();
        }

        shader.loadIsFakeLit(texture.isFakeLit());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getId());
    }

    /**
     * Diese Funktion unbindend die Modell- und Texturdaten für ein TexturedModel Objekt in OpenGL,
     * wenn diese nach dem Rendern nicht mehr benötigt werden.
     */
    private void unbindTexturedModel() {
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    /**
     * Diese Funktion lädt die Transformationsmatrix für ein Entity Object in den Shader.
     *
     * @param entity Entity
     */
    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = entity.getTransformationMatrix();
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(entity.getTextureOffset());
    }
}
