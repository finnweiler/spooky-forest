package models;

/**
 * Diese Klasse repräsentiert ein in OpenGL geladenes Vertex Array Objekt (VAO)
 */
public class RawModel {

    private final int vaoID;
    private final int vertexCount;

    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
