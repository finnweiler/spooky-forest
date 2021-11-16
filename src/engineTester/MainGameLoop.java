package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.Loader;
import models.OBJLoader;
import models.TexturedModel;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import skybox.SkyboxRenderer;
import sound.AudioMaster;
import sound.Source;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainGameLoop {

    private static List<Entity> generateVegetation(Terrain terrain, Loader loader, float density) {
        ArrayList<Entity> vegetation = new ArrayList<>();

        BufferedImage vegetationMapImage = null;
        try {
            vegetationMapImage = ImageIO.read(new File("res/vegetation.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int squareCount = vegetationMapImage.getHeight();
        float scalingFactor = 800 / (float) squareCount;

        RawModel tree1Model = OBJLoader.loadObjModel("trees/bigtree1", loader);
        ModelTexture tree1Texture = new ModelTexture(loader.loadTexture("trees/bigtree1"));

        RawModel tree2Model = OBJLoader.loadObjModel("trees/oak1", loader);
        ModelTexture tree2Texture = new ModelTexture(loader.loadTexture("trees/oak1"));

        RawModel tree3Model = OBJLoader.loadObjModel("trees/christmastree", loader);
        ModelTexture tree3Texture = new ModelTexture(loader.loadTexture("trees/christmastree"));

        RawModel flowerModel = OBJLoader.loadObjModel("trees/flower", loader);
        ModelTexture flowerTexture = new ModelTexture(loader.loadTexture("trees/flower"));

        for(int i=0;i<squareCount;i++){
            for(int j=0;j<squareCount;j++){
                float percentage = getPercentage(j, i, vegetationMapImage);
                if ((1 - percentage) * density > Math.random()) {

                    TexturedModel texturedModel = null;
                    float scale = 1;
                    float object = (float) Math.random();
                    if (object < 0.3) {
                        texturedModel = new TexturedModel(tree1Model, tree1Texture);
                        scale = (float) Math.random() * 2 + 2;
                    } else if (object < 0.6) {
                        texturedModel = new TexturedModel(tree2Model, tree2Texture);
                        scale = (float) Math.random() * 3 + 4;
                    } else if (object < 0.65) {
                        texturedModel = new TexturedModel(tree3Model, tree3Texture);
                        scale = (float) Math.random() * 1 + 25;
                    } else {
                        texturedModel = new TexturedModel(flowerModel, flowerTexture);
                        scale = (float) Math.random() * 4 + 15;
                    }

                    float offsetX = (float) Math.random() * 5 - 2.5f;
                    float offsetZ = (float) Math.random() * 5 - 2.5f;

                    float worldX = scalingFactor * j + offsetX;
                    float worldZ = scalingFactor * i + offsetZ;

                    float y = terrain.getHeight(worldX, worldZ);
                    Entity treeEntity = new Entity(texturedModel,
                            new Vector3f(worldX, y, worldZ),
                            0, (float) Math.random() * 360, 0,
                            scale);
                    vegetation.add(treeEntity);
                }
            }
        }

        return vegetation;
    }

    private static float getPercentage(int x, int y, BufferedImage image) {
        float MAX_PIXEL_COLOR = (float) Math.pow(256, 3);

        if (x < 0 || x >= image.getHeight() || y < 0 || y >= image.getHeight()) {
            return 0;
        }
        float percentage = -image.getRGB(x, y);
        percentage /= MAX_PIXEL_COLOR;
        return percentage;
    }

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

        int bufferFootsteps = AudioMaster.loadSound("sound/footsteps_short.wav");
        Source source2 = new Source();
        source2.setLooping(true);
        boolean stepping = false;
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
        List<Entity> vegetation = generateVegetation(terrain, loader, 0.005f);
        /** Vegetation End */

        /** Player Start */
        RawModel playerModel = OBJLoader.loadObjModel("assets/player", loader);
        ModelTexture playerTexture = new ModelTexture(loader.loadTexture("assets/player"));
        TexturedModel texturedPlayerModel = new TexturedModel(playerModel, playerTexture);
        Player player = new Player(texturedPlayerModel, new Vector3f(400, 0, 400), 0, 180, 0, 8);
        Camera camera = new Camera(player);
        /** Player End */

        /** Lights Start */
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector3f(1, 0.01f, 0.001f)));
        lights.add(new Light(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector3f(1, 0.01f, 0.001f)));
        lights.add(new Light(new Vector3f(+600, 3000, -500), new Vector3f(1f, 1f, 1f)));
        lights.add(new Light(new Vector3f(430, terrain.getHeight(430, 380) + 10, 380), new Vector3f(0, 0, 0), new Vector3f(1, 0.01f, 0.01f)));
        /** Lights End */

        /** Christmas Tree Start */
        RawModel christmasTreeModel = OBJLoader.loadObjModel("trees/christmastree", loader);
        ModelTexture christmasTreeTexture = new ModelTexture(loader.loadTexture("trees/christmastree"));
        TexturedModel texturedChristmasTreeModel = new TexturedModel(christmasTreeModel, christmasTreeTexture);
        Entity christmasTree = new Entity(texturedChristmasTreeModel, new Vector3f(430, terrain.getHeight(430, 380), 380), 0, 0, 0, 18);
        vegetation.add(christmasTree);

        RawModel treeDecorationModel = OBJLoader.loadObjModel("trees/shinytree", loader);
        ModelTexture treeDecorationTexture = new ModelTexture(loader.loadTexture("trees/shinytree"));
        treeDecorationTexture.setShineDamper(15);
        treeDecorationTexture.setReflectivity(1);
        TexturedModel treeDecorationTexturedModel = new TexturedModel(treeDecorationModel, treeDecorationTexture);
        Entity treeDecoration = new Entity(treeDecorationTexturedModel, new Vector3f(430, terrain.getHeight(430, 380), 380), 0, 0, 0, 18);
        vegetation.add(treeDecoration);
        /** Christmas Tree End */

        /** Dino Start */
        RawModel model = OBJLoader.loadObjModel("assets/unsafedino", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("assets/unsafedino"));
        texture.setShineDamper(15);
        texture.setReflectivity(1);
        TexturedModel texturedModel = new TexturedModel(model, texture);
        Entity dino = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 18);
        /** Dino End */

        /** Birds Start */
        ArrayList<Entity> birds = new ArrayList<>();

        RawModel birdModel = OBJLoader.loadObjModel("trees/firstbird", loader);
        ModelTexture birdTexture = new ModelTexture(loader.loadTexture("trees/firstbird"));
        for (int i = 0; i < 350; i++) {
            TexturedModel birdTexturedModel = new TexturedModel(birdModel, birdTexture);
            float x = (float) Math.random() * 800;
            float z = (float) Math.random() * 800;
            float y = 65 * (float) Math.random() + 60;
            Entity birdEntity = new Entity(birdTexturedModel,
                    new Vector3f(x, y, z),
                    0, (float) i, 0,
                    (float) Math.random() * 2 + 2);
            birds.add(birdEntity);
        }
        /** Birds End */

        MasterRenderer renderer = new MasterRenderer(loader);

        float rotation = 0;
        float radius = 130;
        float nightFade = 0;
        int ticks = 0;
        int treeColor = 0;

        while (!Display.isCloseRequested()) {
            player.move(terrain);
            camera.update();
            lights.get(0).setPosition(new Vector3f(camera.getPosition().getX(), camera.getPosition().getY() + 7, camera.getPosition().getZ()));


            /** Dino Start */
            float dinoX = (float) Math.sin(rotation) * radius + 400;
            float dinoZ = (float) Math.cos(rotation) * radius + 400;
            float dinoY = terrain.getHeight(dinoX, dinoZ);
            dino.setPosition(new Vector3f(dinoX, dinoY, dinoZ));
            dino.setRotY((float) Math.toDegrees(rotation) + 90);
            rotation += 0.002f;
            lights.get(1).setPosition(new Vector3f(dino.getPosition().getX(), dino.getPosition().getY() + 7, dino.getPosition().getZ()));
            /** Dino End */

            /** Bird Start */
            for (Entity bird : birds) {
                float dx = (float) Math.sin(Math.toRadians(bird.getRotY())) * DisplayManager.getFrameTime() * 0.023f;
                float dz = (float) Math.cos(Math.toRadians(bird.getRotY())) * DisplayManager.getFrameTime() * 0.023f;
                bird.increasePosition(dx, 0, dz);
                Vector3f birdPos = bird.getPosition();
                if (birdPos.getX() > 800) {
                    birdPos.setX(0);
                }
                if (birdPos.getX() < 0) {
                    birdPos.setX(800f);
                }
                if (birdPos.getZ() > 800) {
                    birdPos.setZ(0);
                }
                if (birdPos.getZ() < 0) {
                    birdPos.setZ(800f);
                }
                bird.setPosition(birdPos);
                renderer.processEntity(bird);
            }
            /** Bird End */

            /** Day / Night Start */

            if (nightFade == 1) {
                if (ticks % 100 == 0) {
                    treeColor++;
                }
                lights.get(3).setColor(new Vector3f(
                        treeColor % 3 == 0 ? 1f : 0f,
                        treeColor % 3 == 1 ? 1f : 0f,
                        treeColor % 3 == 2 ? 1f : 0f
                        ));
            } else if (nightFade > 0) {
                nightFade += 0.0003f;
                nightFade = (float) Math.min(nightFade, 1);
                renderer.setNightFade(nightFade);
                lights.get(2).setColor(new Vector3f(1 - nightFade, 1 - nightFade, 1 - nightFade));
            } else if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
                nightFade = 0.0003f;
            }

            /** Day / Night End */


            renderer.processTerrain(terrain);
            renderer.processEntity(dino);
            for (Entity vegetationEntity : vegetation) {
                renderer.processEntity(vegetationEntity);
            }
            renderer.processEntity(player);
            renderer.render(lights, camera);

            if (player.getWalkingSpeed() != 0) {
                if (!stepping) {
                    source2.play(bufferFootsteps);
                }
                stepping = true;
            } else if (source2.isPlaying()) {
                source2.stop();
                stepping = false;
            }

            ticks++;
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
