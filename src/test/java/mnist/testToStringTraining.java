package mnist;

import neural.labs.labs07_10.MLoader;
import neural.mnist.IMLoader;
import neural.mnist.MDigit;
import org.junit.Test;

import java.util.Random;

public class testToStringTraining {
    @Test
    public void test() {
        IMLoader mnist = new MLoader();
        MDigit[] loadedData = mnist.load("training");

        Random rand = new Random();

        System.out.println(loadedData[rand.nextInt(loadedData.length-1)].toString());
        System.out.println(loadedData[rand.nextInt(loadedData.length-1)].toString());
        System.out.println(loadedData[rand.nextInt(loadedData.length-1)].toString());
        System.out.println(loadedData[rand.nextInt(loadedData.length-1)].toString());
        System.out.println(loadedData[rand.nextInt(loadedData.length-1)].toString());
    }
}
