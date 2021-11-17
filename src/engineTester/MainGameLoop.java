package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import gui.GuiRenderer;
import gui.GuiTexture;
import renderEngine.Loader;
import models.OBJLoader;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import sound.AudioMaster;
import sound.Source;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();

        /** Music Start */
        AudioMaster.init();
        AudioMaster.setListenerData(0, 0, 0);

        int bufferBackgroundMusic = AudioMaster.loadSound("sound/music_short_short.wav");
        Source source = new Source();
        source.setVolume(0.4f);
        source.setLooping(true);
        source.play(bufferBackgroundMusic);
        /** Music End */

        /** Terrain Start */

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap, "heightmap");
        /** Terrain End */

        /** Vegetation Start */

        /** Vegetation End */

        /** GUI Start */
        List<GuiTexture> guis = new ArrayList<GuiTexture>();
        GuiTexture intro = new GuiTexture(loader.loadTexture("forest"), new Vector2f(0.5f, -0.2f), new Vector2f(1.5f, 1.5f));

        GuiTexture startmenu = new GuiTexture(loader.loadTexture("caveoffailes_menu"), new Vector2f(0.4f, -0.2f), new Vector2f(1.5f, 1.5f));

        GuiRenderer guiRenderer = new GuiRenderer(loader);
        // Add Intro
        guis.add(intro);


        int counter = 0;
        boolean escaped = true;
        /** GUI End */


        MasterRenderer renderer = new MasterRenderer(loader);


        EntityGameLoop.prepare(terrain, loader, renderer);


        boolean closeRequested = false;
        while (!Display.isCloseRequested() && !closeRequested) {

            EntityGameLoop.loop(renderer, terrain, !escaped);

            renderer.processTerrain(terrain);



            /** Show Intro */
            if (counter < 200) {
                counter++;
            } else if (counter == 200) {
                counter = 501;
                guis.clear();
                guis.add(startmenu);
                escaped = true;
                Mouse.setGrabbed(false);
            }


            /** Render GUIs */
            guiRenderer.render(guis);

            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                escaped = true;
                guis.add(startmenu);
                guiRenderer.render(guis);
                Mouse.setGrabbed(false); // Disable Mouse
            }

            if (escaped && Mouse.isButtonDown(0)) {
                // Check if in Coords
                int mouseX = Mouse.getX();
                int mouseY = Mouse.getY();
                if (mouseX < 1070 && mouseX > 860) {
                    if (mouseY < 520 && mouseY > 460) {
                        escaped = false;
                        guis.clear();
                        Mouse.setGrabbed(true); // Enable mouse
                    } else if (mouseY < 360 && mouseY > 285) {
                        closeRequested = true;
                    }
                    guis.remove(startmenu);
                }
            }

            DisplayManager.updateDisplay();
        }

        AudioMaster.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}