package mnist;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

/**
 * Tests to run in sequence.
 * To extend this, add a test class to the SuiteClasses, that is, the array.
 * @author Mike Volchko & Oliver Wilson
 * @date 15 Oct 2022
 */
@Suite.SuiteClasses({
        testTrainingDigits.class,
        testMagicNumberTrainingPixels.class,
        testMagicNumberTrainingLabels.class,
        testChecksumTrainingPixels.class,
        testTestingDigits.class,
        testMagicNumberTestingPixels.class,
        testMagicNumberTestingLabels.class,
        testChecksumTestingPixels.class,
        testToStringTraining.class,
        testToStringTesting.class
})

public class MnistSuite {}
