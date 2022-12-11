package neural.labs.medmnist;

import neural.matrix.Mop;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.BasicTraining;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

import neural.util.EncogHelper;

import neural.matrix.IMop;
import neural.mnist.IMedLoader.Normal;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static neural.util.EncogHelper.*;

/**
 * Original code by: Ron Coleman (24 Oct 2017)
 * @authors: Michael Volchko and Oliver Wilson
 * @date 30 Nov 2022
 */
public class MedTraining {

    static double TRAINING_INPUTS[][];
    static double TRAINING_IDEALS[][];

    //Referenced online documentation for Date format
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");
    static String DIR = "src/main/java/neural/labs/medmnist";

    /**
     * ================= SPECIFY DATASET HERE! =================
     */
    static String DATASET = "breast";
    public static int num_samples = 100; // default?


    static void init() {

        IMop mop = new Mop();

        MedLoader medloader = new MedLoader();

        if (DATASET == "pneumonia") {
            medloader.load("pneumonia", "train"); // Dataset size: 4,708
            num_samples = 4000;
        }
        else if (DATASET == "chest") {
            medloader.load("chest", "train"); // Dataset size: 78,468
            num_samples = 100;
        }
        else if (DATASET == "breast") {
            medloader.load("breast", "train"); // Dataset size: 546
            num_samples = 300;
        }
        else {
            System.out.println("Error while loading data.");
        }
        
        Normal normal = medloader.normalize();

        TRAINING_INPUTS = mop.slice(normal.pixels(), 0, num_samples);
        TRAINING_IDEALS = mop.slice(normal.labels(), 0, num_samples);
    }
    /**
     * The main method.
     * @param args No arguments are used.
     */
    public static void main(final String args[]) {

        init();
        System.out.println("Num_Samples: " + num_samples);

         // Instantiate the network
         BasicNetwork network = new BasicNetwork();

         // Input layer
         network.addLayer(new BasicLayer(null, false, 784));
 
         // Hidden layers 
         network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 100));
 
         // Output layer
         network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
 
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
                 EncogDirectoryPersistence.saveObject(new File(DIR+"/encogmnist-" + DATASET + ".bin"),network);
             }
             else
                 sameCount++;
 
             if (sameCount > MAX_SAME_COUNT)
                 break;
 
            EncogHelper.log(epoch, error, false,false);
 
         } while (error > TOLERANCE && epoch < MAX_EPOCHS);

        if (error < minError) {
            EncogDirectoryPersistence.saveObject(new File(DIR + "/models" + "/encogmnist-" + DATASET + ".bin"),network);
        }
        training.finishTraining();
        EncogHelper.log(epoch, error,sameCount > MAX_SAME_COUNT, true);

        MedExercise trainingResults = new MedExercise(network, trainingSet);
        MedExercise.Report results = trainingResults.report();

        double successRate = Math.round((((double) results.hit() / results.tried()) * 100.0) * 10.0)/10.0;

        System.out.println("success rate = " + results.hit() + "/" + results.tried() + " (" + successRate+ "%)");
        System.out.println("finished: " +
                dateFormat.format(LocalDateTime.now().atZone(ZoneId.of("America/New_York"))));

                Encog.getInstance().shutdown();
    }
}
    