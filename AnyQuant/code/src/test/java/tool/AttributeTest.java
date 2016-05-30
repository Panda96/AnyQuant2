package tool;

import bl.stockviewbl.StockViewImpl;
import blservice.stockviewblservice.StockViewService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tool.enums.Attribute;
import tool.exception.NotFoundException;

import java.util.List;

public class AttributeTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testGetCH() {
		System.out.println(Attribute.HIGH.getCH());
	}

	@Test
	public void testGetEN() {
		System.out.println(Attribute.HIGH.getEN());
	}

	@Test
	public void testGetCHbyEN() throws NotFoundException {
		StockViewService st = new StockViewImpl();
		List<String> fields = st.getAllFields();
		for(String s: fields){
			System.out.println(Attribute.getCHbyEN(s));
		}

	}
	
	@Test
	public void testGetENbyCH(){
		System.out.println(Attribute.getENbyCH("最高价"));
		
	}

}
