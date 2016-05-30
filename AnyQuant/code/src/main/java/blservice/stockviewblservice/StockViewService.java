package blservice.stockviewblservice;

import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import vo.chart.common.StockVO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import tool.constant.ConditionSelect;

/**
 * Created by kylin on 16/3/4.
 */
public interface StockViewService {

    /**
     * 返回指定年份和交易所的全部股票列表
     *
     * @param year 年份
     * @param exchange 交易所代码
     * @return 股票列表
     */
    List<StockVO> getAllStock(String year, String exchange, String date) throws NotFoundException, BadInputException;

    /**
     * 返回指定股票代码的指定时间字段的信息
     *
     * @param name 股票代码
     * @param start 起点日期
     * @param end 终点日期
     * @param fields 股票信息字段
     * @return 股票代码的指定时间字段的信息
     */
    StockVO getStock(String name, String start, String end, String fields,List<ConditionSelect> ranges) throws NotFoundException, BadInputException;

    /**
     * 获取所有可用的大盘指数
     *
     * @return 可用的大盘指数名称
     */
    List<StockVO> getAllBenchmark(String date) throws NotFoundException, BadInputException;

    /**
     * 获取所有的大盘指数的编号
     *
     * @return 大盘指数编号的list
     */
    List<String> getAllBenches();

    /**
     * 返回指定大盘的指定时间字段的信息
     *
     * @param bench 大盘代码
     * @param start 起点日期
     * @param end 终点日期
     * @param fields 股票信息字段
     * @return 大盘的指定时间字段的信息
     */
    StockVO getBenchmark(String bench, String start, String end, String fields,List<ConditionSelect> ranges) throws NotFoundException, BadInputException;

    /**
     * 返回股票的可用交易数据字段
     *
     * @return 股票的可用交易数据字段列表
     */
    List<String> getAllFields();
    
    /**
     * 返回大盘的数据字段
     *
     * @return 大盘的数据字段
     */
    List<String> getBenchFields();

    /**
     * 返回行业
     * 股票名字,股票编号
     *
     * @return
     */
    default HashMap<String,HashMap<String,String>> getNamesAndNumbers(){
        HashMap<String,HashMap<String,String>> result = new HashMap<>();

        HashMap<String,String> oneIndx = new HashMap<>();

        oneIndx = new HashMap<>();
        oneIndx.put("光大银行","sh601818");
        oneIndx.put("华夏银行","sh600015");
        oneIndx.put("招商银行","sh600036");
        oneIndx.put("交通银行","sh601328");
        oneIndx.put("农业银行","sh601288");
        oneIndx.put("平安银行","sz000001");
        oneIndx.put("宁波银行","sz002142");
        result.put("银行业",oneIndx);

        oneIndx = new HashMap<>();
        oneIndx.put("贵州茅台","sh600519");
        oneIndx.put("山西汾酒","sh600809");
        oneIndx.put("五粮液","sz000858");
        oneIndx.put("古井贡酒","sz000596");
        oneIndx.put("水井坊","sh600779");
        oneIndx.put("青岛啤酒","sh600600");
        oneIndx.put("泸州老窖","sz000568");

        result.put("酒业",oneIndx);

        oneIndx = new HashMap<>();
        oneIndx.put("银星能源","sz000862");
        oneIndx.put("金风科技","sz002202");
        oneIndx.put("东方电气","sh600875");
        oneIndx.put("中核科技","sz000777");
        oneIndx.put("航天机电","sh600151");
        result.put("新能源",oneIndx);

        oneIndx = new HashMap<>();
        oneIndx.put("上海能源","sh600508");
        oneIndx.put("盘江股份","sh600395");
        oneIndx.put("靖远煤电","sz000552");
        oneIndx.put("远兴能源","sz000683");
        oneIndx.put("平庄能源","sz000780");
        oneIndx.put("陕西煤业","sh601225");
        result.put("煤炭",oneIndx);

        return result;
    }

    /**
     * 获取大盘名称与编号
     *
     * @return
     */
    default HashMap<String,HashMap<String,String>> getBenchNamesAndNumbers(){
        HashMap<String,HashMap<String,String>> result = new HashMap<>();
        HashMap<String,String> oneIndx = new HashMap<>();
        oneIndx.put("上证指数","hs300");
        result.put("大盘",oneIndx);
        return result;
    }

}
