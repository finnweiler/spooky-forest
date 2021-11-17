package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

/**
 * Diese Klasse verwaltet das Fenster, in dem die OpenGL Anwendung läuft.
 */
public class DisplayManager {

    private static final int WIDTH = 1920;  // Breite des Fensters
    private static final int HEIGHT = 1080; // Höhe des Fensters
    private static final int FPS_CAP = 120; // Maximale FPS

    private static long lastFrameTime;      // Zeitpunkt zudem das letzte Bild gerendert wurde in Millisekunden
    private static float delta;             // Differenz zwischen letztem Bild und aktuellem Bild in Millisekunden

    /**
     * Erstellt ein Fenster, in dem die OpenGL Anwendung läuft.
     */
    public static void createDisplay() {

        ContextAttribs attribs = new ContextAttribs(3,2)
        .withForwardCompatible(true)
        .withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("Lost In The Woods");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();

        Mouse.setGrabbed(true);
    }

    /**
     * Aktualisiert den Inhalt des Fensters
     */
    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = currentFrameTime - lastFrameTime;
        lastFrameTime = currentFrameTime;
    }

    /**
     * Gibt die Zeit zurück, wie lange das letzte Bild gezeigt worden ist.
     * @return Differenz in Millisekunden
     */
    public static float getFrameTime() {
        return delta;
    }

    /**
     * Schließt das Fenster, in dem die OpenGL Anwendung läuft.
     */
    public static void closeDisplay() {
        Display.destroy();
    }

    /**
     * Gibt die aktuelle Systemzeit zurück
     * @return Zeit in Millisekunden
     */
    public static long getCurrentTime() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }
}
