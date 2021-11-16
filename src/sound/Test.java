package sound;

/*
import java.io.IOException;

public class Test {

    // Position setzen geht
    // Updaten geht nicht

    public static void main(String[] args) throws IOException, InterruptedException {

        AudioMaster.init();
        AudioMaster.setListenerData(0,0,0);


        int buffer = AudioMaster.loadSound("sound/bounce.wav");
        Source source = new Source();
        source.setLooping(true);
        source.play(buffer);

        float xPos = 100;
        source.setPosition(xPos, 0,0);

        char c = ' ';
        while (c != 'q') {
            c = (char)System.in.read();

            xPos -= 0.03f;
            source.setPosition(xPos, 0, 0);
            Thread.sleep(10);

        }


        source.delete();
        AudioMaster.cleanUp();

    }


}*/
