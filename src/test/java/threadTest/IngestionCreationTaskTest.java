package threadTest;

import init.Init;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import persistenceXML.Persistence;
import threads.IngestionCreationTask;

import static org.junit.Assert.*;

public class IngestionCreationTaskTest {

    @BeforeClass
    public static void setUp() {
        Init.initialize();
    }

    @Test
    public void getIngestionCreatorTask() {

        Persistence p = new Persistence();
        int period = 5;

        IngestionCreationTask iTask = new IngestionCreationTask(p, period);

        Task t = iTask.getIngestionCreatorTask();
        assertTrue("Task is null, but should be initialized", t != null);
        assertTrue("The worker state of the task should be READY", t.getState() == Worker.State.READY);
    }
}