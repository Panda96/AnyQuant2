package blservice.statisticsblservice;

import tool.enums.PeriodEnum;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import vo.analyse.MACDResult;
import vo.chart.common.StockVO;

import java.io.IOException;

/**
 * Created by JiachenWang on 2016/4/10.
 */
public interface SingleStatisticBLService {

    /**
     * 求某时间段股票日价格方差
     * @param stock 股票信息集合
     * @return
     */
     double getVarianceOfPrice(StockVO stock);

    /**
     * 求某时间段股票价格平均值
     * @param stock 股票信息集合
     * @return
     */
    double getAvgPrice(StockVO stock);

    /**
     * 求某时间段所有股票日价格方差集合的1/4,1/2,3/4分位点
     * @return double + "-" + double + "-" + double
     */
    String getAllVarianceOfPrice(String startDate, String endDate) throws NotFoundException;

    /**
     * 计算近90天两只股票的相关系数
     * @param stockname1
     * @param stockname2
     * @return
     * @throws NotFoundException
     * @throws BadInputException
     */
    public String cal90CC(String stockname1, String stockname2) throws NotFoundException, BadInputException, IOException;

    /**
     * 获得股票MACD指数
     *
     * @param stock_name
     * @param stock_num
     * @param period
     * @param startDate
     * @param endDate
     * @return
     */
    MACDResult getMACDValue(String stock_name, String stock_num, PeriodEnum period, String startDate, String endDate) throws NotFoundException, BadInputException, IOException;
}
