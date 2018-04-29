package threads;

import init.IngestionCreator;
import javafx.application.Platform;
import javafx.concurrent.Task;
import persistenceXML.Persistence;
import help.Help;
import java.util.Date;

/**
 * This class contains the logic of a task that creates entries wihtin the persistence file periodically.
 */
public class IngestionCreationTask {

    private int period;
    Persistence p;

    public IngestionCreationTask(Persistence p, int period){
        this.period = period;
        this.p = p;
    }


    /**
     * <p>Creates a new task that calculates the difference in days between the last opened date of th persistence file and today.</p>
     * <p>If the difference is at least 1, new entries are added to the file.</p>
     * @return A new task.
     */
    public Task getIngestionCreatorTask(){
        Task ingCreatorTask = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            if ((util.Date.getDifference(p.readLastOpened(), new Date()).size()) > 0){

                                // Create a new instance
                               new IngestionCreator(p);

                               // Update last opened date
                               p.writeLastOpened(new Date());
                           }
                        }
                    });
                    Thread.sleep(period);
                }
            }
        };
        return ingCreatorTask;
    }


}
