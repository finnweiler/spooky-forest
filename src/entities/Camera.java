package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private final float HEIGHT = 7;

    private Vector3f position = new Vector3f(0,1,0);
    private float pitch;
    private float yaw;

    private Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public void update() {
        this.pitch = player.getHeadPitch();
        this.yaw = -player.getRotY();
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
