package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {

    private Vector3f position;      // Globale Position des Lichts
    private Vector3f color;         // Farbe des Lichts
    private Vector3f attenuation = new Vector3f(1, 0, 0); // Abschwächung des Lichts, standartmäßig deaktiviert.

    /**
     * Erstellt ein neues Licht ohne Abschwächung.
     * @param position Position
     * @param color Farbe
     */
    public Light(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    /**
     * Erstellt ein neues Licht mit den übergebenen Abschwächungsparametern
     * @param position Position
     * @param color Farbe
     * @param attenuation Abschwächung
     */
    public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
