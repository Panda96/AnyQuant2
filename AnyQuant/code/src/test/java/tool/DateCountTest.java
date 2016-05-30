package tool;

import java.util.List;

import org.junit.Test;

import tool.utility.DateCount;

public class DateCountTest {

	@Test
	public void test() {
		String beginDate = DateCount.count("2016-03-08", -30);
		System.out.println(beginDate);
	}
	
	@Test
	public void testD(){
		List <String> days = DateCount.splitDays("2016-03-02", "2016-03-02");
		System.out.println(days.size());
		for(String s:days){
			System.out.println(s);
		}
	}
	
	@Test
	public void testGetDayOfWeek(){
		int d = DateCount.getWeekOfDate("2016-03-20");
		System.out.println(d);

	}
	@Test
	public void testSeconds(){
		DateCount.getSeconds("14:52:31","14:57:31",30);

	}

}
