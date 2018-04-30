# MedDoser

![alt text](https://raw.githubusercontent.com/TeamDF14/MedDoser/master/src/main/resources/images/logo_meddoser.png "MedDoser Logo (TeamDF14)")


The MedDoser is a RaspberryPi 3 Model B  equipped with a touchscreen and some other components and **helps patients to remember to take their medications**.

It offers *acoustic* and *visual memory functions* and provides a **history** that gives the opportunity to track the ingestions to the exact day and time. The information of the respective medication is based on the doctor-prescribed medication plan on **sheet of paper**, whose barcode can be scanned. The read data is provided in a **user interface** that is operated exclusively with the *finger*. The user also has the option to manually **adjust reminder times** for each ingestion time of the day.

## Getting Started (Prerequisit)

The prerequisite is that you have the **MedDoser**, consisting of a RaspberryPi 3, a 7 inch touchscreen, a real-time-clock and a speaker, prepared in front of you.

If you want to know how to plug together these components, follow the instructions described on one of the two websites  or in the written elaboration (See below under "Further Information").

You just want to check out how the software works and have no hardware available? No Problem, just run the source code on your *Windows environment*!

Please note that we are using a separate SQLite *dummy database* that stores drug data, because the license of the official drug database is missing at the moment, but may be available in future releases.

## Dependencies

In order for the project to run correctly, additional projects must be added as modules into the main project. Please read the READMEs of the respective repositories in order to know what they do. Anyway, include them into your IDE:

* **JavaLogger** - [Github](https://github.com/StefanKuppelwieser/JavaLogger)
* **JavaUtil** - [Github](https://github.com/TeamDF14/JavaUtil)
* **UKF2FHIRParser** - [Github](https://github.com/TeamDF14/UKF2FHIRParser)

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [IntelliJ](https://www.jetbrains.com/idea) - Development environment
* [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Java SE Development Kit 8

## Authors

* **Sebastian Büchler** - [Github](https://github.com/sebikolon) - [Website](https://wwww.sbuechler.de)
* **Stefan Kuppelwieser** - [Github](https://github.com/StefanKuppelwieser) - [Website](https://wwww.kuppelwieser.net)

See also the list of [contributors](https://github.com/TeamDF14/MedDoser/graphs/contributors) who participated in this project.

## Further information

* **eHealth @RCBE Regensburg (OTH Regensburg)** - [Website](ehealth.rcbe.de/2018/04/27/meddoser-medikationsplan-auf-dem-raspberry-pi/)
* **sbuechler.de** - [Website](sbuechler.de/projekte/hardware/87-meddoser)
* **Report** -
[Download (pdf) ](https://sbuechler.de/_cstm/medDoser.pdf).


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
