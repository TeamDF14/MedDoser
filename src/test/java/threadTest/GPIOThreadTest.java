package threadTest;

import org.junit.Test;
import threads.GPIOThread;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GPIOThreadTest {


    @Test
    public void testGPIO(){

        GPIOThread t = new GPIOThread();
        assertFalse("On Windows, the toggling should not work.", t.toggleGPIOState(true));
        assertFalse("On Windows, the toggling should not work.", t.toggleGPIOState(false));

    }
}