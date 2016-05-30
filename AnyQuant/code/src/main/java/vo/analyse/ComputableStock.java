package vo.analyse;

/**
 * Created by kylin on 16/4/9.
 *
 * 可以用于计算EMA MACD等指标的抽象股票接口
 */
public interface ComputableStock {

    /**
     * @return 这个股票数据包有多少天的数据
     */
    int getNumberOfDays();

    /**
     * 根据日期的下标获取数据
     *
     * @param dayIndex 第几天
     * @return 这天的股价
     */
    double getPriceAtDay(int dayIndex);

    /**
     * 获取此日期的涨跌价格
     *
     * @param dayIndex 第几天
     * @return 涨跌价格
     */
    double getChangeAtDay(int dayIndex);

    /**
     * 这个数据包的日期下标应该是按照日期的顺序排好序的
     * 例如 0 对应第一天,getNumberOfDays-1 对应最后一天
     *
     * @param dayIndex 第几天
     * @return 日期字符串
     */
    String getDateString(int dayIndex);

}
