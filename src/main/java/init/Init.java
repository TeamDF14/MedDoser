package init;
import persistenceSQL.ControlSQL;
import persistenceXML.Persistence;
import persistenceXML.PersistenceXMLObject;
import threads.IngestionCreationTask;
import threads.GPIOThread;
import logging.Logging;
import org.apache.commons.lang3.SystemUtils;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;
import java.util.logging.Level;

import static logging.Logging.logger;


public class Init {

    // Initialize the GPIO instance statically
    public static GPIOThread toggleGPIOStateInstance = new GPIOThread();
    
    // Define the interval (in milliseconds) that forces the led and sound thread to trigger
    public static int reminderInterval = 20000;

    // Define a persistence XML object in order to keep often used data in the RAM
    public static PersistenceXMLObject persistenceXMLObject;

    // Define the name of the log file
    public static final String nameOfLogFile = "MedLog.log";

    // Defines the input files.
    public final static String pathToLinux = "/home/shares/MedDoser/";
    private static String pathToOutputFile = "/";
    private static final String nameOfOutputFile = "FHIR_generated.xml";
    public static File FHIRFile;

    // Persistence
    public static String pathToPersistenceFile = "/";
    private static final String nameOfPersistenceFile ="Persistence_MedDoser.xml";
    public static File persistenceFile;

    // Database
    private static String pathToDatabase = "/";
    private static final String nameOfDatabaseFile = "pzn.db";
    public static File dbFile;

    // Sound
    private static String pathToSound = "/";
    public static final String nameOfSoundFile = "softTone";
    public static File soundFile;
    public static File newSoundFile;

    // UKF
    public static final String encoding = "UTF-8";
    //private static String pathToUkfFile = "/ukf2fhir/";
    //private static final String nameOfUkfFile ="ukfString.txt";
   // public static File newUKFFile;



    /**
     * Changes the paths to the needed files, depending on the resource classpath (IDE or .JAR)
     * Furthermore, the FILE objects are filled.
     */
    public static void adjustPaths() {
        //String pToOutputFile;
        String pToPersistenceFile;
        String pToDBFile;
        String pToSoundFile;
       // String pToUkfStringFile;

        String pToNewSoundFile;
        String pToNewUIFFile;
        String pToNewOutputFile;

        // LINUX: Always the same path on the raspberry
        if (SystemUtils.IS_OS_LINUX) {

            logger.log(Level.FINEST, "The application is running under Linux!");
            //pToOutputFile = pathToLinux + nameOfOutputFile;
            pToNewOutputFile = pathToLinux + nameOfOutputFile;
            pToPersistenceFile = pathToLinux + nameOfPersistenceFile;
            pToDBFile = pathToLinux + nameOfDatabaseFile;
            pToSoundFile = pathToLinux + nameOfSoundFile;
            pToNewSoundFile = pathToLinux + "/" + nameOfSoundFile;
            //pToNewUIFFile = pathToLinux + nameOfUkfFile;
        }

        // WINDOWS: Different path, depending on the location of the project
        else {

             logger.log(Level.FINEST, "The application is running under Windows!");

             final String pathToWindows = Init.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
             //pToOutputFile = pathToWindows + pathToOutputFile + nameOfOutputFile;
             pToNewOutputFile = pathToWindows + nameOfOutputFile;
             pToPersistenceFile = pathToWindows + pathToPersistenceFile + nameOfPersistenceFile;
             pToDBFile =  pathToWindows + pathToDatabase + nameOfDatabaseFile;
             pToSoundFile =  pathToWindows + pathToSound + nameOfSoundFile;

             //pToNewUIFFile = pathToWindows + nameOfUkfFile;
             pToNewSoundFile = pathToWindows + nameOfSoundFile;
        }

        // JAR: Running from a file.
        if ((pathToOutputFile == null) || new File(Init.class.getProtectionDomain().getCodeSource().getLocation().getPath()).isFile()) {

            logger.log(Level.FINEST, "The application is running from a .jar file!");

            final File jar = new File(Init.class.getProtectionDomain().getCodeSource().getLocation().getFile());
            //pToOutputFile = jar.getParent() + File.separator + pathToOutputFile + File.separator + nameOfOutputFile;
            pToNewOutputFile = jar.getParent() + File.separator + nameOfOutputFile;
            pToPersistenceFile = jar.getParent() + File.separator + nameOfPersistenceFile;
            pToDBFile = jar.getParent() + File.separator + nameOfDatabaseFile;
            pToSoundFile = jar.getParent() + File.separator + nameOfSoundFile;
            //pToNewUIFFile = jar.getParent() + File.separator + nameOfUkfFile;
            pToNewSoundFile = jar.getParent() + File.separator + soundFile;

            // Set this special path - important!
            //pathToUkfFile = "/ukf2fhir/";
        }


        // Decode path to URL by using UTF-8
        try {
            //pToOutputFile = URLDecoder.decode(pToOutputFile, encoding);
            pToNewOutputFile = URLDecoder.decode(pToNewOutputFile, encoding);
            pToPersistenceFile = URLDecoder.decode(pToPersistenceFile, encoding);
            pToDBFile = URLDecoder.decode(pToDBFile, encoding);
            pToSoundFile = URLDecoder.decode(pToSoundFile, encoding);
            //pToNewUIFFile = URLDecoder.decode(pToNewUIFFile, encoding);
            pToNewSoundFile = URLDecoder.decode(pToNewSoundFile, encoding);

        } catch (UnsupportedEncodingException e) {
            logger.log(Level.FINEST, "Cannot convert the url(path) to " + encoding + ": " + e);
        }

        // Set the file variables
        //inputFile = new File(pToOutputFile);
        FHIRFile = new File(pToNewOutputFile);

        persistenceFile = new File(pToPersistenceFile);
        dbFile = new File(pToDBFile);
        soundFile = new File(pToSoundFile);

        //newUKFFile = new File(pToNewUIFFile);

        newSoundFile = new File(pToNewSoundFile);
    }

    /**
     * Starts the logger and starts threads.
     */
    public static void initialize() {
        // Start the logger. Within the class that starts the logger, no logging is possible
        Logging logger = new Logging();
        logger.start(nameOfLogFile);

        // Adjust the correct paths, depending on the operating system etc.
        adjustPaths();

        // Create database instance
        new ControlSQL(dbFile);

        // Create a new persistence object (and file) and insert ingestions into the persistence file, if the FHIR file already exists.
        Persistence p = new Persistence();
        p.writeLastOpened(new Date());
        new IngestionCreator(p);

        if (util.FileSystem.bCheckFileExists(Init.persistenceFile)) {

            // Thread: Checks every minute if a new day has started and inserts new entries into the persistence file
            IngestionCreationTask icTask = new IngestionCreationTask(p, 60000);
            Thread threadICTask = new Thread(icTask.getIngestionCreatorTask());
            threadICTask.setDaemon(true);
            threadICTask.start();
        }

        // Stuff that is executed when the program is quit
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {

                // Unprovision the GPIO pins
                Init.toggleGPIOStateInstance.toggleGPIOState(false);

                // Stop the logger and save the log file
                logger.stop();

            }
        });
    }

}
