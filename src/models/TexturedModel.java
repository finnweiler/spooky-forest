package models;

import textures.ModelTexture;

/**
 * Diese Klasse repräsentiert ein in OpenGL geladenes Vertex Array Buffer (VAO) inkl. dessen Textur.
 */
public class TexturedModel {

    private final RawModel rawModel;
    private final ModelTexture texture;

    public TexturedModel(RawModel rawModel, ModelTexture modelTexture) {
        this.rawModel = rawModel;
        this.texture = modelTexture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getTexture() {
        return texture;
    }
}
