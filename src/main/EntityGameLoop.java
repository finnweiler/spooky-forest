package main;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.OBJLoader;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import sound.AudioMaster;
import sound.Source;
import terrain.Terrain;
import textures.ModelTexture;
import toolbox.MousePicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Der GameLoop zur Generierung und zum Rendern der Entities.
 */
public class EntityGameLoop {

    /**
     * Das Kamera Objekt der Szene.
     */
    private static Camera camera;
    /**
     * Das Spieler Objekt der Szene.
     */
    private static Player player;
    /**
     * Die Liste mit den Lichtern der Szene
     * Bis zu vier Lichter werden aktuell unterstützt, ist aber bei ausreichend Rechenleistung
     * beliebig erweiterbar.
     */
    private static List<Light> lights;

    /**
     * Das Weihnachtsbaum Objekt in der Szene
     */
    private static Entity christmasTree;
    /**
     * Die Dekoration des Weihnachtsbaums Objekt in der Szene
     */
    private static Entity treeDecoration;

    /**
     * Der Höhleneingang der "Cave of Fails"
     */
    private static Entity caveEntry;
    /**
     * Die Höhle "Cave of Fails"
     */
    private static Entity cave;

    /**
     * Der lilafarbende Diamant in der "Cave of Fails"
     */
    private static Entity diamond;
    /**
     * Der Ständer über dem der Diamant schwebt
     */
    private static Entity stand;

    /**
     * Der Dinosaurier, der auf einem Einrad durch den Wald fährt
     */
    private static Entity dino;
    /**
     * Das Haus, in dem der Spieler erscheint
     */
    private static Entity house;

    /**
     * Der überdimensionierte Vogel in der Höhle
     */
    private static Entity colorBird;
    /**
     * Der Schriftzug "Forest of Fails" in der Höhle
     */
    private static Entity forestFailes;
    /**
     * Das neue Playermodel, dass die zum Einsatz kam, weil es zu viel
     * von der Kamerasicht blockiert hat.
     */
    private static Entity newPlayer;
    /**
     * Das alte Playermodel, dass die zum Einsatz kam
     */
    private static Entity oldUser;
    /**
     * Ein Baum mit wurzeln, die man sowieso nicht gesehen hätte und deshalb nicht zum Einsatz kam
     */
    private static Entity runTree;

    /**
     * Der Drache, der nicht zur Szene gehört und über "I" eingeblendet werden kann
     */
    private static Entity dragon;

    /**
     * Der tote Baum mit dem Papagei auf dem Ast
     */
    private static Entity spawnTree;

    /**
     * Die Vögel, die über dem Wald fliegen
     */
    private static List<Entity> birds;

    /**
     * Gibt an, ob der Spieler gerade Stritte macht
     */
    private static boolean stepping = false;
    /**
     * Gibt an, wie weit die Nacht Fortgeschritten ist
     */
    private static float nightFade = 0;

    /**
     * Gibt die Rotation an, die der Fährende Dino schon zurückgelegt hat
     */
    private static float rotation = 0;
    /**
     * Gibt den Radius an, mit dem der Dino um das Zentrum des Waldes fährt
     */
    private static float radius = 130;

    /**
     * Rechnet aus, wo sich ein Strahl aus der Kamera und der Boden schneiden
     */
    private static MousePicker picker;
    /**
     * Die Blume, die der Spieler mit Linksklick platzieren kann
     */
    private static Entity flower;

    /**
     * Der Toneffektbuffer für die Schritte des Spielers
     */
    private static int bufferFootsteps;
    /**
     * Die Quelle auf der die Toneffekte abgespielt werden können.
     */
    private static Source source;

    /**
     * Gibt an, ob der Stanford Drache eingeblendet werden soll.
     * Wird über "I" aktiviert.
     */
    private static boolean dragonVisible = false;

    /**
     * Unter Nutzung des Terrains, eines Loaders und des Renderers
     * werden alle Entities (außerhalb der Vegetation) erstellt.
     *
     * @param terrain  {@link Terrain}
     * @param loader   {@link Loader}
     * @param renderer {@link MasterRenderer}
     */
    public static void prepare(Terrain terrain, Loader loader, MasterRenderer renderer) {
        /** Player Start */
        RawModel playerModel = OBJLoader.loadObjModel("assets/player", loader);
        ModelTexture playerTexture = new ModelTexture(loader.loadTexture("assets/player"));
        TexturedModel texturedPlayerModel = new TexturedModel(playerModel, playerTexture);
        player = new Player(texturedPlayerModel, new Vector3f(400, terrain.getHeight(400, 400), 400), 0, 180, 0, 8);
        camera = new Camera(player);
        /** Player End */

        /** Lights Start */
        lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector3f(1, 0.01f, 0.001f)));
        lights.add(new Light(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector3f(1, 0.01f, 0.001f)));
        lights.add(new Light(new Vector3f(+600, 3000, -500), new Vector3f(1f, 1f, 1f)));
        lights.add(new Light(new Vector3f(430, terrain.getHeight(430, 380) + 10, 380), new Vector3f(0, 0, 0), new Vector3f(1, 0.01f, 0.01f)));
        /** Lights End */

        /** Christmas Tree Start */
        RawModel christmasTreeModel = OBJLoader.loadObjModel("trees/christmastree", loader);
        ModelTexture christmasTreeTexture = new ModelTexture(loader.loadTexture("trees/christmastree"));
        TexturedModel texturedChristmasTreeModel = new TexturedModel(christmasTreeModel, christmasTreeTexture);
        christmasTree = new Entity(texturedChristmasTreeModel, new Vector3f(430, terrain.getHeight(430, 380), 380), 0, 0, 0, 18);

        RawModel treeDecorationModel = OBJLoader.loadObjModel("trees/shinytree", loader);
        ModelTexture treeDecorationTexture = new ModelTexture(loader.loadTexture("trees/shinytree"));
        treeDecorationTexture.setShineDamper(15);
        treeDecorationTexture.setReflectivity(1);
        TexturedModel treeDecorationTexturedModel = new TexturedModel(treeDecorationModel, treeDecorationTexture);
        treeDecoration = new Entity(treeDecorationTexturedModel, new Vector3f(430, terrain.getHeight(430, 380), 380), 0, 0, 0, 18);
        /** Christmas Tree End */

        /** Cave Start */
        RawModel caveEntryModel = OBJLoader.loadObjModel("cavefinal", loader);
        ModelTexture caveEntryTexture = new ModelTexture(loader.loadTexture("cavefinal"));
        TexturedModel caveEntryTexturedModel = new TexturedModel(caveEntryModel, caveEntryTexture);
        caveEntry = new Entity(caveEntryTexturedModel, new Vector3f(185, terrain.getHeight(185, 216) + 10, 216), 0, -30, 0, 60);

        RawModel caveModel = OBJLoader.loadObjModel("dickemap", loader);
        ModelTexture caveTexture = new ModelTexture(loader.loadTexture("dickemap"));
        TexturedModel caveTexturedModel = new TexturedModel(caveModel, caveTexture);
        cave = new Entity(caveTexturedModel, new Vector3f(167, terrain.getHeight(185, 216) + 5, 210), 0, -30, 0, 1.7f);
        /** Cave End */

        /** Start Diamond */
        RawModel diamondModel = OBJLoader.loadObjModel("assets/diamond", loader);
        ModelTexture diamondTexture = new ModelTexture(loader.loadTexture("assets/diamond"));
        diamondTexture.setShineDamper(15);
        diamondTexture.setReflectivity(1);
        TexturedModel diamondTexturedModel = new TexturedModel(diamondModel, diamondTexture);
        diamond = new Entity(diamondTexturedModel, new Vector3f(200, terrain.getHeight(200, 120) + 9, 120), 0, -30, 0, 20);

        RawModel standModel = OBJLoader.loadObjModel("assets/stativ", loader);
        ModelTexture standTexture = new ModelTexture(loader.loadTexture("assets/stativ"));
        TexturedModel standTexturedModel = new TexturedModel(standModel, standTexture);
        stand = new Entity(standTexturedModel, new Vector3f(200, terrain.getHeight(200, 120) - 1, 120), 0, -30, 0, 15);
        /** End Diamond */

        /** Dino Start */
        RawModel model = OBJLoader.loadObjModel("assets/unsafedino", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("assets/unsafedino"));
        texture.setShineDamper(15);
        texture.setReflectivity(1);
        TexturedModel texturedModel = new TexturedModel(model, texture);
        dino = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 18);
        /** Dino End */

        /** House Start */
        RawModel houseModel = OBJLoader.loadObjModel("assets/house", loader);
        ModelTexture houseTexture = new ModelTexture(loader.loadTexture("assets/house"));
        TexturedModel texturedHouseModel = new TexturedModel(houseModel, houseTexture);
        house = new Entity(texturedHouseModel, new Vector3f(400, terrain.getHeight(400, 400), 400), 0, 180, 0, 18);
        /** House End */

        /** FAILES Start */
        // 200x x 120z
        RawModel colorBirdModel = OBJLoader.loadObjModel("failes/birdcolor", loader);
        ModelTexture colorBirdTexture = new ModelTexture(loader.loadTexture("failes/birdcolor"));
        TexturedModel texturedcolorBirdModel = new TexturedModel(colorBirdModel, colorBirdTexture);
        colorBird = new Entity(texturedcolorBirdModel, new Vector3f(190, terrain.getHeight(190, 130) + 20, 130), 0, 0, 0, 18);

        RawModel forestFailesModel = OBJLoader.loadObjModel("failes/forestfailes", loader);
        ModelTexture forestFailesTexture = new ModelTexture(loader.loadTexture("failes/forestfailes"));
        TexturedModel texturedforestFailesModel = new TexturedModel(forestFailesModel, forestFailesTexture);
        forestFailes = new Entity(texturedforestFailesModel, new Vector3f(210, terrain.getHeight(210, 110) + 20, 90), 0, 0, 0, 18);

        RawModel newPlayerModel = OBJLoader.loadObjModel("failes/newplayer", loader);
        ModelTexture newPlayerTexture = new ModelTexture(loader.loadTexture("failes/newplayer"));
        TexturedModel texturednewPlayerModel = new TexturedModel(newPlayerModel, newPlayerTexture);
        newPlayer = new Entity(texturednewPlayerModel, new Vector3f(210, terrain.getHeight(210, 130) + 10, 130), 0, 0, 0, 18);

        RawModel oldUserModel = OBJLoader.loadObjModel("failes/olduser", loader);
        ModelTexture oldUserTexture = new ModelTexture(loader.loadTexture("failes/olduser"));
        TexturedModel texturedoldUserModel = new TexturedModel(oldUserModel, oldUserTexture);
        oldUser = new Entity(texturedoldUserModel, new Vector3f(210, terrain.getHeight(210, 110) + 15, 110), 0, 0, 0, 18);

        RawModel runTreeModel = OBJLoader.loadObjModel("failes/runtree", loader);
        ModelTexture runTreeTexture = new ModelTexture(loader.loadTexture("failes/runtree"));
        TexturedModel texturedrunTreeModel = new TexturedModel(runTreeModel, runTreeTexture);
        runTree = new Entity(texturedrunTreeModel, new Vector3f(220, terrain.getHeight(220, 110) + 12.5f, 110), 0, 0, 0, 18);

        /** FAILES End */

        /** Add Benchmark Dragon */
        RawModel dragonModel = OBJLoader.loadObjModel("dragon", loader);
        ModelTexture dragonTexture = new ModelTexture(loader.loadTexture("blue"));
        TexturedModel texturedrdragonModel = new TexturedModel(dragonModel, dragonTexture);
        dragonTexture.setShineDamper(15);
        dragonTexture.setReflectivity(1);
        dragon = new Entity(texturedrdragonModel, new Vector3f(410, terrain.getHeight(410, 375), 375), 0, 0, 0, 1);

        /** End Benchmark Dragon /

        /** Spawn Tree Start */

        RawModel spawnTreeModel = OBJLoader.loadObjModel("trees/parrottree", loader);
        ModelTexture spawnTreeTexture = new ModelTexture(loader.loadTexture("trees/parrottree"));
        TexturedModel texturedspawnTreeModel = new TexturedModel(spawnTreeModel, spawnTreeTexture);
        spawnTree = new Entity(texturedspawnTreeModel, new Vector3f(390, terrain.getHeight(390, 370), 370), 0, 0, 0, 18);

        /** Spawn Tree End */


        /** Birds Start */
        birds = new ArrayList<>();

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

        /** Pick Objects */
        picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
        RawModel flowerModel = OBJLoader.loadObjModel("trees/flower", loader);
        ModelTexture flowerTexture = new ModelTexture(loader.loadTexture("trees/flower"));
        TexturedModel flowerDecorationTexturedModel = new TexturedModel(flowerModel, flowerTexture);
        flower = new Entity(flowerDecorationTexturedModel, new Vector3f(430, terrain.getHeight(430, 380), 380), 0, 0, 0, 18);
        /** End Pick Objects */

        bufferFootsteps = AudioMaster.loadSound("sound/footsteps_short.wav");
        source = new Source();
        source.setLooping(true);

        player.setPosition(new Vector3f(389, 124, 542));
        camera.update();
    }

    /**
     * Die Player-Figur wird auf die Ausgangsposition (x: 400, z: 400) zurückgesetzt.
     *
     * @param terrain {@link Terrain} der genutzten Map zur Bestimmung der y-Position
     */
    public static void resetPlayer(Terrain terrain) {
        player.setPosition(new Vector3f(400, terrain.getHeight(400, 400), 400));
    }


    /**
     * Unter Nutzung des Renderers werden alle Entities sowie Spielerbewegungen verarbeitet.
     *
     * @param renderer   {@link MasterRenderer}
     * @param terrain    {@link Terrain}
     * @param movePlayer Information, ob eine Bewegung des Spielers verarbeitet werden muss
     */
    public static void loop(MasterRenderer renderer, Terrain terrain, boolean movePlayer) {
        if (movePlayer) {
            player.move(terrain);
            camera.update();
        }

        renderer.processEntity(christmasTree);
        renderer.processEntity(treeDecoration);
        renderer.processEntity(cave);
        renderer.processEntity(caveEntry);
        renderer.processEntity(stand);
        renderer.processEntity(house);
        renderer.processEntity(spawnTree);
        renderer.processEntity(colorBird);
        renderer.processEntity(forestFailes);
        renderer.processEntity(newPlayer);
        renderer.processEntity(oldUser);
        renderer.processEntity(runTree);
        renderer.processEntity(dino);
        renderer.processEntity(player);
        renderer.processEntity(diamond);
        renderer.render(lights, camera);

        if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
            dragonVisible = true;
        }

        if (dragonVisible) {
            renderer.processEntity(dragon);
            dragon.rotate(0, DisplayManager.getFrameTime() * 0.02f, 0);
        }


        diamond.rotate(0, DisplayManager.getFrameTime() * 0.01f, 0);
        lights.get(0).setPosition(new Vector3f(camera.getPosition().getX(), camera.getPosition().getY(), camera.getPosition().getZ()));

        if (player.getWalkingSpeed() != 0) {
            if (!stepping) {
                source.play(bufferFootsteps);
            }
            stepping = true;
        } else if (source.isPlaying()) {
            source.stop();
            stepping = false;
        }

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
            nightFade += 0.00002f * DisplayManager.getFrameTime();
            nightFade = Math.min(nightFade, 1);
            renderer.setNightFade(nightFade);
            lights.get(2).setColor(new Vector3f(1 - nightFade, 1 - nightFade, 1 - nightFade));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_N) || player.getPosition().getX() > 600) {
            // beginne die Nacht
            nightFade = 0.0003f;
        }

        /** Day / Night End */

        /** Move one flower */
        picker.update();
        Vector3f terrainPoint = picker.getCurrentTerrainPoint();
        if (terrainPoint != null && Mouse.isButtonDown(0) && movePlayer) {
            flower.setPosition(terrainPoint);
        }
        renderer.processEntity(flower);
    }
}
