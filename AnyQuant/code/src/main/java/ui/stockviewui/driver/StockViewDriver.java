package ui.stockviewui.driver;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import bl.stockviewbl.stub.StockViewStub;
import blservice.stockviewblservice.StockViewService;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import vo.chart.common.StockVO;

/**
 * Created by kylin on 16/3/6.
 */
public class StockViewDriver {


    public static void main(String[] args) throws NotFoundException, BadInputException {

        StockViewService stockViewService = new StockViewStub();
        StockViewDriver driver = new StockViewDriver();
        driver.drive(stockViewService);
    }

    @Test

    public void drive(StockViewService stockViewService) throws NotFoundException, BadInputException {

        List<StockVO> list = stockViewService.getAllBenchmark("2016-03-02");
        assertEquals(list.size() == 1, true);

        List<String> stringList = stockViewService.getAllFields();
        assertEquals(stringList.size() >= 8, true);

//        List<StockVO> stockVOList = stockViewService.getAllStock("2014", "sh","2014-03-02");
//        assertEquals(stockVOList.size() >= 1000, true);

//        StockVO benchmarkVOs = stockViewService.getBenchmark("sh300", "2015-02-01", "2015-02-03", "open",new ArrayList<>());

//        StockVO stockVO = stockViewService.getStock("sh600000", "2015-10-01", "2015-10-02", "open+close");
    }
}
