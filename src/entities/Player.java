package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrain.Terrain;

/**
 * Diese Klasse repräsentiert einen Spieler mit Bewegungsprofilen.
 */
public class Player extends Entity {

    /**
     * Geschwindigkeit des Spielers bei der Vorwärtsbewegung
     */
    private static final float FORWARDS_SPEED = 0.015f;
    /**
     * Geschwindigkeit des Spielers bei der Seitwärtsbewegung
     */
    private static final float SIDEWARDS_SPEED = 0.01f;

    /**
     * Erdbeschleunigung, die auf den Spieler wirkt
     */
    private static final float GRAVITY = -0.00015f;
    /**
     * Sprungkraft des Spielers
     */
    private static final float JUMP_POWER = 0.04f;
    /**
     * Distanz, die ein Spieler pro Frame einen Hügel hinablaufen kann, ohne zu fallen
     */
    private static final float DOWN_WALK_DISTANCE = 0.04f;

    /**
     * aktuelle Vorwärtsbewegung des Spielers
     */
    private float currentForwardsSpeed = 0;
    /**
     * aktuelle Seitwärtsbewegung des Spielers
     */
    private float currentSidewardsSpeed = 0;
    /**
     * aktuelle Vertikalbewegung des Spielers
     */
    private float verticalSpeed = 0;

    /**
     * Information, ob der Spieler gerade auf Höhe des Terrains ist
     */
    private boolean flying = false;
    /**
     * aktuelle Neigung des Spielerkopfs
     */
    private float headPitch = 0;

    /**
     * Erstellt einen neuen Spieler.
     *
     * @param model    Das Modell, das den Spieler repräsentieren soll
     * @param position Die Position des Spielers im Raum
     * @param rotX     Die x-Rotation des Spielers
     * @param rotY     Die y-Rotation des Spielers
     * @param rotZ     Die z-Rotation des Spielers
     * @param scale    Die Skalierung des Spielers
     */
    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    /**
     * Diese Funktion übergibt die aktuelle Neigung des Spielerkopfs.
     *
     * @return aktuelle Neigung des Spielerkopfs
     */
    public float getHeadPitch() {
        return headPitch;
    }

    /**
     * Diese Funktion aktualisiert die Position und Rotation des Spielers auf Grundlage der Tastatur- und Mauseingaben.
     * Diese Funktion berücksichtigt dabei den Untergrund auf dem Spiele steht, um die Höhe dementsprechend anzupassen.
     *
     * @param terrain Untergrund
     */
    public void move(Terrain terrain) {
        checkInputs();
        checkMouse();

        float passedTime = DisplayManager.getFrameTime();
        float distanceForwards = currentForwardsSpeed * passedTime;
        float distanceSidewards = currentSidewardsSpeed * passedTime;

        float dx, dz;
        // Berechne die Translation abhängig von der Blickrichtung des Spielers
        dx = (float) (Math.sin(Math.toRadians(super.getRotY())) * distanceForwards);
        dz = (float) (Math.cos(Math.toRadians(super.getRotY())) * distanceForwards);
        dx += (float) (Math.cos(Math.toRadians(super.getRotY())) * distanceSidewards);
        dz += -(float) (Math.sin(Math.toRadians(super.getRotY())) * distanceSidewards);

        // Wenn der Spieler sich gleichzeitig vorwärts und seitwärts bewegt, bremse diesen etwas.
        if (distanceForwards != 0 && distanceSidewards != 0) {
            dx *= 0.7;
            dz *= 0.7;
        }
        verticalSpeed += GRAVITY * passedTime;
        float distanceVertical = verticalSpeed * passedTime;
        super.translate(-dx, distanceVertical, -dz);
        float terrainHeight = terrain.getHeight(getPosition().getX(), getPosition().getZ());

        // Wenn der Spieler unter den Boden fällt, setze diesen zurück auf den Boden
        if (getPosition().getY() < terrainHeight) {
            verticalSpeed = 0;
            flying = false;
            super.getPosition().setY(terrainHeight);
            // Wenn der Spieler einen flachen Hang hinabläuft, soll dieser nicht in den zustand "fliegen" kommen
        } else if (!flying || getPosition().getY() < terrainHeight + DOWN_WALK_DISTANCE) {
            verticalSpeed = 0;
            super.getPosition().setY(terrainHeight);
        }

        // Wenn der Spieler sich in die Nähe des Kartenrandes begibt, stoppe ihn
        if (getPosition().getX() < 35) {
            getPosition().setX(35);
        } else if (getPosition().getX() > 765) {
            getPosition().setX(765);
        }
        if (getPosition().getZ() < 35) {
            getPosition().setZ(35);
        } else if (getPosition().getZ() > 765) {
            getPosition().setZ(765);
        }
    }

    /**
     * Diese Funktion lässt den Spieler springen.
     */
    private void jump() {
        flying = true;
        verticalSpeed = JUMP_POWER;
    }

    /**
     * Diese Funktion überprüft die Tastatureingaben und passt ggf. die aktuelle Geschwindigkeit des Spielers an.
     */
    private void checkInputs() {
        if (!flying) {
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                jump();
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                this.currentForwardsSpeed = -FORWARDS_SPEED;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                this.currentForwardsSpeed = FORWARDS_SPEED;
            } else {
                this.currentForwardsSpeed = 0;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                this.currentSidewardsSpeed = SIDEWARDS_SPEED;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                this.currentSidewardsSpeed = -SIDEWARDS_SPEED;
            } else {
                this.currentSidewardsSpeed = 0;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                this.currentForwardsSpeed *= 10;
                verticalSpeed *= 5;
            }
        }
    }

    /**
     * Diese Funktion überprüft die Mausbewegung und passt ggf. die aktuelle Rotation des Spielers und dessen Kopfes an.
     */
    private void checkMouse() {
        float dx = -Mouse.getDX() * 0.1f;
        rotate(0, dx, 0);
        headPitch -= Mouse.getDY() * 0.1f;
    }

    /**
     * Diese Funktion übergibt die absolute Gehgeschwindigkeit des Spielers auf der xz-Ebene zurück.
     *
     * @return Gehgeschwindigkeit des Spielers
     */
    public float getWalkingSpeed() {
        return Math.abs(currentForwardsSpeed) + Math.abs(currentSidewardsSpeed);
    }
}
