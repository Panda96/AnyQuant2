package tool.utility;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by JiachenWang on 2016/4/13.
 */
public class LinearRegressionTest {

    @Test
    public void testCalculateLR_b() throws Exception {
        double[] x = {1,2,3,4,5};
        double[] y = {1,2,3,5,6};
        int count = 5;
        LinearRegression.calculateLR_b(x, y, count);
    }

    @Test
    public void testCalculateLR_a() throws Exception {

    }

    @Test
    public void testCalAvg() throws Exception {

    }
}