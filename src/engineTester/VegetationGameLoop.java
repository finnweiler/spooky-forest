package engineTester;

import entities.Entity;
import models.OBJLoader;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrain.Terrain;
import textures.ModelTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VegetationGameLoop {

    private static List<Entity> vegetation;

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

    public static void prepare(Terrain terrain, Loader loader) {
        vegetation = generateVegetation(terrain, loader, 0.017f);
    }

    public static void loop(MasterRenderer renderer) {
        for (Entity vegetationEntity : vegetation) {
            renderer.processEntity(vegetationEntity);
        }
    }
}
