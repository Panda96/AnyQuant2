package dataservice.stockdataservice;

import po.StockPO;

import java.io.IOException;
import java.util.List;

/**
 * Created by kylin on 16/3/3.
 */
public interface APIDataService {

    /**
     * 查询指定年份和交易所的全部股票列表
     *
     * @param year     年份,4位数字
     * @param exchange 交易所，目前可以为 sz或sh，代表深交所和上交所
     * @return 全部股票列表
     */
    List<String> getAllStock(String year, String exchange) throws IOException;

    /**
     * 返回指定股票代码的股票交易数据，默认返回过去一个月的全部交易数据
     *
     * @param name   指定股票代码,如"sh600000"
     * @param start  起始时间，格式'YYYY-mm-dd
     * @param end    结束时间，格式'YYYY-mm-dd'
     * @param fields 指定数据字段，格式 name1+name2,如"open+high+close"
     * @return 指定股票代码的股票交易数据
     */
    StockPO getStock(String name, String start, String end, String fields) throws IOException;


    /**
     * 获取所有可用的benchmark，大盘指数，目前只有沪深300指数
     *
     * @return 沪深300指数
     */
    List<String> getAllBenchmark() throws IOException;

    /**
     * 获取指定大盘指数的数据
     *
     * @param bench
     * @param start  起始时间，格式 YYYY-mm-dd
     * @param end    结束时间，格式 YYYY-mm-dd
     * @param fields 指定数据字段，格式 name1+name2,如"open+close"
     * @return 指定大盘指数的数据
     */
    StockPO getBenchmark(String bench, String start, String end, String fields) throws IOException;

    /**
     * 查看股票的可用交易数据字段，例如开盘价，收盘价等。
     * 返回形式为name1+name2,如"open+close"
     *
     * @return 股票的可用交易数据字段
     */
    String getAllFields() throws IOException;

    //TODO ugly code
    default String getBenchFields()throws IOException{
        return "high+adj_price+low+close+open";
    }
}
