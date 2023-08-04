package de.jkarthaus;

import de.jkarthaus.exceptions.ParameterNotPlausibleException;
import de.jkarthaus.model.SortResult;
import de.jkarthaus.service.MediaSortService;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.runtime.Micronaut;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.nio.file.Path;


@CommandLine.Command(
        name = "NAS Image Sorter",
        description = "Sort a bunch of Media Files (Image/Video) into NAS Folder Structure",
        mixinStandardHelpOptions = true)
@Slf4j
public class NasImageSorterApplication implements Runnable {

    @Inject
    MediaSortService mediaSortService;

    @CommandLine.Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    @CommandLine.Option(names = {"-i", "--input"}, description = "input folder", required = true)
    Path inputFolder;

    @CommandLine.Option(names = {"-o", "--output"}, description = "output folder", required = true)
    Path outputFolder;

    @CommandLine.Option(names = {"-p", "--preText"}, description = "Prefix of Filenames", required = true)
    String filenamePre;

    public static void main(String[] args) throws Exception {
        Micronaut.build(args)
                .banner(true)
                .start();
        PicocliRunner.run(NasImageSorterApplication.class, args);
    }

    public void run() {
        log.info("Sorting Media Files from:{} to:{}", inputFolder, outputFolder);
        SortResult sortResult = null;
        try {
            sortResult = mediaSortService.sortImages(
                    inputFolder,
                    outputFolder,
                    filenamePre,
                    verbose
            );
        } catch (ParameterNotPlausibleException e) {
            System.exit(1);
            //throw new RuntimeException(e);
        }
        log.info("-----------------------------------------------------------");
        log.info("Processed Files:{}", sortResult.getCountImages());
        log.info("{} duplications detected", sortResult.getDuplications());
        log.info("{} error occured", sortResult.getErrors());
    }
}


