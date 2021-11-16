package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import gui.GuiRenderer;
import gui.GuiTexture;
import models.TexturedModel;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.w3c.dom.Text;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

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

        RawModel playerModel = OBJFileLoader.loadObjModel("fern", loader);
        ModelTexture playerTexture = new ModelTexture(loader.loadTexture("fern"));
        TexturedModel texturedPlayerModel = new TexturedModel(playerModel, playerTexture);
        Player player = new Player(texturedPlayerModel, new Vector3f(-10, 0, -20), 0,0,0,1);

        /** Player */

        RawModel fernRawModel = OBJFileLoader.loadObjModel("fern", loader);
        ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
        fernTexture.setTransparent(true);
        //fernTexture.setFakeLit(true);
        TexturedModel fernTexturedModel = new TexturedModel(fernRawModel, fernTexture);
        Entity fernEntity = new Entity(fernTexturedModel, new Vector3f(-10,0,-17),0,0,0,1);

        RawModel model = OBJLoader.loadObjModel("christmastreelights", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("christmastreelights"));
        texture.setShineDamper(15);
        texture.setReflectivity(1);
        TexturedModel texturedModel = new TexturedModel(model, texture);
        Entity entity = new Entity(texturedModel, new Vector3f(80,30,-250),0,0,0,10);



        RawModel cave = OBJLoader.loadObjModel("cavefinal", loader);
        ModelTexture caveTexture = new ModelTexture(loader.loadTexture("cavefinal"));

        texture.setShineDamper(15);
        texture.setReflectivity(1);
        TexturedModel caveTexturedModel = new TexturedModel(cave, caveTexture);
        Entity caveModel = new Entity(caveTexturedModel, new Vector3f(80,20,-100),0,0,0,1);

        RawModel dickeCave = OBJLoader.loadObjModel("dickemap", loader);
        ModelTexture dickeCavetexture = new ModelTexture(loader.loadTexture("dickemap"));
        dickeCavetexture.setShineDamper(15);
        dickeCavetexture.setReflectivity(1);
        TexturedModel dickeCaveModel = new TexturedModel(dickeCave, dickeCavetexture);
        Entity dickeCaveEnt = new Entity(dickeCaveModel, new Vector3f(65,25,-105),0,0,0,1);


        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1, 0.01f, 0.001f)));
        lights.add(new Light(new Vector3f(+600,3000, -500), new Vector3f(1.0f,1.0f,1.0f)));

        Camera camera = new Camera(player);

        MasterRenderer renderer = new MasterRenderer(loader);

        List<GuiTexture> guis = new ArrayList<GuiTexture>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("forest"), new Vector2f(0.5f, -0.2f), new Vector2f(1.5f, 1.5f));
        guis.add(gui);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

        int counter = 0;

        while (!Display.isCloseRequested()) {
            player.move(terrain);
            camera.update();
            lights.get(0).setPosition(new Vector3f(camera.getPosition().getX(), camera.getPosition().getY() + 7, camera.getPosition().getZ()));
            //entity.increasePosition(0, 0,-0.01f);
            // entity.increaseRotation(0,0.5f,0);


            renderer.processTerrain(terrain);
            //renderer.processTerrain(terrain2);
            renderer.processEntity(entity);
            renderer.processEntity(fernEntity);
            renderer.processEntity(player);
            renderer.processEntity(caveModel);
            renderer.processEntity(dickeCaveEnt);


            renderer.render(lights, camera);

            guis.remove(gui);
            if (counter > 500) {
                guis.remove(gui);
            }

            guiRenderer.render(guis);
            counter++;

            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
