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
        TexturedModel tree1TexturedModel = new TexturedModel(tree1Model, tree1Texture);

        RawModel tree2Model = OBJLoader.loadObjModel("trees/oak1", loader);
        ModelTexture tree2Texture = new ModelTexture(loader.loadTexture("trees/oak1"));
        TexturedModel tree2TexturedModel = new TexturedModel(tree2Model, tree2Texture);

        RawModel tree3Model = OBJLoader.loadObjModel("trees/christmastree", loader);
        ModelTexture tree3Texture = new ModelTexture(loader.loadTexture("trees/christmastree"));
        TexturedModel tree3TexturedModel = new TexturedModel(tree3Model, tree3Texture);

        RawModel flowerModel = OBJLoader.loadObjModel("trees/flower", loader);
        ModelTexture flowerTexture = new ModelTexture(loader.loadTexture("trees/flower"));
        TexturedModel flowerTexturedModel = new TexturedModel(flowerModel, flowerTexture);

        RawModel bushModel = OBJLoader.loadObjModel("assets/bush", loader);
        ModelTexture bushTexture = new ModelTexture(loader.loadTexture("assets/bush"));
        TexturedModel bushTexturedModel = new TexturedModel(bushModel, bushTexture);

        RawModel grassModel = OBJLoader.loadObjModel("assets/grass", loader);
        ModelTexture grassTexture = new ModelTexture(loader.loadTexture("assets/grass"));
        TexturedModel grassTexturedModel = new TexturedModel(grassModel, grassTexture);

        for(int i=0;i<squareCount;i++){
            for(int j=0;j<squareCount;j++){
                float percentage = getPercentage(j, i, vegetationMapImage);
                if ((1 - percentage) * density > Math.random()) {

                    TexturedModel texturedModel = null;
                    float scale = 1;
                    float object = (float) Math.random();
                    if (object < 0.1) {
                        texturedModel = tree1TexturedModel;
                        scale = (float) Math.random() * 2 + 2;
                    } else if (object < 0.2) {
                        texturedModel = tree2TexturedModel;
                        scale = (float) Math.random() * 6 + 36;
                    } else if (object < 0.25) {
                        texturedModel = tree3TexturedModel;
                        scale = (float) Math.random() * 6 + 25;
                    } else if (object < 0.35) {
                        texturedModel = flowerTexturedModel;
                        scale = (float) Math.random() * 4 + 15;
                    }   else if (object < 0.4) {
                        texturedModel = bushTexturedModel;
                        scale = (float) Math.random() * 20 + 15;
                    } else {
                        texturedModel = grassTexturedModel;
                        scale = (float) Math.random() * 10 + 8;
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
        List<Entity> vegetation = generateVegetation(terrain, loader, 0.017f);
        /** Vegetation End */

        /** Player Start */
        RawModel playerModel = OBJLoader.loadObjModel("assets/player", loader);
        ModelTexture playerTexture = new ModelTexture(loader.loadTexture("assets/player"));
        TexturedModel texturedPlayerModel = new TexturedModel(playerModel, playerTexture);
        Player player = new Player(texturedPlayerModel, new Vector3f(400, terrain.getHeight(400, 400), 400), 0, 180, 0, 8);
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

        /** Cave Start */
        RawModel caveEntryModel = OBJLoader.loadObjModel("cavefinal", loader);
        ModelTexture caveEntryTexture = new ModelTexture(loader.loadTexture("cavefinal"));
        TexturedModel caveEntryTexturedModel = new TexturedModel(caveEntryModel, caveEntryTexture);
        Entity caveEntry = new Entity(caveEntryTexturedModel, new Vector3f(185, terrain.getHeight(185, 216), 216), 0, -30, 0, 1.8f);
        vegetation.add(caveEntry);

        RawModel caveModel = OBJLoader.loadObjModel("dickemap", loader);
        ModelTexture caveTexture = new ModelTexture(loader.loadTexture("dickemap"));
        TexturedModel caveTexturedModel = new TexturedModel(caveModel, caveTexture);
        Entity cave = new Entity(caveTexturedModel, new Vector3f(170, terrain.getHeight(185, 216) + 10, 210), 0, -30, 0, 1.8f);
        vegetation.add(cave);
        /** Cave End */

        /** Start Diamond */

        RawModel diamondModel = OBJLoader.loadObjModel("assets/diamond", loader);
        ModelTexture diamondTexture = new ModelTexture(loader.loadTexture("assets/diamond"));
        diamondTexture.setShineDamper(15);
        diamondTexture.setReflectivity(1);
        TexturedModel diamondTexturedModel = new TexturedModel(diamondModel, diamondTexture);
        Entity diamond = new Entity(diamondTexturedModel, new Vector3f(400, terrain.getHeight(400, 400) + 10, 400), 0, -30, 0, 20);
        vegetation.add(diamond);

        RawModel stativModel = OBJLoader.loadObjModel("assets/stativ", loader);
        ModelTexture stativTexture = new ModelTexture(loader.loadTexture("assets/stativ"));
        TexturedModel stativTexturedModel = new TexturedModel(stativModel, stativTexture);
        Entity stativ = new Entity(stativTexturedModel, new Vector3f(400, terrain.getHeight(400, 400), 400), 0, -30, 0, 15);
        vegetation.add(stativ);

        /** End Diamond */

        /** Dino Start */
        RawModel model = OBJLoader.loadObjModel("assets/unsafedino", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("assets/unsafedino"));
        texture.setShineDamper(15);
        texture.setReflectivity(1);
        TexturedModel texturedModel = new TexturedModel(model, texture);
        Entity dino = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 18);
        /** Dino End */

        /** House Start */
        RawModel houseModel = OBJLoader.loadObjModel("assets/house", loader);
        ModelTexture houseTexture = new ModelTexture(loader.loadTexture("assets/house"));
        TexturedModel texturedHouseModel = new TexturedModel(houseModel, houseTexture);
        Entity house = new Entity(texturedHouseModel,  new Vector3f(400, terrain.getHeight(400, 400), 400), 0, 180, 0, 18);
        /** House End */

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

        /** Pick Objects */

        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
        RawModel flowerModel = OBJLoader.loadObjModel("trees/flower", loader);
        ModelTexture flowerTexture = new ModelTexture(loader.loadTexture("trees/flower"));
        treeDecorationTexture.setShineDamper(15);
        treeDecorationTexture.setReflectivity(1);
        TexturedModel flowerDecorationTexturedModel = new TexturedModel(flowerModel, flowerTexture);
        Entity flower = new Entity(flowerDecorationTexturedModel, new Vector3f(430, terrain.getHeight(430, 380), 380), 0, 0, 0, 18);
        List<Entity> flowers = new ArrayList<Entity>();
        /** End Pick Objects */

        float rotation = 0;
        float radius = 130;
        float nightFade = 0;
        boolean closeRequested = false;

        player.move(terrain);
        camera.update();

        int diaHeight = 0;
        boolean diaUp = true;

        while (!Display.isCloseRequested() && !closeRequested) {

            if (!escaped) {
                player.move(terrain);
                camera.update();
            }

<<<<<<< Updated upstream
=======
            diamond.rotate(0, DisplayManager.getFrameTime() * 0.01f, 0);
            Vector3f newPos = new Vector3f();
            newPos.x = 400;
            if (diaUp && diaHeight <= 5) {
                diaHeight += 0.5;
                if (diaHeight >= 5) {
                    diaUp = false;
                }
            } else {
                diaHeight -= 0.5;
                if (diaHeight <= 0) {
                    diaUp = true;
                }
            }

            newPos.y = terrain.getHeight(400, 400) + 10 + diaHeight;
            newPos.z = 400;
            diamond.setPosition(newPos);

            // System.out.println(player.getPosition());

>>>>>>> Stashed changes
            lights.get(0).setPosition(new Vector3f(camera.getPosition().getX(), camera.getPosition().getY() + 7, camera.getPosition().getZ()));

            /** Dino Start */
            float dinoX = (float) Math.sin(rotation) * radius + 400;
            float dinoZ = (float) Math.cos(rotation) * radius + 400;
            float dinoY = terrain.getHeight(dinoX, dinoZ);
            dino.setPosition(new Vector3f(dinoX, dinoY, dinoZ));
            dino.setRotY((float) Math.toDegrees(rotation) + 90);
            rotation += 0.0002f * DisplayManager.getFrameTime();
            lights.get(1).setPosition(new Vector3f(dino.getPosition().getX(), dino.getPosition().getY() + 7, dino.getPosition().getZ()));
            /** Dino End */

            /** Bird Start */
            for (Entity bird : birds) {
                float dx = (float) Math.sin(Math.toRadians(bird.getRotY())) * DisplayManager.getFrameTime() * 0.023f;
                float dz = (float) Math.cos(Math.toRadians(bird.getRotY())) * DisplayManager.getFrameTime() * 0.023f;
                bird.translate(dx, 0, dz);
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
                int treeColor = (int) DisplayManager.getCurrentTime() / 1000;
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

            renderer.processEntity(house);
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

            /** Move one flower */
            picker.update();
            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
            if (terrainPoint != null && Mouse.isButtonDown(0) && !escaped) {
                flower.setPosition(terrainPoint);
            }
            renderer.processEntity(flower);


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