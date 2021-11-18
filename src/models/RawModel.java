package models;

/**
 * Diese Klasse repräsentiert ein in OpenGL geladenes Vertex Array Objekt (VAO).
 */
public class RawModel {

    /**
     * Vertex Array Object-ID des Modells
     */
    private final int vaoID;
    /**
     * Anzahl der Eckpunkte des Modells
     */
    private final int vertexCount;

    /**
     * Ein {@link RawModel} wird mithilfe der Vertex Array Object-ID und der Anzahl der Eckpunkte erstellt.
     *
     * @param vaoID       Vertex Array Object-ID
     * @param vertexCount Anzahl der Eckpunkte
     */
    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    /**
     * Diese Funktion übergibt die Vertex Array Object-ID dieses Modells.
     *
     * @return Vertex Array Object-ID des Modells
     */
    public int getVaoID() {
        return vaoID;
    }

    /**
     * Diese Funktion übergibt die Anzahl der Eckpunkte dieses Modells.
     *
     * @return Anzahl der Eckpunkte des Modells
     */
    public int getVertexCount() {
        return vertexCount;
    }
}
