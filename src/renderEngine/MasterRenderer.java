package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import terrain.TerrainShader;
import skybox.SkyboxRenderer;
import terrain.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Diese Klasse verwaltet den {@link EntityRenderer}, {@link TerrainRenderer} und {@link SkyboxRenderer} um aus diesen
 * Komponenten ein finales Rendering der Szene zu erstellen.
 */
public class MasterRenderer {

    /**
     * Sichtfeld der Kamera
     */
    private static final float FOV = 70;            // Sichtfeld der Kamera
    /**
     * Minimale Darstellungsdistanz der Kamera
     */
    private static final float NEAR_PLANE = 0.1f;   // Minimale Darstellungsdistanz
    /**
     * Maximale Darstellungsdistanz der Kamera
     */
    private static final float FAR_PLANE = 1000f;   // Maximale Darstellungsdistanz

    /**
     * Nebel/Himmelfarbe der Szene bei Nacht: Rot-Kanal
     */
    private static final float SKY_R_N = 0.1f;  // Nebelfrabe bei Nacht (R-Komponente)
    /**
     * Nebel/Himmelfarbe der Szene bei Nacht: Grün-Kanal
     */
    private static final float SKY_G_N = 0.1f;  // Nebelfrabe bei Nacht (G-Komponente)
    /**
     * Nebel/Himmelfarbe der Szene bei Nacht: Blau-Kanal
     */
    private static final float SKY_B_N = 0.15f; // Nebelfrabe bei Nacht (B-Komponente)

    /**
     * Nebel/Himmelfarbe der Szene bei Tag: Rot-Kanal
     */
    private static final float SKY_R = 0.5f;    // Nebelfrabe bei Tag (R-Komponente)
    /**
     * Nebel/Himmelfarbe der Szene bei Tag: Grün-Kanal
     */
    private static final float SKY_G = 0.5f;    // Nebelfrabe bei Tag (G-Komponente)
    /**
     * Nebel/Himmelfarbe der Szene bei Tag: Blau-Kanal
     */
    private static final float SKY_B = 0.6f;    // Nebelfrabe bei Tag (B-Komponente)

    /**
     * Nebeldichte
     */
    private static final float FOG_DENSITY = 0.003f;    // Nebeldichte
    /**
     * Nebelübergang
     */
    private static final float FOG_GRADIENT = 3.5f;     // Nebelübergang

    /**
     * Überblendung zur Nacht
     */
    private float nightFade = 0;    // Tag/Nacht Übergang | 0 = Tag und 1 = Nacht

    /**
     * Projektionsmatirx der perspektivischen Kamera
     */
    private Matrix4f projectionMatrix; // Projektionsmatrix

    /**
     * Shader zum Rendern von Entities
     */
    private final StaticShader shader = new StaticShader();
    /**
     * Renderer zum Rendern von Entities
     */
    private final EntityRenderer renderer;

    /**
     * Shader zum Rendern von Terrains
     */
    private final TerrainShader terrainShader = new TerrainShader();
    /**
     * Renderer zum Rendern von Terrains
     */
    private final TerrainRenderer terrainRenderer;

    /**
     * Map von Listen von Entities, die gerendert werden sollen.
     * Nach TexturedModels indizierte Hashmap von Entities, um Effizienz zu steigern.
     */
    private final Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    /**
     * Liste von Terrains, doe gerendert werden sollen.
     */
    private final List<Terrain> terrains = new ArrayList<>();

    /**
     * Renderer zum Rendern von Skyboxes
     */
    private final SkyboxRenderer skyboxRenderer;

    public MasterRenderer(Loader loader) {
        enableCulling();
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
    }

    /**
     * Überspringe das Rendern der Rückseiten von Polygonen, um Ressourcen zu sparen
     */
    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    /**
     * Aktiviere das Rendern der Rückseiten von Polygonen, um flache Objekte zu rendern
     * z.B. Gras
     */
    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    /**
     * Dies ist die Haupt-Render-Funktion und ruft alle anderen Renderer auf. Sie muss jeden Frame aufgerufen werden.
     * @param lights Alle Lichter in der Szene. Vom Shader werden aktuell bis zu vier Lichter berücksichtigt.
     * @param camera Die Kamera, die die Szene rendern soll.
     */
    public void render(List<Light> lights, Camera camera) {
        prepare();

        // Bestimme die aktuelle Nebenfarbe
        float skyR = SKY_R * (1 - nightFade) + SKY_R_N * nightFade;
        float skyG = SKY_G * (1 - nightFade) + SKY_G_N * nightFade;
        float skyB = SKY_B * (1 - nightFade) + SKY_B_N * nightFade;

        // Render alle Entities
        shader.start();
        shader.loadSkyColor(skyR, skyG, skyB);
        shader.loadFog(FOG_DENSITY, FOG_GRADIENT);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();

        // Render den Boden
        terrainShader.start();
        terrainShader.loadSkyColor(skyR, skyG, skyB);
        terrainShader.loadFog(FOG_DENSITY, FOG_GRADIENT);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        // Render die Skybox
        skyboxRenderer.render(camera, skyR, skyG, skyB);

        // Entferne die gerenderten Objekte,
        // da diese nächsten Frame wieder hinzugefügt werden
        terrains.clear();
        entities.clear();
    }

    /**
     * Diese Funktion übergibt dem {@link TerrainRenderer} alle Terrains, die im nächsten Frame angezeigt werden sollen.
     * @param terrain Terrain
     */
    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    /**
     * Diese Funktion übergibt dem {@link EntityRenderer} alle Entities, die im nächsten Frame angezeigt werden sollen.
     * Diese werden zum optimierten Rendering nach Ihrem {@link TexturedModel} indiziert eingefügt.
     * @param entity
     */
    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    /**
     * Entbindet alle shader Ressourcen, die vor Beenden der Anwendung wieder freigegeben werden müssen.
     */
    public void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
        skyboxRenderer.getShader().cleanUp();
    }

    /**
     * Wird vor jedem Frame aufgerufen um das Bild (Color- und Depthbuffer) zu leeren und den Hintergrund in
     * der Nebelfarbe zu zeichnen.
     */
    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(SKY_R,SKY_G,SKY_B,1);
    }

    /**
     * Erstellt die Projektionsmatrix, um die Szene perspektivisch aus Kamerasicht zu projizieren.
     */
    private void createProjectionMatrix() {
        projectionMatrix = new Matrix4f();
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    /**
     * Übergibt dem {@link SkyboxRenderer} wie weit zwischen Tag- und Nachtszene verblendet werden soll.
     * @param nightFade Fortschritt der Nacht | 0 = Tag, 0.5 = 50% Nacht, 0.0 = 100% Nacht
     */
    public void setNightFade(float nightFade) {
        this.nightFade = nightFade;
        skyboxRenderer.setFade(nightFade);
    }

    /**
     * Diese Funktion übergibt die Projektionsmatrix der perspektivischen Kamera
     *
     * @return Projektionsmatrix
     */
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
