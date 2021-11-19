package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Diese Klasse repräsentiert einen Spieler mit Bewegungsprofilen.
 */
public class Light {

    /**
     * globale Position des Lichts
     */
    private Vector3f position;
    /**
     * Farbe des Lichts
     */
    private Vector3f color;
    /**
     * Abschwächung des Lichts (standartmäßig deaktiviert)
     */
    private Vector3f attenuation = new Vector3f(1, 0, 0); //

    /**
     * Es wird ein neues Licht ohne Abschwächung erstellt.
     *
     * @param position Position
     * @param color    Farbe
     */
    public Light(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    /**
     * Es wird ein neues Licht mit den übergebenen Abschwächungsparametern erstellt.
     *
     * @param position    Position
     * @param color       Farbe
     * @param attenuation Abschwächung
     */
    public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;
    }

    /**
     * Diese Funktion übergibt die Abschwächung des Lichts.
     *
     * @return Abschwächung des Lichts
     */
    public Vector3f getAttenuation() {
        return attenuation;
    }

    /**
     * Diese Funktion übergibt die globale Position des Lichts.
     *
     * @return globale Position des Lichts
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Diese Funktion hinterlegt eine neue Position des Lichts.
     *
     * @param position Position des Lichts
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    /**
     * Diese Funktion übergibt die Farbe des Lichts.
     *
     * @return Farbe des Lichts
     */
    public Vector3f getColor() {
        return color;
    }

    /**
     * Diese Funktion hinterlegt eine neue Farbe des Lichts.
     *
     * @param color Farbe des Lichts
     */
    public void setColor(Vector3f color) {
        this.color = color;
    }
}
