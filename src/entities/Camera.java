package entities;

import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private final static float HEIGHT = 7;                            // Höhe der Kamera über dem Boden

    private final Vector3f position = new Vector3f(0, 1, 0); // Globale Position der Kamera
    private float pitch;                                              // Neigung der Kamera nach oben / unten
    private float yaw;                                                // Rotation der Kamera um die Y Achse

    private final Player player;                                      // Player object, das mit der Kamera verbunden ist.

    public Camera(Player player) {
        this.player = player;
    }

    /**
     * Diese Funktion syncronisiert die Position und Rotation des verbundenen Players mit der Kamera.
     * Sie muss jeden Frame neu aufgerufen werden.
     */
    public void update() {
        this.pitch = player.getHeadPitch();
        this.yaw = -player.getRotY() + 180;
        this.position.setZ(player.getPosition().getZ());
        this.position.setY(player.getPosition().getY() + HEIGHT);
        this.position.setX(player.getPosition().getX());
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}
