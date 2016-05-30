package blservice.statisticsblservice;

import tool.enums.PeriodEnum;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import vo.analyse.single.BasicSingleVO;
import vo.chart.linearchart.LinearChartVO;

/**
 * Created by JiachenWang on 2016/3/21.
 */
public interface SingleViewService {

    /**
     * 获得单股综合分析以及预测
     * @param stock_name 股票名称
     * @param stock_num 股票编号
     * @param period 时间间隔
     * @param startDate 起始时间
     * @param endDate 结束时间
     * @return
     */
    BasicSingleVO getBasicSingleInfo(String stock_name, String stock_num, PeriodEnum period, String startDate, String endDate) throws BadInputException, NotFoundException;

    /**
     * 获得数值折线图
     * @param stock_num
     * @param startDate
     * @param endDate
     * @return
     * @throws BadInputException
     * @throws NotFoundException
     */
    LinearChartVO getStockRSI(String stock_num, String startDate, String endDate)throws BadInputException, NotFoundException;

    /**
     * 获得数值折线图
     * @param stock_num
     * @param startDate
     * @param endDate
     * @return
     * @throws BadInputException
     * @throws NotFoundException
     */
    LinearChartVO getStockEMA(String stock_num, String startDate, String endDate)throws BadInputException, NotFoundException;

    /**
     * 获得数值折线图
     * @param stock_num
     * @param startDate
     * @param endDate
     * @return
     * @throws BadInputException
     * @throws NotFoundException
     */
    LinearChartVO getStockMACD(String stock_num, String startDate, String endDate)throws BadInputException, NotFoundException;

}
