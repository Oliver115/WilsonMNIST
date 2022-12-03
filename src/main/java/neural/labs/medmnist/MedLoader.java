package neural.labs.medmnist;

import neural.mnist.IMedLoader;
import neural.mnist.MDigit;

import java.io.*;
import java.util.Arrays;
import java.util.zip.CRC32;
import org.encog.mathutil.Equilateral;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.lang.reflect.Array;

/**
 * @author Oliver Wilson & Mike Volchko
 * @date 17 Oct 2022
 *
 */

public class MedLoader implements IMedLoader {

    //member variables
    //static final Equilateral eq = new Equilateral(1, 1, 0); 

    //initialize loadedData to 0 so the run will not break if an incorrect type of data is entered or a file
    //is not found.
    static MDigit[] loadedData = new MDigit[0];
    static double[] pixels = new double[0];

    /**
     * Gets the pixel and label data in row-major order from their respective files.
     * @param type The type of data to be returned Options: (training, testing)
     * @return Data in row-major order.
     */
    @Override
    public MDigit[] load(String type, String mode) {

        if ((type.toLowerCase() == "breast") && (mode.toLowerCase() == "train")) {
            // Try to find the file path. If it cannot be found throw an exception
            try {
                loadedData = new MDigit[546];

                // Pixels
                FileReader filereader_images = new FileReader("data/breastMNIST/breast_train_images.csv");
                CSVReader csvReaderI = new CSVReader(filereader_images);

                // Labels
                FileReader filereader_labels = new FileReader("data/breastMNIST/breast_train_labels.csv");
                CSVReader csvReaderL  = new CSVReader(filereader_labels);

                // we are going to read data line by line
                String[] nextRecordI;
                String[] nextRecordL;
                for (int i = 0; i < 546; i++) {
                    // Pixels
                    nextRecordI = csvReaderI.readNext();
                    double[] doubleArray = Arrays.stream(nextRecordI).mapToDouble(Double::parseDouble).toArray();

                    // Label
                    double label;
                    nextRecordL = csvReaderL.readNext();
                    
                    if ((nextRecordL[0].charAt(0)) == 49) {
                        label = 1.0;
                    }
                    else {
                        label = 0.0;
                    }

                    // Create MDigit
                    MDigit loadedMDigit = new MDigit(i, doubleArray, label);
                    loadedData[i] = loadedMDigit;

                }
                csvReaderI.close();
                csvReaderL.close();
            }
            catch(Exception e) {
                System.out.println(e);
                System.out.println("Error when trying to load the training data.");
            }
        }
        else if ((type.toLowerCase() == "breast") && (mode.toLowerCase() == "test")) {
            // Try to find the file path. If it cannot be found throw an exception
            try {
                loadedData = new MDigit[156];

                // Pixels
                FileReader filereader_images = new FileReader("data/breastMNIST/breast_test_images.csv");
                CSVReader csvReaderI = new CSVReader(filereader_images);

                // Labels
                FileReader filereader_labels = new FileReader("data/breastMNIST/breast_test_labels.csv");
                CSVReader csvReaderL  = new CSVReader(filereader_labels);

                // we are going to read data line by line
                String[] nextRecordI;
                String[] nextRecordL;
                for (int i = 0; i < 156; i++) {
                    // Pixels
                    nextRecordI = csvReaderI.readNext();
                    double[] doubleArray = Arrays.stream(nextRecordI).mapToDouble(Double::parseDouble).toArray();

                    // Label
                    double label;
                    nextRecordL = csvReaderL.readNext();
                    
                    if ((nextRecordL[0].charAt(0)) == 49) {
                        label = 1.0;
                    }
                    else {
                        label = 0.0;
                    }

                    //System.out.print(label);

                    // Create MDigit
                    MDigit loadedMDigit = new MDigit(i, doubleArray, label);
                    loadedData[i] = loadedMDigit;

                }
                csvReaderI.close();
                csvReaderL.close();
            }
            catch(Exception e) {
                System.out.println(e);
                System.out.println("Error when trying to load the training data.");
            }
        }
        
        else if ((type.toLowerCase() == "chest") && (mode.toLowerCase() == "train")) {
            // Try to find the file path. If it cannot be found throw an exception
            try {

                loadedData = new MDigit[78468];

                // Pixels
                FileReader filereader_images = new FileReader("data/chestMNIST/chest_train_images.csv");
                CSVReader csvReaderI = new CSVReader(filereader_images);

                // Labels
                FileReader filereader_labels = new FileReader("data/chestMNIST/chest_train_labels.csv");
                CSVReader csvReaderL  = new CSVReader(filereader_labels);

                // we are going to read data line by line
                String[] nextRecordI;
                String[] nextRecordL;
                for (int i = 0; i < 78468; i++) {
                    // Pixels
                    nextRecordI = csvReaderI.readNext();
                    double[] doubleArray = Arrays.stream(nextRecordI).mapToDouble(Double::parseDouble).toArray();

                    // Label
                    double label;
                    nextRecordL = csvReaderL.readNext();
                    
                    if ((nextRecordL[0].charAt(0)) == 49) {
                        label = 1.0;
                    }
                    else {
                        label = 0.0;
                    }

                    // Create MDigit
                    MDigit loadedMDigit = new MDigit(i, doubleArray, label);
                    loadedData[i] = loadedMDigit;

                }
                csvReaderI.close();
                csvReaderL.close();
            }
            catch(Exception e) {
                System.out.println(e);
                System.out.println("Error when trying to load the training data.");
            }
        }
        else if ((type.toLowerCase() == "chest") && (mode.toLowerCase() == "test")) {
            // Try to find the file path. If it cannot be found throw an exception
            try {

                loadedData = new MDigit[22433];

                // Pixels
                FileReader filereader_images = new FileReader("data/chestMNIST/chest_test_images.csv");
                CSVReader csvReaderI = new CSVReader(filereader_images);

                // Labels
                FileReader filereader_labels = new FileReader("data/chestMNIST/chest_test_labels.csv");
                CSVReader csvReaderL  = new CSVReader(filereader_labels);

                // we are going to read data line by line
                String[] nextRecordI;
                String[] nextRecordL;
                for (int i = 0; i < 22433; i++) {
                    // Pixels
                    nextRecordI = csvReaderI.readNext();
                    double[] doubleArray = Arrays.stream(nextRecordI).mapToDouble(Double::parseDouble).toArray();

                    // Label
                    double label;
                    nextRecordL = csvReaderL.readNext();
                    
                    if ((nextRecordL[0].charAt(0)) == 49) {
                        label = 1.0;
                    }
                    else {
                        label = 0.0;
                    }

                    // Create MDigit
                    MDigit loadedMDigit = new MDigit(i, doubleArray, label);
                    loadedData[i] = loadedMDigit;

                }
                csvReaderI.close();
                csvReaderL.close();
            }
            catch(Exception e) {
                System.out.println(e);
                System.out.println("Error when trying to load the training data.");
            }
        }
        else if ((type.toLowerCase() == "pneumonia") && (mode.toLowerCase() == "train")) {
            // Try to find the file path. If it cannot be found throw an exception
            try {

                loadedData = new MDigit[4708];

                // Pixels
                FileReader filereader_images = new FileReader("data/pneumoniaMNIST/pneumonia_train_images.csv");
                CSVReader csvReaderI = new CSVReader(filereader_images);

                // Labels
                FileReader filereader_labels = new FileReader("data/pneumoniaMNIST/pneumonia_train_labels.csv");
                CSVReader csvReaderL  = new CSVReader(filereader_labels);

                // we are going to read data line by line
                String[] nextRecordI;
                String[] nextRecordL;
                for (int i = 0; i < 4708; i++) {
                    // Pixels
                    nextRecordI = csvReaderI.readNext();
                    double[] doubleArray = Arrays.stream(nextRecordI).mapToDouble(Double::parseDouble).toArray();

                    // Label
                    double label;
                    nextRecordL = csvReaderL.readNext();
                    
                    if ((nextRecordL[0].charAt(0)) == 49) {
                        label = 1.0;
                    }
                    else {
                        label = 0.0;
                    }

                    // Create MDigit
                    MDigit loadedMDigit = new MDigit(i, doubleArray, label);
                    loadedData[i] = loadedMDigit;

                }
                csvReaderI.close();
                csvReaderL.close();
            }
            catch(Exception e) {
                System.out.println(e);
                System.out.println("Error when trying to load the training data.");
            }
        }
        else if ((type.toLowerCase() == "pneumonia") && (mode.toLowerCase() == "test")) {
            // Try to find the file path. If it cannot be found throw an exception
            try {

                loadedData = new MDigit[624];

                // Pixels
                FileReader filereader_images = new FileReader("data/pneumoniaMNIST/pneumonia_test_images.csv");
                CSVReader csvReaderI = new CSVReader(filereader_images);

                // Labels
                FileReader filereader_labels = new FileReader("data/pneumoniaMNIST/pneumonia_test_labels.csv");
                CSVReader csvReaderL  = new CSVReader(filereader_labels);

                // we are going to read data line by line
                String[] nextRecordI;
                String[] nextRecordL;
                for (int i = 0; i < 624; i++) {
                    // Pixels
                    nextRecordI = csvReaderI.readNext();
                    double[] doubleArray = Arrays.stream(nextRecordI).mapToDouble(Double::parseDouble).toArray();

                    // Label
                    double label;
                    nextRecordL = csvReaderL.readNext();
                    
                    if ((nextRecordL[0].charAt(0)) == 49) {
                        label = 1.0;
                    }
                    else {
                        label = 0.0;
                    }

                    // Create MDigit
                    MDigit loadedMDigit = new MDigit(i, doubleArray, label);
                    loadedData[i] = loadedMDigit;

                }
                csvReaderI.close();
                csvReaderL.close();
            }
            catch(Exception e) {
                System.out.println(e);
                System.out.println("Error when trying to load the training data.");
            }
        }

        else {
            System.out.println("The type of data requested is incorrect. Please request 'training' or 'testing'.");
        }
        return loadedData;
    }

    /**
     * Normalizes the data.
     * @return Normalized data
     */
    @Override
    public Normal normalize() {

        double[][] normalizedPixels = new double[loadedData.length][784];
        double[][] normalizedLabels = new double[loadedData.length][0];

        for (int i = 0; i < loadedData.length; i++) {

            var tempPixels = loadedData[i].pixels();
            // Normalize the pixels: divide every pixel by 255 using double-precision division.
            for (int k = 0; k < (tempPixels.length); k++) {
                var pixel = ( (tempPixels[k]) / (255.0) );
                normalizedPixels[i][k] = pixel;
            }

            double[] temp_label = new double[1];
            temp_label[0] = loadedData[i].label();

            normalizedLabels[i] = temp_label;
        }
        Normal normalizedData = new Normal(normalizedPixels, normalizedLabels);

        return normalizedData;
    }
}