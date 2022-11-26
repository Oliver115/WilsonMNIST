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
 * @date 17 Oct 2022
 */
public class VolchkoWilsonzMTraining {

    public final static int NUM_SAMPLES = 3000;
    static String DIR = "src/main/java/neural/labs/labs07_10";

    static double TRAINING_INPUTS[][];
    static double TRAINING_IDEALS[][];

    //Referenced online documentation for Date format
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");

    static void init() {
        // Get mop as we may not use all training data
        IMop mop = new Mop();

        MLoader mloader = new MLoader();

        mloader.load("training");
        Normal normal = mloader.normalize();

        TRAINING_INPUTS = mop.slice(normal.pixels(), 0, NUM_SAMPLES);
            assert(TRAINING_INPUTS[0].length == (28*28));

        TRAINING_IDEALS = mop.slice(normal.labels(), 0, NUM_SAMPLES);
            assert(TRAINING_IDEALS[0].length == (10-1));
    }

    /**
     * The main method.
     * @param args No arguments are used.
     */
    public static void main(final String args[]) {

        init();
        
        // Instantiate the network
        BasicNetwork network = new BasicNetwork();

        // Input layer
        network.addLayer(new BasicLayer(null, false, 784));

        // Hidden layers 
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 100));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 75));

        // Output layer
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 9));

        // No more layers to be added
        network.getStructure().finalizeStructure();

        // Randomize the weights
        network.reset();

        EncogHelper.summarize(network);

        // Create training observations
        BasicMLDataSet trainingSet = new BasicMLDataSet(TRAINING_INPUTS, TRAINING_IDEALS);

        // Use a training object for the learning algorithm, backpropagation.
        // final BasicTraining training = new Backpropagation(network, trainingSet,LEARNING_RATE,LEARNING_MOMENTUM);
        final BasicTraining training = new ResilientPropagation(network, trainingSet);

        // Set learning batch size: 0 = batch, 1 = online, n = batch size
        // See org.encog.neural.networks.training.BatchSize
        // train.setBatchSize(0);

        int epoch = 0;

        double minError = Double.MAX_VALUE;

        double error = 0.0;

        int sameCount = 0;
        final int MAX_SAME_COUNT = 5*LOG_FREQUENCY;

        System.out.println("started: " +
                dateFormat.format(LocalDateTime.now().atZone(ZoneId.of("America/New_York"))));
        EncogHelper.log(epoch, error,false, false);
        do {
            training.iteration();

            epoch++;

            error = training.getError();

            if (error < minError) {
                minError = error;
                sameCount = 1;
                EncogDirectoryPersistence.saveObject(new File(DIR+"/encogmnist-"+NUM_SAMPLES+ ".bin"),network);
            }
            else
                sameCount++;

            if (sameCount > MAX_SAME_COUNT)
                break;

            EncogHelper.log(epoch, error,false,false);

        } while (error > TOLERANCE && epoch < MAX_EPOCHS);

        if (error < minError) {
            EncogDirectoryPersistence.saveObject(new File(DIR+"/encogmnist-"+NUM_SAMPLES+ ".bin"),network);
        }
        training.finishTraining();
        EncogHelper.log(epoch, error,sameCount > MAX_SAME_COUNT, true);
        //EncogHelper.report(trainingSet, network);
        //EncogHelper.describe(network);                //Commented out due to output being too long.

        MExercise trainingResults = new MExercise(network, trainingSet);
        MExercise.Report results = trainingResults.report();

        double successRate = Math.round((((double) results.hit() / results.tried()) * 100.0) * 10.0)/10.0;

        System.out.println("success rate = " + results.hit() + "/" + results.tried() + " (" + successRate+ "%)");
        System.out.println("finished: " +
                dateFormat.format(LocalDateTime.now().atZone(ZoneId.of("America/New_York"))));

        Encog.getInstance().shutdown();
    }
}