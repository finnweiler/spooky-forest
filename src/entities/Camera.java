package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Diese Klasse repräsentiert eine Kamera mit der zugehörigen Position, Neigung und Rotation.
 */
public class Camera {

    /**
     * Höhe der Kamera über dem Boden
     */
    private final static float HEIGHT = 7;

    /**
     * globale Position der Kamera
     */
    private final Vector3f position = new Vector3f(0, 1, 0);
    /**
     * Neigung der Kamera nach oben / unten
     */
    private float pitch;
    /**
     * Rotation der Kamera um die y-Achse
     */
    private float yaw;

    /**
     * Spieler ({@link Player}), der mit der Kamera verbunden ist
     */
    private final Player player;

    /**
     * Die {@link Camera} wird mit dem dazugehörigen {@link Player} initialisiert.
     *
     * @param player Spieler ({@link Player}), der mit der Kamera verbunden ist
     */
    public Camera(Player player) {
        this.player = player;
    }

    /**
     * Diese Funktion syncronisiert die Position und Rotation des verbundenen {@link Player} mit der Kamera.
     * Sie muss jeden Frame neu aufgerufen werden.
     */
    public void update() {
        this.pitch = player.getHeadPitch();
        this.yaw = -player.getRotY() + 180;
        this.position.setZ(player.getPosition().getZ());
        this.position.setY(player.getPosition().getY() + HEIGHT);
        this.position.setX(player.getPosition().getX());
    }

    /**
     * Diese Funktion übergibt die globale Position der Kamera.
     *
     * @return Position der Kamera
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Diese Funktion übergibt die Neigung der Kamera nach oben / unten.
     *
     * @return Neigung der Kamera
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Die Funktion übergibt die Rotation der Kamera um die y-Achse.
     *
     * @return Rotation der Kamera
     */
    public float getYaw() {
        return yaw;
    }
}
