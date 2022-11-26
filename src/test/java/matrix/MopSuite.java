package matrix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

/**
 * Tests to run in sequence.
 * To extend this add a test class to the SuiteClasses, that is, the array.
 * @author Mike Volchko & Oliver Wilson
 * @date 17 Sep 2022
 */
@Suite.SuiteClasses({
        SliceStartTest.class,
        CommuteTest.class,
        MopTest01.class,
        MopTest02.class,
        MopTest03.class,
        MopTest04.class,
        MopTest05.class,
        MopTest07.class,
        MopTest08.class,
        MopTest09.class,
        MopTest10.class,
        MopTest11.class,
        MopTest12.class,
        MopTest13.class,
        MopTest14.class,
        MopTest15.class,
        MopTest16.class,
        MopTest17.class
})

public class MopSuite {}
