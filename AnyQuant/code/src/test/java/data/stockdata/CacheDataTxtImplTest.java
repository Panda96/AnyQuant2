package data.stockdata;

import data.factory.DataFactory;
import data.factory.DataFactoryTxtImpl;
import dataservice.stockdataservice.CacheDataService;
import org.junit.Test;
import org.python.bouncycastle.asn1.dvcs.Data;
import po.TradeInfoPO;

import static org.junit.Assert.*;

/**
 * Created by kylin on 16/4/3.
 */
public class CacheDataTxtImplTest {

    private CacheDataService dataService;

    public CacheDataTxtImplTest(){
        dataService = DataFactoryTxtImpl.getInstance().getCacheDataService();
    }

    @Test
    public void testGetAllFields() throws Exception {

    }

    @Test
    public void testGetLatestDayTradeInfo() throws Exception {
//        TradeInfoPO tradeInfoPO = dataService.getLatestDayTradeInfo("sh600000","2016-04-01");
//        System.out.println(tradeInfoPO.getClose());
//        System.out.println(tradeInfoPO.getOpen());
//        System.out.println(tradeInfoPO.getLinesNumber());
    }

    @Test
    public void testGetLatestDayOfLatestInfo() throws Exception {
        String date = dataService.getLatestDayOfLatestInfo();
        System.out.println("已经缓存的最新的详细交易日期是:"+date);
    }
}