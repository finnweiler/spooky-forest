package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.OBJLoader;
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

        /** Terrain Start */

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(0,0, loader, texturePack, blendMap, "heightmap");
        /** Terrain End */

        /** Vegetation Start */
        ArrayList<Entity> vegetation = new ArrayList<>();

        RawModel tree1Model = OBJLoader.loadObjModel("trees/bigtree1", loader);
        ModelTexture tree1Texture = new ModelTexture(loader.loadTexture("trees/bigtree1"));
        //fernTexture.setFakeLit(true);
        for (int i = 0; i < 1000; i++) {
            TexturedModel tree1TexturedModel = new TexturedModel(tree1Model, tree1Texture);
            float x = (float) Math.random() * 800;
            float z = (float) Math.random() * 800;
            float y = terrain.getHeight(x, z);
            Vector3f n = terrain.getNormal(x, z);
            Entity treeEntity = new Entity(tree1TexturedModel,
                    new Vector3f(x,y,z),
                    0,(float) i,0,
                    (float) Math.random() * 2 + 2 );
            vegetation.add(treeEntity);
        }

        RawModel tree2Model = OBJLoader.loadObjModel("trees/oak1", loader);
        ModelTexture tree2Texture = new ModelTexture(loader.loadTexture("trees/oak1"));
        //fernTexture.setFakeLit(true);
        for (int i = 0; i < 500; i++) {
            TexturedModel tree2TexturedModel = new TexturedModel(tree2Model, tree2Texture);
            float x = (float) Math.random() * 800;
            float z = (float) Math.random() * 800;
            float y = terrain.getHeight(x, z);
            Vector3f n = terrain.getNormal(x, z);
            Entity treeEntity = new Entity(tree2TexturedModel,
                    new Vector3f(x,y,z),
                    0,(float) i,0,
                    (float) Math.random() * 2 + 4 );
            vegetation.add(treeEntity);
        }
        /** Vegetation End */

        /** Player Start */
        RawModel playerModel = OBJFileLoader.loadObjModel("fern", loader);
        ModelTexture playerTexture = new ModelTexture(loader.loadTexture("fern"));
        TexturedModel texturedPlayerModel = new TexturedModel(playerModel, playerTexture);
        Player player = new Player(texturedPlayerModel, new Vector3f(400, 0, 400), 0,0,0,1);
        Camera camera = new Camera(player);
        /** Player End */

        /** Lights Start */
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(0,0,0), new Vector3f(1,1,1), new Vector3f(1, 0.01f, 0.001f)));
        lights.add(new Light(new Vector3f(+600,3000, -500), new Vector3f(0f,0f,0f)));
        /** Lights End */


        RawModel model = OBJFileLoader.loadObjModel("dragon", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("blue"));
        texture.setShineDamper(15);
        texture.setReflectivity(1);
        TexturedModel texturedModel = new TexturedModel(model, texture);
        Entity entity = new Entity(texturedModel, new Vector3f(400,terrain.getHeight(400, 400),400),0,0,0,1);

        MasterRenderer renderer = new MasterRenderer(loader);

        while (!Display.isCloseRequested()) {
            player.move(terrain);
            camera.update();
            lights.get(0).setPosition(new Vector3f(camera.getPosition().getX(), camera.getPosition().getY() + 7, camera.getPosition().getZ()));
            //entity.increasePosition(0, 0,-0.01f);
            entity.increaseRotation(0,0.5f,0);


            renderer.processTerrain(terrain);
            renderer.processEntity(entity);
            for (Entity vegetationEntity: vegetation) {
                renderer.processEntity(vegetationEntity);
            }
            renderer.processEntity(player);
            renderer.render(lights, camera);

            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
