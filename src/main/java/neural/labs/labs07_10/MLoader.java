package neural.labs.labs07_10;

import neural.mnist.IMLoader;
import neural.mnist.MDigit;

import java.io.*;
import java.util.Arrays;
import java.util.zip.CRC32;
import org.encog.mathutil.Equilateral;

/**
 * @author Oliver Wilson & Mike Volchko
 * @date 17 Oct 2022
 *
 */

public class MLoader implements IMLoader {

    //member variables
    static CRC32 crc = new CRC32();

    static final Equilateral eq = new Equilateral(10, 1, 0);

    //initialize loadedData to 0 so the run will not break if an incorrect type of data is entered or a file
    //is not found.
    static MDigit[] loadedData = new MDigit[0];
    static double[] pixels = new double[0];
    static int label = 0;
    static int pixelsMagicNumber = 0;
    static int pixelsNumberOfItems = 0;
    static int pixelsNumberOfRows = 0;
    static int pixelsNumberOfColumns = 0;
    static int labelsMagicNumber = 0;
    static int labelsNumberOfItems = 0;

    /**
     * Gets the pixel and label data in row-major order from their respective files.
     * @param type The type of data to be returned Options: (training, testing)
     * @return Data in row-major order.
     */
    @Override
    public MDigit[] load(String type) {
        //this value will hold the value of the pixel stream.
        int currentPixelStreamValue = 0;

        if(type.toLowerCase() == "training")
        {
            //Try to find the file path. If it cannot be found throw an exception
            try
            {
                DataInputStream pixelsDis =
                        new DataInputStream(
                                new BufferedInputStream(
                                        new FileInputStream("data/train-images.idx3-ubyte")));
                pixelsMagicNumber = pixelsDis.readInt();
                pixelsNumberOfItems = pixelsDis.readInt();
                pixelsNumberOfRows = pixelsDis.readInt();
                pixelsNumberOfColumns = pixelsDis.readInt();

                // creating our loadedData array that we will return containing the loaded data
                loadedData = new MDigit[pixelsNumberOfItems];

                DataInputStream labelsDis =
                        new DataInputStream(
                                new BufferedInputStream(
                                        new FileInputStream("data/train-labels.idx1-ubyte")));
                labelsMagicNumber = labelsDis.readInt();
                labelsNumberOfItems = labelsDis.readInt();

                for(int currentMDigit = 0; currentMDigit < pixelsNumberOfItems; currentMDigit++)
                {
                    //creating our pixels array that will hold the ubyte stream for a specific pixel
                    pixels = new double[pixelsNumberOfColumns * pixelsNumberOfColumns];
                    label = labelsDis.readUnsignedByte();
                    for(int currentValueOfStream = 0; currentValueOfStream < pixels.length; currentValueOfStream++)
                    {
                        currentPixelStreamValue = pixelsDis.readUnsignedByte();
                        pixels[currentValueOfStream] = currentPixelStreamValue;
                        crc.update(currentPixelStreamValue);
                    }//for

                    MDigit loadedMDigit = new MDigit(currentMDigit, pixels, label);
                    loadedData[currentMDigit] = loadedMDigit;
                }//for

            }//try
            catch(Exception e)
            {
                System.out.println(e);
                System.out.println("Error when trying to load the training data.");
            }//catch

        }//if TRAINING DATA
        else if(type.toLowerCase() == "testing")
        {
            //Try to find the file path. If it cannot be found throw an exception
            try
            {
                DataInputStream pixelsDis =
                        new DataInputStream(
                                new BufferedInputStream(
                                        new FileInputStream("data/t10k-images.idx3-ubyte")));
                pixelsMagicNumber = pixelsDis.readInt();
                pixelsNumberOfItems = pixelsDis.readInt();
                pixelsNumberOfRows = pixelsDis.readInt();
                pixelsNumberOfColumns = pixelsDis.readInt();

                //creating our loadedData array that we will return containing the loaded data
                loadedData = new MDigit[pixelsNumberOfItems];

                DataInputStream labelsDis =
                        new DataInputStream(
                                new BufferedInputStream(
                                        new FileInputStream("data/t10k-labels.idx1-ubyte")));
                labelsMagicNumber = labelsDis.readInt();
                labelsNumberOfItems = labelsDis.readInt();

                for(int currentMDigit = 0; currentMDigit < pixelsNumberOfItems; currentMDigit++)
                {
                    label = labelsDis.readUnsignedByte();
                    //creating our pixels array that will hold the ubyte stream for a specific pixel
                    pixels = new double[pixelsNumberOfColumns * pixelsNumberOfColumns];
                    for(int currentValueOfStream = 0; currentValueOfStream < pixels.length; currentValueOfStream++)
                    {
                        currentPixelStreamValue = pixelsDis.readUnsignedByte();
                        pixels[currentValueOfStream] = currentPixelStreamValue;
                        crc.update(currentPixelStreamValue);
                    }//for

                    MDigit loadedMDigit = new MDigit(currentMDigit, pixels, label);
                    loadedData[currentMDigit] = loadedMDigit;
                }//for
                System.out.println(loadedData.length);

            }//try
            catch(Exception e)
            {
                System.out.println(e);
                System.out.println("Error when trying to load the training data.");
            }//catch
        }//else if TESTING DATA

        else
        {
            System.out.println("The type of data requested is incorrect. Please request 'training' or 'testing'.");
        }//else INCORRECT REQUEST
        return loadedData;
    }

    /**
     * Gets the pixel magic number.
     * @return Magic number
     */
    @Override
    public int getPixelsMagic() {
        return pixelsMagicNumber;
    }

    /**
     * Gets the label magic number.
     * @return Magic number
     */
    @Override
    public int getLabelsMagic() {
        return labelsMagicNumber;
    }

    /**
     * Gets the checksum over the pixels <i>only</i>.
     * @return Checksum
     */
    @Override
    public long getChecksum() {
        return crc.getValue();
    }

    /**
     * Normalizes the data.
     * @return Normalized data
     */
    @Override
    public Normal normalize() {

        double[][] normalizedPixels = new double[loadedData.length][784];
        double[][] normalizedLabels = new double[loadedData.length][9];

        for (int i = 0; i < loadedData.length; i++) {

            var tempPixels = loadedData[i].pixels();
            // Normalize the pixels: divide every pixel by 255 using double-precision division.
            for (int k = 0; k < (tempPixels.length); k++) {
                var pixel = ( (tempPixels[k]) / (255.0) );
                normalizedPixels[i][k] = pixel;
            }

            //var tempLabels = eq.encode(loadedData[i].label());
            // Normalize the labels: use the Equilateral encoder for 10 categories in the normalized range, 0.0-1.0.
            /**for (int j = 0; j < tempLabels.length; j++) {
                normalizedLabels[i][j] = tempLabels[j];
            }*/
            normalizedLabels[i] = eq.encode(loadedData[i].label());
        }
        Normal normalizedData = new Normal(normalizedPixels, normalizedLabels);

        return normalizedData;
    }
}
