package textures;

public class ModelTexture {

    private int textureId;

    private float shineDamper = 1;
    private float reflectivity = 0;

    private boolean isTransparent = false;
    private boolean isFakeLit = false;

    public ModelTexture(int id) {
        this.textureId = id;
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
