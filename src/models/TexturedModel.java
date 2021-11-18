package models;

import textures.ModelTexture;

/**
 * Diese Klasse repräsentiert ein in OpenGL geladenes Vertex Array Buffer (VAO) inkl. dessen Textur.
 */
public class TexturedModel {

    /**
     * {@link RawModel} des Modells
     */
    private final RawModel rawModel;
    /**
     * {@link ModelTexture} des Modells
     */
    private final ModelTexture texture;

    /**
     * Ein {@link TexturedModel} wird mithilfe des {@link RawModel} und der {@link ModelTexture} erstellt.
     *
     * @param rawModel     {@link RawModel} des Modells
     * @param modelTexture {@link ModelTexture} des Modells
     */
    public TexturedModel(RawModel rawModel, ModelTexture modelTexture) {
        this.rawModel = rawModel;
        this.texture = modelTexture;
    }

    /**
     * Diese Funktion übergibt das {@link RawModel} dieses Modells.
     *
     * @return {@link RawModel} des Modells
     */
    public RawModel getRawModel() {
        return rawModel;
    }

    /**
     * Diese Funktion übergibt die {@link ModelTexture} dieses Modells.
     *
     * @return {@link ModelTexture} des Modells
     */
    public ModelTexture getTexture() {
        return texture;
    }
}
