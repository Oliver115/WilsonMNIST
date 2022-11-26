package neural.labs.lab02;

import neural.util.EncogHelper;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.BasicTraining;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import static neural.util.EncogHelper.*;

/**
 * @author Oliver Wilson & Mike Volchko
 * @date 10 Sep 2022
 */
public class Circuit1 {
    /**
     * These learning parameters generally give good results according to literature,
     * that is, the training algorithm converges with the tolerance below.
     * */
    public final static double LEARNING_RATE = 0.25;
    public final static double LEARNING_MOMENTUM = 0.25;

    /** Inputs necessary for XOR. */
    public static double CIRCUIT1_INPUTS[][] = {
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 1.0},
            {0.0, 1.0, 0.0},
            {0.0, 1.0, 1.0},
            {1.0, 0.0, 0.0},
            {1.0, 0.0, 1.0},
            {1.0, 1.0, 0.0},
            {1.0, 1.0, 1.0}
    };

    /** Ideals necessary for XOR.*/
    public static double CIRCUIT1_IDEALS[][] = {
            {1.0},
            {1.0},
            {0.0},
            {1.0},
            {0.0},
            {0.0},
            {0.0},
            {1.0}
    };

    /**
     * The main method.
     * @param args No arguments are used.
     */
    public static void main(final String args[]) {
        // Instantiate the network
        BasicNetwork network = new BasicNetwork();

        // Input layer plus bias node
        network.addLayer(new BasicLayer(null, true, 3));

        // Hidden layer plus bias node
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 2));

        // Output layer
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));

        // No more layers to be added
        network.getStructure().finalizeStructure();

        // Randomize the weights
        network.reset();

        EncogHelper.describe(network);

        // Create training observations
        MLDataSet trainingSet = new BasicMLDataSet(CIRCUIT1_INPUTS, CIRCUIT1_IDEALS);

        // Use a training object for the learning algorithm, backpropagation.
//        final BasicTraining training = new Backpropagation(network, trainingSet,LEARNING_RATE,LEARNING_MOMENTUM);
        final BasicTraining training = new ResilientPropagation(network, trainingSet);

        // Set learning batch size: 0 = batch, 1 = online, n = batch size
        // See org.encog.neural.networks.training.BatchSize
        // train.setBatchSize(0);

        int epoch = 0; // Epoch

        double minError = Double.MAX_VALUE;

        double error = 0.0; // Error

        int sameCount = 0; // Same Count
        final int MAX_SAME_COUNT = 5*LOG_FREQUENCY;

        EncogHelper.log(epoch, error,false, false);
        do {
            training.iteration();

            epoch++;

            error = training.getError();

            if(error < minError) {
                minError = error;
                sameCount = 1;
            }
            else
                sameCount++;

            if(sameCount > MAX_SAME_COUNT)
                break;

            EncogHelper.log(epoch, error,false,false);

        } while (error > TOLERANCE && epoch < MAX_EPOCHS);

        training.finishTraining();

        EncogHelper.log(epoch, error,sameCount > MAX_SAME_COUNT, true);
        EncogHelper.report(trainingSet, network);
        EncogHelper.describe(network);

        Encog.getInstance().shutdown();
    }
}
