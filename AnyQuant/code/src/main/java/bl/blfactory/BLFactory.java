package bl.blfactory;

import bl.graphbl.BarChartBLImpl;
import bl.graphbl.LinearChartImpl;
import bl.graphbl.PieChartBLImpl;
import bl.statisticbl.IndustryViewImpl;
import bl.statisticbl.SingleStatisticBLImpl;
import bl.statisticbl.SingleViewImpl;
import bl.stockmanagebl.StockManageImpl;
import bl.stockviewbl.StockViewImpl;
import blservice.blfactoryservice.BLFactoryService;
import blservice.graphblservice.BarChartBLService;
import blservice.graphblservice.LinearChartBLService;
import blservice.graphblservice.PieChartBLService;
import blservice.statisticsblservice.IndustryViewService;
import blservice.statisticsblservice.SingleStatisticBLService;
import blservice.statisticsblservice.SingleViewService;
import blservice.stockmanageblservice.StockManageService;
import blservice.stockviewblservice.LoginBLService;
import blservice.stockviewblservice.StockViewService;
import tool.exception.NotFoundException;

import java.io.IOException;

/**
 * Created by kylin on 16/3/17.
 */
public class BLFactory implements BLFactoryService {

    private LoginBLService loginBLService;

    private StockManageService stockManageService;

    private StockViewService stockViewService;

    private SingleViewService singleViewService;

    private LinearChartBLService basicGraphBLService;

    private IndustryViewService industryViewService;

    private BarChartBLService barChartBLService;

    private PieChartBLService pieChartBLService;

    private SingleStatisticBLService singleStatisticBLService;

    private static BLFactory instance;

    private BLFactory() throws IOException, NotFoundException {
        stockViewService = new StockViewImpl();
        singleStatisticBLService = new SingleStatisticBLImpl(stockViewService);

        singleViewService = new SingleViewImpl(singleStatisticBLService,stockViewService);
        stockManageService = new StockManageImpl();


        basicGraphBLService = new LinearChartImpl();
        industryViewService = new IndustryViewImpl(stockViewService);
        barChartBLService = new BarChartBLImpl();
        pieChartBLService = new PieChartBLImpl();
    }

    public static BLFactory getInstance() throws IOException, NotFoundException {
        if (instance == null)
            instance = new BLFactory();
        return instance;
    }
    @Override
    public LoginBLService getLoginBLService() {
        return loginBLService;
    }

    @Override
    public StockManageService getStockManageService() {
        return stockManageService;
    }

    @Override
    public StockViewService getStockViewService() {
        return stockViewService;
    }

    @Override
    public LinearChartBLService getLinearChartBLService() {
        return basicGraphBLService;
    }

    public SingleViewService getSingleViewService() {
        return singleViewService;
    }

    @Override
    public IndustryViewService getIndustryViewService() {
        return industryViewService;
    }

    @Override
    public BarChartBLService getBarChartBLService() {
        return barChartBLService;
    }

    @Override
    public SingleStatisticBLService getSingleStatisticBLService() {
        return singleStatisticBLService;
    }

    @Override
    public PieChartBLService getPieChartBLService() {
        return pieChartBLService;
    }
}
