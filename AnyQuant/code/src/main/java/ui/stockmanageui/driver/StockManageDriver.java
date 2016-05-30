package ui.stockmanageui.driver;

import bl.stockmanagebl.stub.StockManageStub;
import blservice.stockmanageblservice.StockManageService;
import org.junit.Test;
import tool.constant.ResultMsg;
import tool.constant.UserInfo;

import static org.junit.Assert.assertEquals;

/**
 * Created by kylin on 16/3/6.
 */
public class StockManageDriver {

    public static void main(String[] args) {
        StockManageService stockManageService = new StockManageStub();
        StockManageDriver driver = new StockManageDriver();
        driver.drive(stockManageService);
    }

    @Test
    public void drive(StockManageService stockManageService) {
        ResultMsg resultMsg = stockManageService.addUserCollection(new UserInfo("plw","123456"),"sh603889");
        assertEquals(resultMsg.isPass(), true);

        resultMsg = stockManageService.addUserCollection(new UserInfo("plw","123456"),"sh88888888");
        assertEquals(resultMsg.isPass(), false);

        resultMsg = stockManageService.deleteUserCollection(new UserInfo("plw","123456"),"sh603889");
        assertEquals(resultMsg.isPass(), true);

//        List<StockVO> stockVOs = stockManageService.getUserStock();
    }
}
