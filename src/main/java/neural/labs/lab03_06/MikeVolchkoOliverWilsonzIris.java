package neural.labs.lab03_06;

import neural.util.IrisHelper;

import org.apache.commons.math3.ml.neuralnet.Network;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.mathutil.Equilateral;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.BasicTraining;
import org.encog.neural.NeuralNetworkError;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import neural.util.EncogHelper;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;
import neural.matrix.IMop;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static neural.util.EncogHelper.*;
import static org.apache.commons.math3.stat.StatUtils.min;
import static org.apache.commons.math3.stat.StatUtils.max;

/**
 * Original code by: Ron Coleman (24 Oct 2017)
 * @authors: Michael Volchko and Oliver Wilson
 * @date 23 Sep 2022
 */
public class MikeVolchkoOliverWilsonzIris {
    /**
     * These learning parameters generally give good results according to literature,
     * that is, the training algorithm converges with the tolerance below.
     * */
    final static double NORMALIZED_HI = 1;
    final static double NORMALIZED_LO = -1;

    /** Added normalizers for diagnostic testing and reporting. */
    final static Map<Double, Double> normalizers = new HashMap<>();

    static List<Double> ranges = new ArrayList<Double>();

    static final Equilateral eq = new Equilateral(IrisHelper.species2Cat.size(),
                                                    NORMALIZED_HI, NORMALIZED_LO);
    
    /**
     * @param src a 2D array or matrix in row-major order.
     * That is, the first dimension contains the rows and the second dimension
     * contains the columns. This method returns a matrix in row-major order
     */
    static double[][] normalize(double[][] src) {
        IMop mop = new Mop();
        double[][] dest = new double[0][0];
        double range_min; double range_max;

        for (int i = 0; i < (src.length); i++) {
            dest = mop.slice(src, i, (i + 1));

            /** Get min and max ranges */
            range_min = min(dest[0]); 
            ranges.add(range_min);
            range_max = max(dest[0]);
            ranges.add(range_max);


            NormalizedField norm = new NormalizedField(NormalizationAction.Normalize,
                    null, range_max, range_min, NORMALIZED_HI, NORMALIZED_LO);

            for (int k = 0; k < dest[0].length; k++) {
                normalizers.put(norm.normalize(src[i][k]), dest[0][k]);
                dest[0][k] = norm.normalize(src[i][k]);
                src[i][k] = dest[0][k];
            }
        }
        return src;
    }

    static void init() {
        IMop mop = new Mop();
        double[][] inputs;
        double[][] observations = IrisHelper.load("data/iris.csv");

        double[][] observations_ = mop.slice(observations, 0, 4);

        inputs = normalize(observations_);

        TRAINING_INPUTS = mop.dice(inputs, 0 ,120); 
        TESTING_INPUTS = mop.dice(inputs, 120, 150);

        observations_ = mop.slice(observations, 4, 5);
        double[][] outputs = encode(observations_);

        TRAINING_IDEALS = mop.dice(mop.transpose(outputs), 0, 120);
        TESTING_IDEALS = mop.dice(mop.transpose(outputs), 120, 150);

        //report("training", TRAINING_INPUTS);
        report("training", TRAINING_INPUTS, TRAINING_IDEALS);
        //report("testing", TESTING_INPUTS, TESTING_IDEALS);
        TRAINING_IDEALS = mop.transpose(TRAINING_IDEALS);
        TRAINING_INPUTS = mop.transpose(TRAINING_INPUTS);
        TESTING_IDEALS = mop.transpose(TESTING_IDEALS);
        TESTING_INPUTS = mop.transpose(TESTING_INPUTS);
    }

    static void report(String type, double[][] matrix, double[][] output) {
        DecimalFormat decim = new DecimalFormat("0.00");
        DecimalFormat decim2 = new DecimalFormat("0.0000");

        double[] valToBeDecoded;
        int decodedVal = -1;
        String irisType = "";

        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("Training Inputs");
        System.out.println("   SL:   " + ranges.get(0) + "  -  " + ranges.get(1));
        System.out.println("   SW:   " + ranges.get(2) + "  -  " + ranges.get(3));
        System.out.println("   PL:   " + ranges.get(4) + "  -  " + ranges.get(5));
        System.out.println("   PW:   " + ranges.get(6) + "  -  " + ranges.get(7));

        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("%5s %9s %18s %18s %18s", "#", "SL", "SW", "PL", "PW");
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------");
        for (int i = 0; i < 120; i++) {
            System.out.format("%5s %10s %7s %10s %7s %10s %7s %10s %7s", (i + 1), normalizers.get(matrix[0][i]), 
            decim.format(matrix[0][i]), normalizers.get(matrix[1][i]), decim.format(matrix[1][i]), 
            normalizers.get(matrix[2][i]), decim.format(matrix[2][i]), normalizers.get(matrix[3][i]), decim.format(matrix[3][i]));
            System.out.println();
        }

        System.out.println();
        System.out.println("\nTraining Outputs");
        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("%5s %10s %14s %23s", "#", "t1", "t2", "Decoding");
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------");
        for (int i = 0; i < output[0].length; i++) {
            valToBeDecoded = new double[]{output[0][i],output[1][i]};
            decodedVal = (int)eq.decode(valToBeDecoded);
            switch(decodedVal)
            {
                case 0:
                    irisType = "setosa";
                    break;
                case 1:
                    irisType = "virginica";
                    break;
                case 2:
                    irisType = "versicolor";
                    break;
            }

            System.out.format("%5s %14s %14s %15s %12s", (i + 1), decim2.format(output[0][i]), decim2.format(output[1][i]),
                    (decodedVal + " ->"), irisType);
            System.out.println();
        }
    }

    static double [][] encode(double [][] src)
    {
        int valToEncode = -2;
        double[][] encodedIris = new double[src[0].length][2];
        for(int i = 0; i < src[0].length; i++) {
            valToEncode = (int)src[0][i];
            encodedIris[i] = eq.encode(valToEncode);
        }
        return encodedIris;
    }//encode

    static double TRAINING_INPUTS[][];
    static double TRAINING_IDEALS[][];


    static double TESTING_INPUTS[][] = {
        {0.0, 0.0},
        {0.0, 1.0},
        {1.0, 0.0},
        {1.0, 1.0}
    };
    static double TESTING_IDEALS[][] = {
            {0.0},
            {1.0},
            {1.0},
            {0.0}};

    /**
     * The main method.
     * @param args No arguments are used.
     */
    public static void main(final String args[]) {

        init();
        
        // Instantiate the network
        BasicNetwork network = new BasicNetwork();

        // Input layer plus bias node
        network.addLayer(new BasicLayer(null, true, 4));

        // Hidden layer plus bias node
        network.addLayer(new BasicLayer(new ActivationTANH(), true, 4));

        // Output layer
        network.addLayer(new BasicLayer(new ActivationTANH(), false, 2));

        // No more layers to be added
        network.getStructure().finalizeStructure();

        // Randomize the weights
        network.reset();

        EncogHelper.describe(network);

        // Create training observations
        
        MLDataSet trainingSet = new BasicMLDataSet(TRAINING_INPUTS, TRAINING_IDEALS);

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

        //=================== Lab 06 ======================//
        // Receives the network output -- the equilateral encoding
        double[] output = new double[2];
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("  Network Results:");
        System.out.println();
        System.out.printf("%5s %13s %13s", "#", "Ideal", "Actual");
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------");

        double success = 0;
        for(int k = 0; k < TESTING_INPUTS.length; k++) {
            // Get the input 
            double[] input = TESTING_INPUTS[k];

            // Get the output and decode it to a subtype index.
            network.compute(input, output);
            int actualno = eq.decode(output);

            String actual = "";
            switch(actualno) {
                case 0:
                    actual = "setosa";
                    break;
                case 1:
                    actual = "virginica";
                    break;
                case 2:
                    actual = "versicolor";
                    break;
            }
            String ideal = "";
            switch(eq.decode(TESTING_IDEALS[k])) {
                case 0:
                    ideal = "setosa";
                    break;
                case 1:
                    ideal = "virginica";
                    break;
                case 2:
                    ideal = "versicolor";
                    break;
            }
            String miss = "";
            if (actual != ideal) { 
                miss = "MISSED!"; 
            }
            else { 
                success++; 
            }
            System.out.format("%5s %13s %13s %10s", (k + 1), ideal, actual, miss);
            System.out.println();
        } 
        DecimalFormat decim = new DecimalFormat("0.0");
        System.out.println("Success rate = " + (int)success + "/30" + " (" + decim.format((success / 30)*100) + "%)");
    }
}
