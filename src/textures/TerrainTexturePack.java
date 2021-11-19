package textures;

/**
 * Diese Klasse hält bis zur vier Texturen, die auf den Untergrund angewendet werden können.
 * Da der Untergrund mehrere Texturen darstellt, muss dieser nicht nur eine Textur, sondern ein ganzes Texturenpaket bekommen.
 */
public class TerrainTexturePack {

    /**
     * Standard-Hintergrundtextur
     */
    private final TerrainTexture backgroundTexture;
    /**
     * Textur, aktiviert durch den R-Kanal
     */
    private final TerrainTexture rTexture;
    /**
     * Textur, aktiviert durch den G-Kanal
     */
    private final TerrainTexture gTexture;
    /**
     * Textur, aktiviert durch den B-Kanal
     */
    private final TerrainTexture bTexture;


    /**
     * Ein {@link TerrainTexturePack} wird mit den Texturen für den Hintergrund, R-, G- und B-Kanal erstellt.
     *
     * @param backgroundTexture Textur für den Hintergrund
     * @param rTexture          Textur für den R-Kanal
     * @param gTexture          Textur für den G-Kanal
     * @param bTexture          Textur für den B-Kanal
     */
    public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture, TerrainTexture bTexture) {
        this.backgroundTexture = backgroundTexture;
        this.rTexture = rTexture;
        this.gTexture = gTexture;
        this.bTexture = bTexture;
    }

    /**
     * Diese Funktion übergibt die Standard-Hintergrundtextur.
     *
     * @return Standard-Hintergrundtextur
     */
    public TerrainTexture getBackgroundTexture() {
        return backgroundTexture;
    }

    /**
     * Diese Funktion übergibt die Textur, aktiviert durch den R-Kanal.
     *
     * @return Textur, aktiviert durch den R-Kanal
     */
    public TerrainTexture getRTexture() {
        return rTexture;
    }

    /**
     * Diese Funktion übergibt die Textur, aktiviert durch den G-Kanal.
     *
     * @return Textur, aktiviert durch den G-Kanal
     */
    public TerrainTexture getGTexture() {
        return gTexture;
    }

    /**
     * Diese Funktion übergibt die Textur, aktiviert durch den B-Kanal.
     *
     * @return Textur, aktiviert durch den B-Kanal
     */
    public TerrainTexture getBTexture() {
        return bTexture;
    }
}
