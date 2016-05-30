package bl.statisticbl;

import bl.blfactory.BLFactory;
import blservice.statisticsblservice.TechnicalAnalysisStrategy;
import blservice.stockviewblservice.StockViewService;
import org.junit.Test;
import tool.exception.NotFoundException;
import tool.utility.MyHashItem;
import tool.utility.MySort;
import vo.chart.common.StockVO;

import java.io.IOException;
import java.util.*;

/**
 * Created by kylin on 16/4/9.
 */
public class TechnicalAnalysisTest {

    private TechnicalAnalysisStrategy strategy;

    private StockViewService stockViewService;

    public TechnicalAnalysisTest() throws IOException, NotFoundException {
        this.stockViewService = BLFactory.getInstance().getStockViewService();
        strategy = new TechnicalAnalysis();
    }

    private void printMap(HashMap<String, Double> hash){
        List<MyHashItem> hashItems = MySort.sortHashmapByKey(hash);
        for (MyHashItem item:hashItems){
            System.out.print(item.getKey()+" ");
            System.out.println(item.getValue());
        }
    }

    @Test
    public void testCalculateEMA() throws Exception {
        StockVO stockVO = this.stockViewService.getStock("sh600015","2015-01-01","2016-04-13",
                "open+close+high",null);
        HashMap<String, Double> hashMap = this.strategy.calculateEMA(stockVO,6);
        this.printMap(hashMap);
    }

    @Test
    public void testCalculateMACD() throws Exception {
//        StockVO stockVO = this.stockViewService.getStock("sh600000","2015-01-01","2016-04-13",
//                "open+close+high",null);
//        MACDResult macdResult = this.strategy.calculateMACD(stockVO);
//        HashMap<String, Double> hashMap = macdResult.getBarValue();
//        this.printMap(hashMap);
    }

    @Test
    public void testCalculateRSI() throws Exception {
        StockVO stockVO = this.stockViewService.getStock("sh600015","2016-01-01","2016-04-13",
                "open+close+high",null);
        HashMap<String, Double> hashMap = this.strategy.calculateRSI(stockVO,6);
        this.printMap(hashMap);
    }
}