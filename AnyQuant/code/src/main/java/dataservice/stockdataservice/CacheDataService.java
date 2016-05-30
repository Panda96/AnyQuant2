package dataservice.stockdataservice;

import data.factory.DataFactoryUrlImpl;
import po.StockPO;
import po.TradeInfoPO;
import tool.exception.NotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kylin on 16/3/7.
 */
public interface CacheDataService {
	/**
	 * 返回已经缓存到本地的股票编号和名称
	 * 
	 * @return
	 * @throws IOException
	 */
    HashMap<String,String> getStockNumAndName() throws IOException;

    /**
     * 返回行业
     * 股票编号,股票名字
     *
     * @return
     */
    HashMap<String, HashMap<String, String>> getNamesAndNames() throws IOException;

    /**
     * 返回指定股票代码的股票所有交易数据
     *
     * @param name   指定股票代码,如"sh600000"
     * @return 指定股票代码的所有股票交易数据
     */
    StockPO getStock(String name) throws NotFoundException, IOException;

    /**
     * 获取所有可用的benchmark，大盘指数，目前只有沪深300指数
     *
     * @return 沪深300指数
     */
    List<String> getAllBenchmark() throws IOException;

    /**
     * 获取指定大盘指数的数据
     *
     * @param bench 大盘指数编号
     * @return 指定大盘指数的所有数据
     */
    StockPO getBenchmark(String bench) throws NotFoundException, IOException;

    String getAllFields() throws IOException;

    default String getBenchFields()throws IOException{
        return DataFactoryUrlImpl.getInstance().getAPIDataService().getBenchFields();
    }

    /**
     * 返回指定股票代码的一个工作日的详细交易情况
     *
     * @param number
     * @return
     */
    TradeInfoPO getLatestDayTradeInfo(String number, String time) throws NotFoundException, IOException;

    /**
     * 返回缓存的最近一天的日期
     *
     * @return
     */
    String getLatestDayOfLatestInfo();
}
