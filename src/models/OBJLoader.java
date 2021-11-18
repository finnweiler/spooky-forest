package models;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse kann Daten aus dem OBJ Format in die Model-Klassen ({@link RawModel}) übersetzen.
 */
public class OBJLoader {

    private static final String RESOURCES = "res/";


    /**
     * Übersetzen einer Zeile der Form "vt {float} {float}" in ein {@link Vector2f}.
     *
     * @param line Zeile zum Übersetzen
     * @return {@link Vector2f}
     */
    private static Vector2f convertToVector2f(String line) {
        String[] lineArray = line.split(" ");
        return new Vector2f(
                Float.parseFloat(lineArray[1]),
                Float.parseFloat(lineArray[2]));
    }

    /**
     * Übersetzen einer Zeile der Form "v_ {float} {float} {float}" in ein {@link Vector3f}.
     *
     * @param line Zeile zum Übersetzen
     * @return {@link Vector3f}
     */
    private static Vector3f convertToVector3f(String line) {
        String[] lineArray = line.split(" ");
        return new Vector3f(
                Float.parseFloat(lineArray[1]),
                Float.parseFloat(lineArray[2]),
                Float.parseFloat(lineArray[3]));
    }


    /**
     * Diese Funktion lädt ein OBJ Objekt aus einer Datei und gibt ein RawModel Objekt zurück,
     * das die in OpenGL geladenen Daten repräsentiert.
     *
     * @param fileName Name der zuladenden Datei
     * @param loader   Loader zum Konvertieren der Daten in ein {@link RawModel}
     * @return {@link RawModel} des Objektes
     */
    public static RawModel loadObjModel(String fileName, Loader loader) {
        List<Vector3f> vertices = new ArrayList();
        List<Vector2f> textures = new ArrayList();
        List<Vector3f> normals = new ArrayList();
        List<Integer> indices = new ArrayList();

        float[] normalsArray = null;
        float[] textureArray = null;

        String name = fileName.endsWith(".obj") ? fileName : fileName + ".obj";

        // Auslesen der Datei aus dem Speicher
        try (BufferedReader reader = new BufferedReader(new FileReader(RESOURCES + name))) {
            String line;
            vertexLoop: while (true) {
                line = reader.readLine();
                // Liest alle Vertex-, Texturkoordinaten und Normalenvektoren aus.
                switch (line.substring(0, 2)) {
                    case "v ":
                        vertices.add(convertToVector3f(line));
                        break;
                    case "vt":
                        textures.add(convertToVector2f(line));
                        break;
                    case "vn":
                        normals.add(convertToVector3f(line));
                        break;
                    case "f ": // begin of faces
                        textureArray = new float[vertices.size() * 2];
                        normalsArray = new float[vertices.size() * 3];
                        break vertexLoop;
                }
            }

            while (line != null) {
                if (line.startsWith("f ")) {
                    convertFace(line, indices, textures, normals, textureArray, normalsArray);
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not load OBJ file!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        float[] verticesArray = new float[vertices.size() * 3];
        int[] indicesArray = new int[indices.size()];

        int j = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[j++] = vertex.x;
            verticesArray[j++] = vertex.y;
            verticesArray[j++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
    }


    /**
     * Diese Funktion bearbeitet die Informationen einer Face-Zeile in der OBJ-Datei.
     *
     * @param face Zeile der OBJ-Datei beginnend mit "f "
     */
    private static void convertFace(String face, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
        String[] vertices = face.split(" "); // Aufteilen in 3 Eckpunkte
        processFaceVertex(vertices[1], indices, textures, normals, textureArray, normalsArray);
        processFaceVertex(vertices[2], indices, textures, normals, textureArray, normalsArray);
        processFaceVertex(vertices[3], indices, textures, normals, textureArray, normalsArray);
    }


    /**
     * Diese Funktion teilt einen Eckpunkt einer Fläche in den Vertex-Index und die Texture- und Normalen-Indizes auf.
     */
    private static void processFaceVertex(String vertex, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
        String[] vertexData = vertex.split("/");

        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);

        Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertexPointer * 2] = currentTexture.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y; // anderes Koordinatensystem von Blender => 1-y

        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNorm.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
    }
}
