package neural.labs.medmnist;

import org.encog.mathutil.Equilateral;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.kmeans.Centroid;

import java.util.ArrayList;
import java.util.List;

/**
 * @authors: Michael Volchko and Oliver Wilson
 * @date 29 Oct 2022
 */

public class MedExercise{
    record Report(int tried, int hit) {}

    //initializing class members
    private BasicNetwork mExerciseNetwork;
    private BasicMLDataSet mExerciseDataset;
    static final Equilateral eq = new Equilateral(10, 1, 0);

    public MedExercise(BasicNetwork network, BasicMLDataSet dataset) {
        // TODO: instantiate the members
        this.mExerciseNetwork = network;
        this.mExerciseDataset = dataset;
    }

    public Report report() {
        int tried = this.mExerciseDataset.size();
        int hit = 0;
        double decodedLabel = -1;
        double actualLabel = -2;
        double[] input;
        double[] output = new double[9];
        List<MLDataPair> data = new ArrayList<>();

        data = this.mExerciseDataset.getData();

        // TODO: calculate the hit given tried
        for(int numResultsChecked = 0; numResultsChecked < tried; numResultsChecked++) {
            //get the Data pairs for Inputs and Ideals
            input = data.get(numResultsChecked).getInput().getData();

            // Get the output and decode it to a subtype index.
            this.mExerciseNetwork.compute(input, output);
            decodedLabel = eq.decode(output);

            // Decode Ideal Label
            actualLabel = eq.decode(data.get(numResultsChecked).getIdeal().getData());

            if(decodedLabel == actualLabel)
                hit++;

        }//for

        return new Report(tried, hit);
    }
}
