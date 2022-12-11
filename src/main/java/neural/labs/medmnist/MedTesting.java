package neural.labs.medmnist;

import org.encog.Encog;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import neural.util.EncogHelper;

import neural.mnist.IMedLoader.Normal;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Original code by: Ron Coleman (24 Oct 2017)
 * @authors: Michael Volchko and Oliver Wilson
 * @date 30 Nov 2022
 */
public class MedTesting {

    //public final static int NUM_SAMPLES = 10000;

    static double TRAINING_INPUTS[][];
    static double TRAINING_IDEALS[][];

    //Referenced online documentation for Date format
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");

    /**
     * ================= SPECIFY DATASET HERE! =================
     */
    static String DATASET = "pneumonia";

    static void init() {
        MedLoader medloader = new MedLoader();

        if (DATASET == "pneumonia") {
            medloader.load("pneumonia", "test");
        }
        else if (DATASET == "chest") {
            medloader.load("chest", "test");
        }
        else if (DATASET == "breast") {
            medloader.load("breast", "test");
        }
        else {
            System.out.println("Error while loading data.");
        }

        Normal normal = medloader.normalize();

        TRAINING_INPUTS = normal.pixels();

        //System.out.println("This is the size: " + TRAINING_INPUTS.length);

        TRAINING_IDEALS = normal.labels();
    }

    /**
     * The main method.
     * @param args No arguments are used.
     */
    public static void main(final String args[]) {

        init();

        // Create training observations
        BasicMLDataSet testingSet = new BasicMLDataSet(TRAINING_INPUTS, TRAINING_IDEALS);

        BasicNetwork network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File("src/main/java/neural/labs/medmnist/encogmnist-" + DATASET + ".bin"));

        EncogHelper.summarize(network);

        MedExercise exercise = new MedExercise(network, testingSet);

        MedExercise.Report results = exercise.report();
        double successRate = Math.round((((double) results.hit() / results.tried()) * 100.0) * 10.0)/10.0;

        System.out.println("success rate = " + results.hit() + "/" + results.tried() + " (" + successRate+ "%)");
        System.out.println("finished: " +
                dateFormat.format(LocalDateTime.now().atZone(ZoneId.of("America/New_York"))));

        Encog.getInstance().shutdown();

    }
}
