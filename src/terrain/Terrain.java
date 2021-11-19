package terrain;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Diese Klasse umfasst die Daten einer Höhenkarte sowie die Funktionen, um diese zu verwenden.
 */
public class Terrain {


    /**
     * Pfad zum Ressourcen-Verzeichnis
     */
    private static final String RESOURCES = "res/";

    /**
     * Größe der Terrain-Karte
     */
    private static final float SIZE = 800;
    /**
     * maximale y-Höhe des Terrains
     */
    private static final float MAX_HEIGHT = 30;
    /**
     * höchste Pixelfarbe des zu ladenden Bildes
     */
    private static final float MAX_PIXEL_COLOR = (float) Math.pow(256, 3);

    /**
     * x-Position im Grid der Terrains
     */
    private final float gridX;
    /**
     * z-Position im Grid der Terrains
     */
    private final float gridZ;
    /**
     * {@link RawModel} des Terrains
     */
    private final RawModel model;
    /**
     * {@link TerrainTexturePack} des Terrains
     */
    private final TerrainTexturePack texturePack;
    /**
     * {@link TerrainTexture} des Terrains
     */
    private final TerrainTexture blendMap;

    /**
     * 2D-Map der Höhe des Terrains
     */
    private float[][] heights;
    /**
     * 2D-Map der Normalen des Terrains
     */
    private Vector3f[][] normals;


    /**
     * Ein Terrain an der Grid-Position (x, z) wird durch Laden der angegebenen Datei erstellt.
     *
     * @param gridX             x-Position im Grid der Terrains
     * @param gridZ             z-Position im Grid der Terrains
     * @param loader            {@link Loader} zum Konvertieren der Daten
     * @param texturePack       Texturen für die {@link Terrain#blendMap}
     * @param blendMap          Karte mit Einteilung der Texturen
     * @param heightMapFilename Name der zuladenden Datei für die Höhenkarte
     */
    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMapFilename) {
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.gridX = gridX * SIZE;
        this.gridZ = gridZ * SIZE;
        this.model = generateTerrain(loader, heightMapFilename);
    }

    /**
     * Diese Funktion übergibt die x-Position im Grid der Terrains.
     *
     * @return x-Position im Grid der Terrains
     */
    public float getX() {
        return gridX;
    }

    /**
     * Diese Funktion übergibt die z-Position im Grid der Terrains.
     *
     * @return z-Position im Grid der Terrains
     */
    public float getZ() {
        return gridZ;
    }

    /**
     * Diese Funktion übergibt das {@link RawModel} des Terrains.
     *
     * @return {@link RawModel} des Terrains
     */
    public RawModel getModel() {
        return model;
    }

    /**
     * Diese Funktion übergibt das {@link TerrainTexturePack} des Terrains.
     *
     * @return {@link TerrainTexturePack} des Terrains
     */
    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    /**
     * Diese Funktion übergibt die {@link TerrainTexture} des Terrains.
     *
     * @return {@link TerrainTexture} des Terrains
     */
    public TerrainTexture getBlendMap() {
        return blendMap;
    }


    /**
     * Diese Funktion generiert ein {@link RawModel} eines Terrains aus einer Datei mit dem angegebenen Namen.
     *
     * @param loader            {@link Loader} zum Konvertieren der Daten in ein {@link RawModel}
     * @param heightMapFilename Name der zuladenden Datei für die Höhenkarte
     * @return {@link RawModel} eines Terrains
     */
    private RawModel generateTerrain(Loader loader, String heightMapFilename) {
        float[] vertices = null;
        float[] normals = null;
        float[] textureCoords = null;
        int[] indices = null;

        String name = heightMapFilename.endsWith(".png") ? heightMapFilename : heightMapFilename + ".png";
        try (FileInputStream fileInputStream = new FileInputStream(RESOURCES + name)) {
            BufferedImage heightMapImage = ImageIO.read(fileInputStream);
            int vertexCount = heightMapImage.getHeight();

            heights = new float[vertexCount][vertexCount];
            this.normals = new Vector3f[vertexCount][vertexCount];

            int count = vertexCount * vertexCount;
            vertices = new float[count * 3];
            normals = new float[count * 3];
            textureCoords = new float[count * 2];
            indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];

            int vertexPointer = 0;
            for (int i = 0; i < vertexCount; i++) {
                for (int j = 0; j < vertexCount; j++) {
                    vertices[vertexPointer * 3] = (float) j / ((float) vertexCount - 1) * SIZE;

                    float height = getHeight(j, i, heightMapImage);
                    heights[j][i] = height;
                    vertices[vertexPointer * 3 + 1] = height;
                    vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexCount - 1) * SIZE;
                    Vector3f normal = calculateNormal(j, i, heightMapImage);

                    this.normals[j][i] = normal;
                    normals[vertexPointer * 3] = normal.x;
                    normals[vertexPointer * 3 + 1] = normal.y;
                    normals[vertexPointer * 3 + 2] = normal.z;

                    textureCoords[vertexPointer * 2] = (float) j / ((float) vertexCount - 1);
                    textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexCount - 1);

                    vertexPointer++;
                }
            }

            int pointer = 0;
            for (int gz = 0; gz < vertexCount - 1; gz++) {
                for (int gx = 0; gx < vertexCount - 1; gx++) {
                    int topLeft = (gz * vertexCount) + gx;
                    int topRight = topLeft + 1;
                    int bottomLeft = ((gz + 1) * vertexCount) + gx;
                    int bottomRight = bottomLeft + 1;

                    indices[pointer++] = topLeft;
                    indices[pointer++] = bottomLeft;
                    indices[pointer++] = topRight;
                    indices[pointer++] = topRight;
                    indices[pointer++] = bottomLeft;
                    indices[pointer++] = bottomRight;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }


    /**
     * Diese Funktion berechnet den Normalenvektor in einem Punkt anhand der Höhenkarte.
     *
     * @param x         x-Koordinate
     * @param y         y-Koordinate
     * @param heightmap Höhenkarte
     * @return Normalenvektor im Punkt
     */
    private Vector3f calculateNormal(int x, int y, BufferedImage heightmap) {
        float heightL = getHeight(x - 1, y, heightmap);
        float heightR = getHeight(x + 1, y, heightmap);
        float heightD = getHeight(x, y - 1, heightmap);
        float heightU = getHeight(x, y + 1, heightmap);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }

    /**
     * Diese Funktion berechnet die Höhe eines Punktes anhand eines Bilds.
     *
     * @param x     x-Koordinate
     * @param y     y-Koordinate
     * @param image Bild
     * @return Höhe des Punktes
     */
    private float getHeight(int x, int y, BufferedImage image) {
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            return 0;
        }
        float height = -image.getRGB(x, y);
        height /= MAX_PIXEL_COLOR;
        height *= MAX_HEIGHT;
        return height;
    }


    /**
     * Mithilfe der x- und z-Koordinate der Welt übergibt diese Funktion die Höhe an dieser Stelle.
     *
     * @param worldX x-Koordinate der Welt
     * @param worldZ z-Koordinate der Welt
     * @return Höhe (y)
     */
    public float getHeight(float worldX, float worldZ) {
        float terrainX = worldX - this.gridX;
        float terrainZ = worldZ - this.gridZ;

        float gridSquareSize = SIZE / ((float) heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
            return 0;
        }

        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float finalHeight;
        if (xCoord <= (1 - zCoord)) {
            finalHeight = Maths.baryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                    heights[gridX + 1][gridZ], 0), new Vector3f(0,
                    heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            finalHeight = Maths.baryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                    heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                    heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }

        return finalHeight;
    }
}


