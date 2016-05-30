package bl.statisticbl;

import org.junit.Test;
import vo.analyse.MACDResult;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by kylin on 16/4/9.
 */
public class MACDResultTest {

    @Test
    public void test(){
        HashMap<String, Double> MACDLine = new HashMap<>();
        HashMap<String, Double> EMA9 = new HashMap<>();

        double one = 1;
        double two = 2;
        double three = 3;
        double four = 4;
        double five = 5;
        MACDLine.put("2016-03-31",two);

        MACDLine.put("2016-04-02",four);
        MACDLine.put("2016-04-03",five);


        MACDLine.put("2016-03-30",one);

        MACDLine.put("2016-04-01",three);

        MACDResult macdResult = new MACDResult(MACDLine,EMA9);

        System.out.println(macdResult.getNumberOfDays());

        System.out.println(macdResult.getDateString(0));
        System.out.println(macdResult.getDateString(1));
        System.out.println(macdResult.getDateString(2));
        System.out.println(macdResult.getDateString(3));
        System.out.println(macdResult.getDateString(4));

        System.out.println(macdResult.getPriceAtDay(2));
        System.out.println(macdResult.getPriceAtDay(3));
        System.out.println(macdResult.getPriceAtDay(4));


    }

}