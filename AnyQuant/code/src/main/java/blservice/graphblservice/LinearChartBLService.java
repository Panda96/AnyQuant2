package blservice.graphblservice;

import tool.exception.NotFoundException;
import vo.chart.linearchart.LinearChartVO;

import java.io.IOException;

/**
 * Created by kylin on 16/3/20.
 */
public interface LinearChartBLService {

    /**
     * 返回指定日期的股价折线图数据
     *
     * @param number 股票代码
     * @param date 日期
     * @return 每一笔交易的股价折线图数据
     * @throws IOException 读取文件异常
     * @throws NotFoundException 数据不存在的异常
     */
    LinearChartVO getLineChartVO(String number, String date) throws NotFoundException;

}
