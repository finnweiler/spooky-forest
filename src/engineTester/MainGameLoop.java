package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import gui.GuiRenderer;
import gui.GuiTexture;
import models.TexturedModel;
import objConverter.OBJFileLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.w3c.dom.Text;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import sound.AudioMaster;
import sound.Source;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;

import java.util.ArrayList;
import java.util.List;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();

        Loader loader = new Loader();

        /** Terrain Stuff Start */

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(0,-1, loader, texturePack, blendMap, "heightmap");
        //Terrain terrain2 = new Terrain(-1,-1, loader, texturePack, blendMap, "heightmap");
        /** Terrain Stuff End */

        RawModel playerModel = OBJFileLoader.loadObjModel("futuremen", loader);
        ModelTexture playerTexture = new ModelTexture(loader.loadTexture("futuremen"));
        TexturedModel texturedPlayerModel = new TexturedModel(playerModel, playerTexture);
        Player player = new Player(texturedPlayerModel, new Vector3f(-10, 0, -20), 0,0,0,3);

        /** Player */

        RawModel fernRawModel = OBJFileLoader.loadObjModel("fern", loader);
        ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
        fernTexture.setTransparent(true);
        //fernTexture.setFakeLit(true);
        TexturedModel fernTexturedModel = new TexturedModel(fernRawModel, fernTexture);
        Entity fernEntity = new Entity(fernTexturedModel, new Vector3f(-10,0,-17),0,0,0,1);

        RawModel model = OBJLoader.loadObjModel("cavefinal", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("cavefinal"));

        texture.setShineDamper(15);
        texture.setReflectivity(1);
        TexturedModel texturedModel = new TexturedModel(model, texture);
        Entity entity = new Entity(texturedModel, new Vector3f(80,20,-100),0,0,0,1);

        RawModel dickeCave = OBJLoader.loadObjModel("dickemap", loader);
        ModelTexture dickeCavetexture = new ModelTexture(loader.loadTexture("dickemap"));
        dickeCavetexture.setShineDamper(15);
        dickeCavetexture.setReflectivity(1);
        TexturedModel dickeCaveModel = new TexturedModel(dickeCave, dickeCavetexture);
        Entity dickeCaveEnt = new Entity(dickeCaveModel, new Vector3f(65,25,-105),0,0,0,1);


        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(0,0,0), new Vector3f(1,1,1), new Vector3f(1, 0.01f, 0.001f)));
        lights.add(new Light(new Vector3f(0,200, -0), new Vector3f(0.0f,0.0f,0.0f)));

        Camera camera = new Camera(player);

        MasterRenderer renderer = new MasterRenderer();

        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

        List<GuiTexture> guis = new ArrayList<GuiTexture>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("forest"), new Vector2f(0.5f, -0.2f), new Vector2f(1.5f, 1.5f));
        guis.add(gui);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

        // 3D Sound
        AudioMaster.init();
        int buffer = AudioMaster.loadSound("engineTester/horrorsound.wav");
        Source source = new Source();
        source.setLooping(true);
        source.setPosition(65,25,-105);
        source.play(buffer);


        int counter = 0;

        while (!Display.isCloseRequested()) {
            player.move(terrain);
            camera.update();

            // AudioMaster.setListenerData(10000,10000,10000);

            lights.get(0).setPosition(new Vector3f(camera.getPosition().getX(), camera.getPosition().getY() + 7, camera.getPosition().getZ()));
            //entity.increasePosition(0, 0,-0.01f);
            // entity.increaseRotation(0,0.5f,0);

            renderer.processTerrain(terrain);
            //renderer.processTerrain(terrain2);
            renderer.processEntity(entity);
            // Gut kick
            renderer.processEntity(dickeCaveEnt);
            renderer.processEntity(fernEntity);
            renderer.processEntity(player);
            renderer.render(lights, camera);

            picker.update();
            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
            if (terrainPoint != null && Mouse.isButtonDown(0)) {
                fernEntity.setPosition(terrainPoint);
            }

            if (counter > 500) {
                guis.remove(gui);
            }

            guiRenderer.render(guis);
            counter++;


            DisplayManager.updateDisplay();
        }

        source.delete();
        AudioMaster.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
