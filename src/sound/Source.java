package sound;

import org.lwjgl.openal.AL10;

public class Source {

    private final int sourceId;

    public Source() {
        sourceId = AL10.alGenSources();
        AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 2);
        AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 6);
        AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 15);
    }

    public void play(int buffer) {
        stop();
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
        continuePlaying();
    }

    public void setVolume(float volume) {
        AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
    }

    public void setLooping(boolean loop) {
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    public boolean isPlaying() {
        return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    public void continuePlaying() {
        AL10.alSourcePlay(sourceId);
    }

    public void stop() {
        AL10.alSourceStop(sourceId);
    }
}
