package textures;

/**
 * Diese Klasse ermöglicht die Referenz auf eine Textur, gebunden an ein Model.
 */
public class ModelTexture {

    /**
     * Referenz zur geladenen Textur
     */
    private final int textureId;

    /**
     * Dämpung des Scheins
     */
    private float shineDamper = 1;
    /**
     * Reflektivität eines Objekts
     */
    private float reflectivity = 0;

    /**
     * Transparenz des gerenderten Bilds
     */
    private boolean isTransparent = false;
    /**
     * Information, ob pysikalisch richtige Beleuchtung genutzt werden soll
     */
    private boolean isFakeLit = false;

    /**
     * Anzahl der Texturen für das gleiche Modell (ermöglicht mehrere Texturen pro Texturdatei)
     */
    private final int numberOfRows = 1;


    /**
     * Eine Modeltextur wird mithilfe der Referenz zur Textur erstellt.
     *
     * @param id Referenz zur geladenen Textur
     */
    public ModelTexture(int id) {
        this.textureId = id;
    }

    /**
     * Diese Funktion übergibt die Anzahl der Texturen für das gleiche Modell.
     *
     * @return Anzahl der Texturen
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Diese Funktion übergibt die Referenz zur Textur.
     *
     * @return Referenz zur geladenen Textur
     */
    public int getId() {
        return textureId;
    }

    /**
     * Diese Funktion übergibt die Dämpung des Scheins.
     *
     * @return Dämpung des Scheins
     */
    public float getShineDamper() {
        return shineDamper;
    }

    /**
     * Diese Funktion hinterlegt die Dämpung des Scheins.
     *
     * @param shineDamper Dämpung des Scheins
     */
    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    /**
     * Diese Funktion übergibt die Reflektivität eines Objekts
     *
     * @return Reflektivität eines Objekts
     */
    public float getReflectivity() {
        return reflectivity;
    }

    /**
     * Diese Funktion hinterlegt die Reflektivität eines Objekts.
     *
     * @param reflectivity Reflektivität eines Objekts
     */
    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    /**
     * Diese Funktion übergibt die Transparenz des gerenderten Bilds.
     *
     * @return Transparenz des gerenderten Bilds
     */
    public boolean isTransparent() {
        return isTransparent;
    }

    /**
     * Diese Funktion hinterlegt die Transparenz des gerenderten Bilds.
     *
     * @param transparent Transparenz des gerenderten Bilds
     */
    public void setTransparent(boolean transparent) {
        isTransparent = transparent;
    }

    /**
     * Diese Funktion übergibt die Information, ob alle Normalenvektoren des Models nach oben zeigen sollen.
     *
     * @return Information, ob alle Normalenvektoren des Models nach oben zeigen sollen
     */
    public boolean isFakeLit() {
        return isFakeLit;
    }

    /**
     * Diese Funktion hinterlegt die Information, ob pysikalisch richtige Beleuchtung genutzt werden soll.
     *
     * @param fakeLit Information, ob pysikalisch richtige Beleuchtung genutzt werden soll
     */
    public void setFakeLit(boolean fakeLit) {
        isFakeLit = fakeLit;
    }
}
