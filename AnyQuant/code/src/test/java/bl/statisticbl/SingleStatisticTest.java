package bl.statisticbl;

import bl.stockviewbl.StockViewImpl;
import blservice.statisticsblservice.SingleStatisticBLService;
import blservice.stockviewblservice.StockViewService;
import org.junit.Test;
import tool.constant.R;
import tool.exception.NotFoundException;
import vo.chart.common.StockVO;

import java.util.ArrayList;

/**
 * Created by JiachenWang on 2016/4/11.
 */
public class SingleStatisticTest {

    private SingleStatisticBLService singleBL;
    private StockViewService stockViewService;

    public SingleStatisticTest() throws NotFoundException {
        stockViewService = new StockViewImpl();
        singleBL = new SingleStatisticBLImpl(stockViewService);
    }

    @Test
    public void testMACD() throws Exception {

    }

    @Test
    public void testVariance() throws Exception {
//        StockVO stock = stockViewService.getStock("sh600000", "2016-03-20", "2016-03-29", R.field.all, new ArrayList<>());
//        System.out.println(singleBL.getVarianceOfPrice(stock));
    }

    @Test
    public void testAvgPrice() throws Exception {
//        StockVO stock = stockViewService.getStock("sh600000", "2016-03-20", "2016-03-29", R.field.all, new ArrayList<>());
//        System.out.println(singleBL.getAvgPrice(stock));
    }

    @Test
    public void testAllVariance() throws Exception {
        System.out.println(singleBL.getAllVarianceOfPrice("2016-03-20", "2016-03-29"));
    }
}
