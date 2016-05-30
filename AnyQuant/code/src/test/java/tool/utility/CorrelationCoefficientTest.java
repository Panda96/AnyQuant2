package tool.utility;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kylin on 16/4/13.
 */
public class CorrelationCoefficientTest {

    @Test
    public void testCalculateCC() throws Exception {
        double[] arg1 = {1,2,3,4,5};
        double[] arg2 = {1,2,3,4,5};

        double[] arg3 = {2,3,4,5,6};

        System.out.println( CorrelationCoefficient.calAvg(arg1));
        System.out.println( CorrelationCoefficient.calVar(arg1));
        System.out.println( CorrelationCoefficient.calculateCC(arg1,arg2));
        System.out.println( CorrelationCoefficient.calculateCC(arg1,arg3));
    }
}