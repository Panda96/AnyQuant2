package blservice.blfactoryservice;

import blservice.graphblservice.BarChartBLService;
import blservice.graphblservice.LinearChartBLService;
import blservice.graphblservice.PieChartBLService;
import blservice.statisticsblservice.IndustryViewService;
import blservice.statisticsblservice.SingleStatisticBLService;
import blservice.statisticsblservice.SingleViewService;
import blservice.stockmanageblservice.StockManageService;
import blservice.stockviewblservice.LoginBLService;
import blservice.stockviewblservice.StockViewService;

/**
 * Created by kylin on 16/3/17.
 */
public interface BLFactoryService {

    LoginBLService getLoginBLService();

    StockManageService getStockManageService();

    StockViewService getStockViewService();

    LinearChartBLService getLinearChartBLService();

    SingleViewService getSingleViewService();

    IndustryViewService getIndustryViewService();

    BarChartBLService getBarChartBLService();

    SingleStatisticBLService getSingleStatisticBLService();

    PieChartBLService getPieChartBLService();
}
