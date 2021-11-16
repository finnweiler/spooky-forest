package textures;

/**
 * Da der Untergrund mehrere Texturen darstellt, muss dieser nicht nur eine Textur, sondern gleich ein ganzes Texturenpaket bekommen.
 * Diese Klasse hält bis zur vier Texturen, die auf den Untergrund angewendet werden können.
 */
public class TerrainTexturePack {

    private TerrainTexture backgroundTexture;   // Standardtextur
    private TerrainTexture rTexture;            // Textur aktiviert durch r-Channel
    private TerrainTexture gTexture;            // Textur aktiviert durch g-Channel
    private TerrainTexture bTexture;            // Textur aktiviert durch b-Channel

    public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture, TerrainTexture bTexture) {
        this.backgroundTexture = backgroundTexture;
        this.rTexture = rTexture;
        this.gTexture = gTexture;
        this.bTexture = bTexture;
    }

    public TerrainTexture getBackgroundTexture() {
        return backgroundTexture;
    }

    public TerrainTexture getrTexture() {
        return rTexture;
    }

    public TerrainTexture getgTexture() {
        return gTexture;
    }

    public TerrainTexture getbTexture() {
        return bTexture;
    }
}
