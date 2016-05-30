package blservice.statisticsblservice;

import tool.exception.BadInputException;
import vo.analyse.ComputableStock;
import vo.analyse.MACDResult;
import vo.analyse.RSIResult;

import java.util.HashMap;

/**
 * Created by kylin on 16/4/9.
 */
public interface TechnicalAnalysisStrategy {

    /**
     * 计算一只股票以一定时间间隔为单位的RSI指数
     *
     * @param computableStock 可计算股票数据包接口
     * @param step            间隔天数
     *
     * @return
     * @throws BadInputException
     */
    HashMap<String,Double> calculateRSI(ComputableStock computableStock, int step) throws BadInputException;

    /**
     * 计算一只股票以一定时间间隔为单位的MACD指数(Moving Average Convergence Divergence)
     * <p/>
     * It makes use of moving averages of different time frames to indicate momentum changes
     * and swings in the mood of the crowd, to give buying and selling signals
     * that catches the big moves.
     * By many thought to be the best of all technical analysis indicators available
     * <p/>
     *
     * @param computableStock 可计算股票数据包接口
     * @return MACDResult结果数据包
     * @throws BadInputException 输入参数不恰当异常
     */
    MACDResult calculateMACD(ComputableStock computableStock) throws BadInputException;

    /**
     * 计算一只股票以一定时间间隔为单位的EMA指数(Exponetial Moving Average)
     * <p/>
     * Exponetial Moving Average (EMA for short) is one of the most used indicators
     * in technical analysis today.
     *
     * @param computableStock 可计算股票数据包接口
     * @param step            间隔天数
     * @return EMA指数包(日期 数据)从入口结果的第二天开始
     * @throws BadInputException 输入参数不恰当异常
     */
    HashMap<String, Double> calculateEMA(ComputableStock computableStock, int step) throws BadInputException;


}
