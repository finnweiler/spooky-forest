package sound;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import java.util.ArrayList;
import java.util.List;

public class AudioMaster {

    private static final List<Integer> buffers = new ArrayList();

    public static void init() {
        try {
            AL.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static int loadSound(String file) {
        int buffer = AL10.alGenBuffers();
        buffers.add(buffer);

        WaveData waveFile = WaveData.create(file);
        AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
        waveFile.dispose();
        return buffer;
    }

    public static void setListenerData(float x, float y, float z) {
        AL10.alListener3f(AL10.AL_POSITION, x, y, z);
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
    }

    /**
     * MUSS vor dem Beenden der Applikation ausgeführt werden! Beendet die Audio Sourcen und Buffer.
     */
    public static void cleanUp() {
        AL10.alSourceStop(AL10.alGenSources()); // close source
        for (int buffer : buffers) {
            AL10.alDeleteBuffers(buffer); // free buffers
        }
        AL.destroy();
    }
}
