package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
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

        RawModel model = OBJLoader.loadObjModel("dragon", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("blue"));
        texture.setShineDamper(15);
        texture.setReflectivity(1);
        TexturedModel texturedModel = new TexturedModel(model, texture);
        Entity entity = new Entity(texturedModel, new Vector3f(0,0.1f,-25),0,0,0,1);

        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(10,100, -10), new Vector3f(1,0,1)));
        lights.add(new Light(new Vector3f(10,100, 50), new Vector3f(0,1,1)));

        Camera camera = new Camera(player);

        MasterRenderer renderer = new MasterRenderer();

        while (!Display.isCloseRequested()) {
            player.move(terrain);
            camera.update();
            //entity.increasePosition(0, 0,-0.01f);
            entity.increaseRotation(0,0.5f,0);


            renderer.processTerrain(terrain);
            //renderer.processTerrain(terrain2);
            renderer.processEntity(entity);
            renderer.processEntity(fernEntity);
            renderer.processEntity(player);
            renderer.render(lights, camera);

            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
