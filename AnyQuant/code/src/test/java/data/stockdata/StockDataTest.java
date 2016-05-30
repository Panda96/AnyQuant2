package data.stockdata;

import data.factory.DataFactoryTxtImpl;
import dataservice.stockdataservice.CacheDataService;
import org.junit.Test;
import po.StockPO;
import po.TradeInfoPO;
import tool.exception.NotFoundException;

import java.io.IOException;
import java.util.List;

/**
 * Created by kylin on 16/3/8.
 */
public class StockDataTest {

//
//    @Test
//    public void testReadAllStock() throws IOException {
//        CacheDataService dataService = DataFactoryTxtImpl.getInstance().getCacheDataService();
//        List<String> list = dataService.getAllStock("2014","sz");
//        System.out.println(list.size());
//
//        list = dataService.getAllStock("2014","sh");
//        System.out.println(list.size());
//
//        for(int i = 0;i<100;i++){
//            System.out.println(list.get(i));
//        }
//    }

    @Test
    public void testGetStock() throws NotFoundException, IOException {
//        CacheDataService dataService = DataFactoryTxtImpl.getInstance().getCacheDataService();
//        StockPO stockPO = dataService.getStock("sh600000");
//        System.out.println(stockPO.toString());

//        stockPO = dataService.getStock("sh600007");
//        System.out.println(stockPO.toString());
//
//        stockPO = dataService.getStock("sh600006");
//        System.out.println(stockPO.toString());
    }

    @Test
    public void testReadAllBench() throws IOException {
//        CacheDataService dataService = DataFactoryTxtImpl.getInstance().getCacheDataService();
//        List<String> list = dataService.getAllBenchmark();
//        System.out.println(list.size());
//        System.out.println(list.get(0));
    }

    @Test
    public void testGetBenchMark() throws NotFoundException, IOException {
//        CacheDataService dataService = DataFactoryTxtImpl.getInstance().getCacheDataService();
//        StockPO stockPO = dataService.getBenchmark("hs300");
//        System.out.println(stockPO.toString());
    }


    @Test
    public void testReadFields() throws IOException {
//        CacheDataService dataService = DataFactoryTxtImpl.getInstance().getCacheDataService();
//        String result = dataService.getAllFields();
//        System.out.println(result);
    }


    @Test
    public void testReadLatest() throws NotFoundException, IOException {
//        CacheDataService dataService = DataFactoryTxtImpl.getInstance().getCacheDataService();
//        TradeInfoPO po = dataService.getLatestDayTradeInfo("sh600000","2016-04-01");
//        System.out.println(po.getPriceBySeconds("14:59:48"));
//        System.out.println(po.getLinesNumber());
    }
}
