package mnist;

import neural.labs.labs07_10.MLoader;
import neural.mnist.IMLoader;
import neural.mnist.MDigit;
import org.junit.Test;

public class testMagicNumberTrainingPixels {
    @Test
    public void test() {
        IMLoader mnist = new MLoader();
        MDigit[] loadedData = mnist.load("training");
        assert(mnist.getPixelsMagic() == 2051);
    }
}
