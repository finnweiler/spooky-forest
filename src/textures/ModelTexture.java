package textures;

public class ModelTexture {

    private int textureId;                  // Referenz zur geladenen Textur

    private float shineDamper = 1;          // Dämpung des Scheins
    private float reflectivity = 0;         // Reflektivität eines Objekts

    private boolean isTransparent = false;  // Gibt an, ob das PNG transparent gerendert werden soll
    private boolean isFakeLit = false;      // Gibt an, ob alle Normalenvektoren des Models nach oben zeigen sollen

    private int numberOfRows = 1;           // Falls eine Texturdatei mehrere Texturen für das gleiche Modell enthält,
                                            // kann dies hier angegeben werden

    public ModelTexture(int id) {
        this.textureId = id;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getId() {
        return textureId;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public void setTransparent(boolean transparent) {
        isTransparent = transparent;
    }

    public boolean isFakeLit() {
        return isFakeLit;
    }

    public void setFakeLit(boolean fakeLit) {
        isFakeLit = fakeLit;
    }
}
