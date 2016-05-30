package bl.toolTest;

import org.junit.Test;
import tool.utility.DateCount;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by kylin on 16/3/18.
 */
public class DateCountTest {

    @Test
    public void test(){
        String tody = DateCount.dateToStr(new Date());
        String yesterDay = DateCount.count(tody,-2);
        System.out.println(tody);
        System.out.println(yesterDay);
        assertEquals(tody.compareTo(yesterDay) > 0,true);
    }
}
