package renderEngine;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import textures.TextureData;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse lädt die 3D Objekt-Modelle und Texturen in die Vertex Array Buffers (VAO)
 * bzw. Vertex Buffer Object (VBO), damit diese von der GPU verwendet werden können.
 */
public class Loader {

    /**
     * Pfad zum Ressourcen-Verzeichnis
     */
    private static final String RESOURCES = "res/";

    /**
     * Vertex Array Buffers
     */
    private final List<Integer> vaos = new ArrayList();
    /**
     * Vertex Buffer Objects
     */
    private final List<Integer> vbos = new ArrayList();
    /**
     * Texturen
     */
    private final List<Integer> textures = new ArrayList();


    /**
     * @param positions         Vertex Positionen des 3D Modells
     * @param textureCoordinate Textur Koordinaten des 3D Modells
     * @param normals           Normalenvektoren des 3D Modells
     * @param indices           Indizes des
     * @return
     */
    public RawModel loadToVAO(float[] positions, float[] textureCoordinate, float[] normals, int[] indices) {
        int vaoId = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoordinate);
        storeDataInAttributeList(2, 3, normals);
        unbindVAO();
        return new RawModel(vaoId, indices.length);
    }

    public RawModel loadToVAO(float[] positions, int dimensions) {
        int vaoId = createVAO();
        this.storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();
        return new RawModel(vaoId, positions.length / dimensions);
    }

    public RawModel loadToVAO(float[] positions) {
        int vaoId = createVAO();
        this.storeDataInAttributeList(0, 2, positions);
        unbindVAO();
        return new RawModel(vaoId, positions.length / 2);
    }

    public int loadTexture(String fileName) {
        Texture texture = null;

        String name = fileName.endsWith(".png") ? fileName : fileName + ".png";
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream(RESOURCES + name));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int textureId = texture.getTextureID();
        textures.add(textureId);
        return textureId;
    }

    /**
     * Nachdem die Anwendung beendet wurde, sollten die gebundenen Ressourcen wieder freigegeben werden.
     */
    public void cleanUp() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }

    /**
     * Diese Funktion läd eine CubeMap Textur, bestehend aus mehreren Texturdateien in den Texturspeicher.
     * CubeMaps werden für die Quadratische Skybox benötigt.
     *
     * @param textureFiles Name der Texturen
     * @return Id zur Referenz auf die geladenen Texturen.
     */
    public int loadCubeMap(String[] textureFiles) {
        int texId = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texId);

        for (int i = 0; i < textureFiles.length; i++) {
            String textureName = textureFiles[i].endsWith(".png") ? textureFiles[i] : textureFiles[i] + ".png";
            TextureData data = decodeTextureFile(RESOURCES + textureName);
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i,
                    0,
                    GL11.GL_RGBA,
                    data.getWidth(),
                    data.getHeight(),
                    0,
                    GL11.GL_RGBA,
                    GL11.GL_UNSIGNED_BYTE,
                    data.getBuffer()
            );
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        textures.add(texId);
        return texId;
    }

    /**
     * Diese Funktion läd mithilfe einer PNG-Decoder-Library PNG Daten in ein Buffer Objekt.
     *
     * @param fileName Dateiname der Textur, die dekodiert werden soll.
     * @return Texturdaten
     */
    private TextureData decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try (FileInputStream in = new FileInputStream(fileName);) {
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = BufferUtils.createByteBuffer(4 * width * height);
            decoder.decode(buffer, width * 4, Format.RGBA);
            buffer.flip();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not load texture " + fileName);
            System.exit(-1);
        }
        return new TextureData(width, height, buffer);
    }

    /**
     * Erstellt einen neuen Vertex Array Buffer und gibt dessen Id zurück.
     *
     * @return VAO Id
     */
    private int createVAO() {
        int vaoId = GL30.glGenVertexArrays();
        vaos.add(vaoId);
        GL30.glBindVertexArray(vaoId);
        return vaoId;
    }

    /**
     * Speichert ein Float Array, welches Vektoren variabler Größe repräsentiert in einem Vertex Buffer Object (VBO) und legt diese unter eine Attribut-Nummer ab.
     * Die AttributNummer repräsentiert hierbei um welche Daten es sich handelt. Z.B.: Vertex Positionen, Texturkoordinaten, o.Ä.
     *
     * @param attributeNumber Unter welchem Index im Vertex Array Object (VAO), das Vertex Buffer Object (VBO) gespeichert werden soll.
     * @param coordinateSize  Dimensionen der Vektoren im Float Array.
     * @param data            Daten, z.B. Vertex Positionen, Texturkoordinaten, o.Ä.
     */
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboId = GL15.glGenBuffers();
        vbos.add(vboId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboId = GL15.glGenBuffers();
        vbos.add(vboId);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    /**
     * Erstellt aus einem Integer Array ein Buffer mit den Daten aus dem Array.
     *
     * @param data
     * @return Buffer Objekt
     */
    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    /**
     * Erstellt aus einem Float Array ein Buffer mit den Daten aus dem Array.
     *
     * @param data
     * @return Buffer Objekt
     */
    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
