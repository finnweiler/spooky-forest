package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private final float HEIGHT = 7;

    private Vector3f position = new Vector3f(0,1,0);
    private float pitch;
    private float yaw;
    private float roll;

    private float distanceFormPlayer = 50;
    private float angleAroundPlayer = 0;

    private Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();

        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
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

    public float getRoll() {
        return roll;
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));

        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().x - offsetZ;
        position.y = player.getPosition().y + verticalDistance;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFormPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFormPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFormPlayer -= zoomLevel;
    }

    private void calculatePitch() {
        if (Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            pitch -= pitchChange;
        }
    }

    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(3)) {
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }
}
