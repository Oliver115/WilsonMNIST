package neural.labs.medmnist;

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
import neural.mnist.IMedLoader.Normal;

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
 * @date 30 Nov 2022
 */
public class MedTraining {

    static double TRAINING_INPUTS[][];
    static double TRAINING_IDEALS[][];

    static void init() {

        MedLoader medloader = new MedLoader();

        medloader.load("breast", "test");
        Normal normal = medloader.normalize();
    }

    /**
     * The main method.
     * @param args No arguments are used.
     */
    public static void main(final String args[]) {

        init();
        
    }
}