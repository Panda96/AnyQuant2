package blservice.graphblservice;

import tool.enums.PeriodEnum;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import vo.chart.kchart.KChartVO;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Seven on 16/3/21.
 */
public interface KLinearChartBLService {
    /**
     * 按照时间段获得K线图数据
     * @param stockNum
     * @param period
     * @return
     */
    List<KChartVO> getKChartVO(String stockNum,PeriodEnum period,String startDate,String endDate) throws NotFoundException, BadInputException;

    double getLowerBound(String stockNum, PeriodEnum period, String startDate, String endDate) throws BadInputException, NotFoundException;

    double getUpperBound(String stockNum, PeriodEnum period, String startDate, String endDate) throws BadInputException, NotFoundException;
}
