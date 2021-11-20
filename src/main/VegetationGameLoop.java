package main;

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

/**
 * Der GameLoop zur Generierung und zum Rendern der Vegetation.
 */
public class VegetationGameLoop {

    /**
     * Alle Vegetations Entities, die vom {@link renderEngine.EntityRenderer} gerendert werden sollen.
     */
    private static List<Entity> vegetation;

    /**
     * Unter Nutzung des Terrains und eines Loaders wird die Vegetation erstellt.
     *
     * @param terrain           {@link Terrain}
     * @param loader            {@link Loader}
     * @param vegetationDensity Dichte der Vegetation
     */
    private static List<Entity> generateVegetation(Terrain terrain, Loader loader, float vegetationDensity) {
        ArrayList<Entity> vegetation = new ArrayList<>();

        BufferedImage vegetationMapImage = null;
        try {
            vegetationMapImage = ImageIO.read(new File("res/vegetation.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int squareCount = vegetationMapImage.getHeight();
        float scalingFactor = 800 / (float) squareCount;

        TexturedModel tree1TexturedModel = getTexturedModel(loader, "trees/bigtree1");
        TexturedModel tree2TexturedModel = getTexturedModel(loader, "trees/oak1");
        TexturedModel tree3TexturedModel = getTexturedModel(loader, "trees/christmastree");

        TexturedModel flowerTexturedModel = getTexturedModel(loader, "trees/flower");
        TexturedModel bushTexturedModel = getTexturedModel(loader, "assets/bush");
        TexturedModel grassTexturedModel = getTexturedModel(loader, "assets/grass");

        for (int i = 0; i < squareCount; i++) {
            for (int j = 0; j < squareCount; j++) {
                float percentage = getPercentage(j, i, vegetationMapImage);
                if ((1 - percentage) * vegetationDensity > Math.random()) {

                    TexturedModel texturedModel;
                    float scale;
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
                    } else if (object < 0.4) {
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
                    Entity treeEntity =
                            new Entity(texturedModel, new Vector3f(worldX, y, worldZ),
                                    0, (float) Math.random() * 360, 0, scale);
                    vegetation.add(treeEntity);
                }
            }
        }

        return vegetation;
    }

    /**
     * Diese Funktion lädt ein texturiertes Modell.
     * @param loader {@link Loader} zum Laden des Modells
     * @param modelName Name des Modells
     * @return {@link TexturedModel}
     */
    private static TexturedModel getTexturedModel(Loader loader, String modelName) {
        RawModel tree1Model = OBJLoader.loadObjModel(modelName, loader);
        ModelTexture tree1Texture = new ModelTexture(loader.loadTexture(modelName));
        return new TexturedModel(tree1Model, tree1Texture);
    }

    /**
     * Diese Funktion berechnet die Helligkeit eines Punktes in einem Bild.
     *
     * @param x     x-Koordinate
     * @param y     y-Koordinate
     * @param image Bild
     * @return Helligkeit des Punktes
     */
    private static float getPercentage(int x, int y, BufferedImage image) {
        float MAX_PIXEL_COLOR = (float) Math.pow(256, 3);

        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            return 0;
        }
        float percentage = -image.getRGB(x, y);
        percentage /= MAX_PIXEL_COLOR;
        return percentage;
    }

    /**
     * Unter Nutzung des Terrains und eines Loaders wird die Vegetation erstellt.
     * (Nutzt Funktion generateVegetation intern.)
     *
     * @param terrain {@link Terrain}
     * @param loader  {@link Loader}
     */
    public static void prepare(Terrain terrain, Loader loader) {
        vegetation = generateVegetation(terrain, loader, 0.017f);
    }

    /**
     * Unter Nutzung des Renderers werden alle Vegetationseinträge verarbeitet.
     *
     * @param renderer {@link MasterRenderer}
     */
    public static void loop(MasterRenderer renderer) {
        for (Entity vegetationEntity : vegetation) {
            renderer.processEntity(vegetationEntity);
        }
    }
}
