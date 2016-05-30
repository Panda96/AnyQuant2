package bl.statisticbl;

import bl.blfactory.BLFactory;
import org.junit.Test;
import tool.exception.NotFoundException;
import tool.utility.MyHashItem;
import tool.utility.MySort;
import ui.chartui.linearchart.MyChartSeries;
import vo.chart.linearchart.LinearChartVO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kylin on 16/4/13.
 */
public class SingleViewImplTest {

    private SingleViewImpl singleView;

    public SingleViewImplTest() throws NotFoundException, IOException {
        singleView = new SingleViewImpl(BLFactory.getInstance().getSingleStatisticBLService(),
                BLFactory.getInstance().getStockViewService());
    }

    @Test
    public void testGetStockRSI() throws Exception {
        LinearChartVO linear = this.singleView.getStockRSI("sh600015","2015-01-01","2016-04-13");
        List<MyChartSeries> series =  linear.getChartSeries();
        for(MyChartSeries myChartSeries : series){
            HashMap<String, Double> hashMap = myChartSeries.getXyItem();
            this.printMap(hashMap);
        }
    }

    @Test
    public void testGetStockEMA() throws Exception {
        LinearChartVO linear = this.singleView.getStockEMA("sh600015","2015-01-01","2016-04-13");
        List<MyChartSeries> series =  linear.getChartSeries();
        for(MyChartSeries myChartSeries : series){
            HashMap<String, Double> hashMap = myChartSeries.getXyItem();
            this.printMap(hashMap);
        }
    }

    @Test
    public void testGetStockMACD() throws Exception {
        LinearChartVO linear = this.singleView.getStockMACD("sh600015","2015-01-01","2016-04-13");
        List<MyChartSeries> series =  linear.getChartSeries();
        for(MyChartSeries myChartSeries : series){
            HashMap<String, Double> hashMap = myChartSeries.getXyItem();
            this.printMap(hashMap);
        }
    }

    private void printMap(HashMap<String, Double> hash){
        List<MyHashItem> hashItems = MySort.sortHashmapByKey(hash);
        for (MyHashItem item:hashItems){
            System.out.print(item.getKey()+" ");
            System.out.println(item.getValue());
        }
    }
}