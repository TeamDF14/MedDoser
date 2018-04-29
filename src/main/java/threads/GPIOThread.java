package threads;

import com.pi4j.io.gpio.*;
import org.apache.commons.lang3.SystemUtils;

import java.util.logging.Level;

import static logging.Logging.logger;

public class GPIOThread {

    // Names of pins
    final String sPinLEDName = "BlinkingLED";
    final String sPinSoundName ="PlayingSound";

    // IDs of pins. For reference, look at https://www.elektronik-kompendium.de/sites/raspberry-pi/1907101.htm
    final Pin rpinLED = RaspiPin.GPIO_01;
    final Pin rpinSound = RaspiPin.GPIO_03;

    // Initialize the GpioFactory only if we are under LINUX. On Windows the hardware is missing
    GpioController gpio = SystemUtils.IS_OS_LINUX ? GpioFactory.getInstance(): null;

    // Initialize the pins only if we are under LINUX. On Windows the hardware is missing
    GpioPinDigitalOutput pinLED;
    GpioPinDigitalOutput pinSound;


    /**
     * Toggles the provisioning of the GPIO pins for the LED and the speaker.
     * For reference, see 'http://pi4j.com/pins/model-b-plus.html'
     * @param switchOn True if the GPIO pins should be switched ON, false if they should be switched OFF.
     * @return True if the program is running under LINUX and could connect to the pins and toggle it, false if not.
     */
    public boolean toggleGPIOState(boolean switchOn) {

        if(SystemUtils.IS_OS_LINUX) {
            System.out.println("<--Pi4J--> Pi4J is now running under LINUX.");

            // Create gpio controller instance
            if (gpio == null) {
                try {
                    gpio = GpioFactory.getInstance();
                } catch (Exception e) {
                    logger.log(Level.INFO, "<--Pi4J--> MedDoser wasn't able to receive an pi4j GPIO factory instance: " + e);
                    return false;
                }
            }

            // Provide GPIO pins as output pins and turn them on.
            // Reference: http://pi4j.com/usage.html#Provision_Pins
            if (pinLED == null){
                pinLED = gpio.provisionDigitalOutputPin(rpinLED, sPinLEDName);
                pinLED.setShutdownOptions(true, PinState.LOW);
            }
            if (pinSound == null){
                pinSound = gpio.provisionDigitalOutputPin(rpinSound, sPinSoundName);
                pinSound.setShutdownOptions(true, PinState.LOW);
            }



            // Turn on the pins, if they are not already ON
            if(switchOn) {
                if ((pinLED.getState() != PinState.HIGH)) {
                    pinLED.setState(PinState.HIGH);
                    logger.log(Level.INFO, "<--Pi4J--> GPIO pin '"+ pinLED.getName() + "' was triggered to be ON.");
                }
                if ((pinSound.getState() != PinState.HIGH)) {
                    pinSound.setState(PinState.HIGH);
                    logger.log(Level.INFO, "<--Pi4J--> GPIO pin '" + pinSound.getName() + "' was triggered to be ON.");
                }
                else{
                    System.out.println("# GPIO pin '" + pinSound.getName() + "' was not triggered to be ON because it already is!");
                }
            }
            // Turn off the pins
            else{
                if (pinLED.getState() != PinState.LOW){
                    pinLED.low();
                    logger.log(Level.INFO, "<--Pi4J--> GPIO pin '"+ pinLED.getName() + "' was triggered to be OFF.");
                }
                if (pinSound.getState() != PinState.LOW){
                    pinSound.low();
                    logger.log(Level.INFO, "<--Pi4J--> GPIO pin '" + pinSound.getName() + "' was triggered to be OFF");
                }
            }

            return true;
        }

        //logger.log(Level.INFO, "<--Pi4J--> OS type WINDOWS detected, no GPIO Pin can be triggered because of missing hardware.");
        return false;
    }
}
