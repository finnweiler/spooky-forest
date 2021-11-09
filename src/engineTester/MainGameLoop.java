package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();

        Loader loader = new Loader();
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);

        RawModel model = OBJLoader.loadObjModel("dragon", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("blue"));
        texture.setShineDamper(15);
        texture.setReflectivity(1);
        TexturedModel texturedModel = new TexturedModel(model, texture);
        Entity entity = new Entity(texturedModel, new Vector3f(0,-5,-25),0,0,0,1);

        Light light = new Light(new Vector3f(10,0, -10), new Vector3f(1,1,1));

        Camera camera = new Camera();

        while (!Display.isCloseRequested()) {
            camera.move();
            //entity.increasePosition(0, 0,-0.01f);
            entity.increaseRotation(0,0.5f,0);

            renderer.prepare();

            shader.start();
            shader.loadLight(light);
            shader.loadViewMatrix(camera);
            renderer.render(entity, shader);
            shader.start();

            DisplayManager.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
