package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

/**
 * Diese Klasse verwaltet das Fenster, in dem die OpenGL Anwendung läuft.
 */
public class DisplayManager {

    /**
     * Breite des Fensters
     */
    private static final int WIDTH = 1920;
    /**
     * Höhe des Fensters
     */
    private static final int HEIGHT = 1080;
    /**
     * maximale FramesProSeconds (Bildwiederholungsrate)
     */
    private static final int FPS_CAP = 120;

    /**
     * Zeitpunkt, zu dem das letzte Bild gerendert wurde (Millisekunden)
     */
    private static long lastFrameTime;
    /**
     * Differenz zwischen letztem Bild und aktuellem Bild (Millisekunden)
     */
    private static float delta;

    /**
     * Diese Funktion erstellt ein Fenster, in dem die OpenGL-Anwendung läuft.
     */
    public static void createDisplay() {
        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("Cave of Fails");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();

        Mouse.setGrabbed(true);
    }

    /**
     * Diese Funktion aktualisiert den Inhalt des Fensters.
     */
    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = currentFrameTime - lastFrameTime;
        lastFrameTime = currentFrameTime;
    }

    /**
     * Diese Funktion gibt die Zeit zurück, wie lange das letzte Bild gezeigt worden ist.
     *
     * @return Differenz in Millisekunden
     */
    public static float getFrameTime() {
        return delta;
    }

    /**
     * Diese Funktion schließt das Fenster, in dem die OpenGL-Anwendung läuft.
     */
    public static void closeDisplay() {
        Display.destroy();
    }

    /**
     * Diese Funktion gibt die aktuelle Systemzeit zurück.
     *
     * @return Zeit in Millisekunden
     */
    public static long getCurrentTime() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }
}
