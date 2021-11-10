package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrain.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();

        Loader loader = new Loader();

        ModelTexture terrainTexture = new ModelTexture(loader.loadTexture("grass"));
        Terrain terrain = new Terrain(0,-1, loader, terrainTexture);
        Terrain terrain2 = new Terrain(-1,-1, loader, terrainTexture);

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

        Light light = new Light(new Vector3f(10,100, -10), new Vector3f(1,1,1));

        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();

        while (!Display.isCloseRequested()) {
            camera.move();
            //entity.increasePosition(0, 0,-0.01f);
            entity.increaseRotation(0,0.5f,0);


            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processEntity(entity);
            renderer.processEntity(fernEntity);
            renderer.render(light, camera);

            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
