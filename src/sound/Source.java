package sound;

import org.lwjgl.openal.AL10;

public class Source {

    private final int sourceId;

    /**
     * Es wird eine Audio Quelle mit ID generiert und auf Starteinstellungen gesetzt.
     */
    public Source() {
        sourceId = AL10.alGenSources();
        AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 2);
        AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 6);
        AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 15);
    }

    /**
     * Mithilfe der Buffer-ID wird die Audio Quelle gestartet.
     * @param buffer Buffer-ID
     */
    public void play(int buffer) {
        stop();
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
        continuePlaying();
    }

    /**
     * Mithilfe dieser Funktion wird die Lautstärke der Audio Quelle gesetzt.
     *
     * @param volume Lautstärke
     */
    public void setVolume(float volume) {
        AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
    }

    public void setLooping(boolean loop) {
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    /**
     * Diese Funktion übergibt, ob die Audio Quelle aktuell wiedergegeben wird.
     *
     * @return Wiedergabestatus
     */
    public boolean isPlaying() {
        return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    /**
     * Diese Funktion setzt die Wiedergabe der Audio Quelle fort.
     */
    public void continuePlaying() {
        AL10.alSourcePlay(sourceId);
    }

    /**
     * Diese Funktion stoppt die Audio Quelle.
     */
    public void stop() {
        AL10.alSourceStop(sourceId);
    }
}
