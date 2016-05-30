package bl.stockmanagebl;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import bl.stockviewbl.StockViewImpl;
import data.factory.DataFactoryTxtImpl;
import dataservice.stockdataservice.CacheDataService;
import tool.constant.ResultMsg;
import tool.constant.UserInfo;
import tool.exception.NotFoundException;
import vo.chart.common.StockVO;

public class StockManageImplTest {
	static CacheDataService dataservice;
	static StockManageImpl stockmanage;
	static StockViewImpl stockview;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		stockmanage = new StockManageImpl();
		stockview = new StockViewImpl();
		dataservice = DataFactoryTxtImpl.getInstance().getCacheDataService();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testGetUserCollections() {
		List<String> collections = stockmanage.getUserCollections(new UserInfo("test", "test"));
		if(collections!=null){
			for(String s: collections){
				System.out.println(s);
			}
		}
	}

	@Test
	public void testAdd(){
		ResultMsg msg = stockmanage.addUserCollection(new UserInfo("test","test"),"sh600000");
		System.out.println(msg.isPass());
	}


}
