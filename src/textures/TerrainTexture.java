package textures;

/**
 * Diese Klasse repräsentiert eine Textur für das TerrainTexturePack.
 */
public class TerrainTexture {

    /**
     * Referenz zur geladenen Textur
     */
    private final int textureId;


    /**
     * Eine Terraintextur wird mithilfe der Referenz zur Textur erstellt.
     *
     * @param textureId Referenz zur geladenen Textur
     */
    public TerrainTexture(int textureId) {
        this.textureId = textureId;
    }

    /**
     * Diese Funktion übergibt die Referenz zur Textur.
     *
     * @return Referenz zur geladenen Textur
     */
    public int getId() {
        return textureId;
    }
}
