package neural.labs.labs07_10;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.BasicTraining;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

import neural.util.EncogHelper;

import neural.labs.lab03_06.Mop;
import neural.matrix.IMop;
import neural.mnist.IMLoader.Normal;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static neural.util.EncogHelper.*;

/**
 * Original code by: Ron Coleman (24 Oct 2017)
 * @authors: Michael Volchko and Oliver Wilson
 * @date 31 Oct 2022
 */
public class VolchkoWilsonzMTesting {

    //public final static int NUM_SAMPLES = 10000;
    static String DIR = "src/main/java/neural/labs/labs07_10";

    static double TRAINING_INPUTS[][];
    static double TRAINING_IDEALS[][];

    //Referenced online documentation for Date format
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");

    static void init() {
        MLoader mloader = new MLoader();

        mloader.load("testing");
        Normal normal = mloader.normalize();

        TRAINING_INPUTS = normal.pixels();
            assert(TRAINING_INPUTS[0].length == (28*28));

        //System.out.println("This is the size: " + TRAINING_INPUTS.length);

        TRAINING_IDEALS = normal.labels();
            assert(TRAINING_IDEALS[0].length == (10-1));
    }

    /**
     * The main method.
     * @param args No arguments are used.
     */
    public static void main(final String args[]) {

        init();

        // Create training observations
        BasicMLDataSet testingSet = new BasicMLDataSet(TRAINING_INPUTS, TRAINING_IDEALS);

        BasicNetwork network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File("src/main/java/neural/labs/labs07_10/encogmnist-2750-616-82_10.bin"));

        EncogHelper.summarize(network);

        MExercise exercise = new MExercise(network, testingSet);

        MExercise.Report results = exercise.report();
        double successRate = Math.round((((double) results.hit() / results.tried()) * 100.0) * 10.0)/10.0;

        System.out.println("success rate = " + results.hit() + "/" + results.tried() + " (" + successRate+ "%)");
        System.out.println("finished: " +
                dateFormat.format(LocalDateTime.now().atZone(ZoneId.of("America/New_York"))));

        Encog.getInstance().shutdown();

    }
}