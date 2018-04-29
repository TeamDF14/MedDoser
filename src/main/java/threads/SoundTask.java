package threads;

import init.Init;
import org.apache.commons.lang3.SystemUtils;
import java.io.IOException;

public class SoundTask implements Runnable {

    public SoundTask() {
        start();
        run();
    }

    private void start() {
        new Thread(this);
    }

    public void run() {

            // Play the sound track
            if (SystemUtils.IS_OS_LINUX){
                String command= Init.pathToLinux + Init.nameOfSoundFile;
                try {
                    Runtime.getRuntime().exec(command);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("# Sound task: No sound will be played, MedDoser is running under WINDOWS..");
            }

    }
}