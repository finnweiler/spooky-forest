package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrain.Terrain;

public class Player extends Entity {

    private static final float FORWARDS_SPEED = 0.03f;
    private static final float SIDEWARDS_SPEED = 0.02f;
    private static final float GRAVITY = -0.00015f;
    private static final float JUMP_POWER = 0.04f;

    private float currentForwardsSpeed = 0;
    private float currentSidewardsSpeed = 0;
    private float verticalSpeed = 0;

    private boolean flying = false;

    private float headPitch = 0;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public float getHeadPitch() {
        return headPitch;
    }

    public void move(Terrain terrain) {
        checkInputs();
        checkMouse();
        float passedTime = DisplayManager.getFrameTime();
        float distanceForwards = currentForwardsSpeed * passedTime;
        float distanceSidewards = currentSidewardsSpeed * passedTime;
        float dx = 0, dz = 0;
        dx = (float) (Math.sin(Math.toRadians(super.getRotY())) * distanceForwards);
        dz = (float) (Math.cos(Math.toRadians(super.getRotY())) * distanceForwards);
        dx += (float) (Math.cos(Math.toRadians(super.getRotY())) * distanceSidewards);
        dz += -(float) (Math.sin(Math.toRadians(super.getRotY())) * distanceSidewards);
        if (distanceForwards != 0 && distanceSidewards != 0) {
            dx *= 0.7;
            dz *= 0.7;
        }
        verticalSpeed += GRAVITY * passedTime;
        float distanceVertical = verticalSpeed * passedTime;
        super.increasePosition(-dx, distanceVertical, -dz);
        float terrainHeight = terrain.getHeight(getPosition().getX(), getPosition().getZ());
        if (getPosition().getY() < terrainHeight) {
            verticalSpeed = 0;
            flying = false;
            super.getPosition().setY(terrainHeight);
        }
    }

    private void jump() {
        flying = true;
        verticalSpeed = JUMP_POWER;
    }

    private void checkInputs() {
        if (!flying) {
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                jump();
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                this.currentForwardsSpeed = FORWARDS_SPEED;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                this.currentForwardsSpeed = -FORWARDS_SPEED;
            } else {
                this.currentForwardsSpeed = 0;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                this.currentSidewardsSpeed = -SIDEWARDS_SPEED;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                this.currentSidewardsSpeed = SIDEWARDS_SPEED;
            } else {
                this.currentSidewardsSpeed = 0;
            }
        }
    }

    private void checkMouse() {
        float dx = -Mouse.getDX() * 0.1f;
        increaseRotation(0,dx,0);

        headPitch -= Mouse.getDY() * 0.1f;
    }
}
