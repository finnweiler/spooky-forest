package gui;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;
import toolbox.Maths;

import java.util.List;

public class GuiRenderer {

    private final RawModel quad;
    private final GuiShader shader;

    public GuiRenderer(Loader loader) {
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1}; // Positionen, wo das GUI angezeigt werden soll (Matrix)
        quad = loader.loadToVAO(positions);
        shader = new GuiShader(); // Shadeer = Das was im GPU RAM ist
    }

    // Binded VAO
    // Kriegt nur VAO Slot 0, weil brauchen nur die Positionen
    // Texture laden wir ganz normal über den Loader
    public void render(List<GuiTexture> guis) {
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        // Loop durch alle GUIs
        // Machen schieben die Textur auf unsere Fläche
        // Machen ALpha blending an, damit durchsichtige Pixel nicht schwarz gerednered werden
        // Mit Transformationsmatrix können wir skalieren (größer, kleiner, position)
        // Am ende matrix mit Triangle Strips anstatt triangles drawen
        // TRIANGLE STRIPS: generieren mit menge an punkten automatisch dreiecke
        for (GuiTexture gui : guis) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
            Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
            shader.loadTransformation(matrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    // Shader cleanen
    public void cleanUp() {
        shader.cleanUp();
    }
}
