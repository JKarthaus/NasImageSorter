package de.jkarthaus;

import io.micronaut.configuration.picocli.PicocliRunner;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.nio.file.Path;


@CommandLine.Command(
        name = "NAS Image Sorter",
        description = "Sort a bunch of Media Files (Image/Video) into NAS Folder Structure",
        mixinStandardHelpOptions = true)
@Slf4j
public class NasImageSorterApplication implements Runnable {

    @CommandLine.Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    @CommandLine.Option(names = {"-i", "--input"}, description = "input folder", required = true)
    Path inputFolder;

    @CommandLine.Option(names = {"-o", "--output"}, description = "output folder", required = true)
    Path outputFolder;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(NasImageSorterApplication.class, args);
    }

    public void run() {
        log.info("Sorting Media Files from:{} to:{}", inputFolder, outputFolder);
        // business logic here
        if (verbose) {
            System.out.println("Hi!");
        }
    }
}




/*
public class NasImageSorterApplication implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(NasImageSorterApplication.class);

    Date startTime = new Date();

    @Autowired
    ConfigTools configTools;

    @Autowired
    ImageSortWorker imageSortWorker;

    @Autowired
    PushOverWorker pushOverWorker;

    @Override
    public void run(String... args) throws Exception {
        if (args == null || args.length != 1) {
            logger.error("I want a config File at Parameter 1");
            throw new ExitException();
        }
        File configFile = new File(args[0]);
        if (!configFile.exists() || !configFile.canRead()) {
            logger.error("Config File:" + args[0] + " not found or cannot read.");
            throw new ExitException();
        }
        logger.info("--------------------------------------------------------------------------");

        configTools.loadFromFile(configFile);
        ImageSortResult imageSortResult = imageSortWorker.sortImages();
        pushOverWorker.sendToPushOver(imageSortResult);
        Date endTime = new Date();
        long diff = endTime.getTime() - startTime.getTime();
        logger.info("Working Time : " + (diff / 1000 % 60) + " Seconds.");
        logger.info("--------------------------------------------------------------------------");

    }


    public static void main(String[] args) {
        SpringApplication.run(NasImageSorterApplication.class, args);
    }
*/

//}
