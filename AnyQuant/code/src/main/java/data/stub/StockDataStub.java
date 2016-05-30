package data.stub;

import data.download.APIDataServiceJson;

/**
 * Created by Seven on 16/3/6.
 */
public class StockDataStub implements APIDataServiceJson {

    /**
     * 查询指定年份和交易所的全部股票列表
     *
     * @param year     年份,4位数字
     * @param exchange 交易所，目前可以为 sz或sh，代表深交所和上交所
     * @return 全部股票列表json数据字符串
     */
    public String getAllStockJson(String year, String exchange){
        if(year.equals("2014")&&exchange.equals("sh")){
            return new String("{\"status\": \"ok\", \"data\": " +
                    "[ {\"link\": \"http://121.41.106.89:8010/api/stock/sh600000\", \"name\": \"sh600000”}]}");
        }else
            return new String("Not Found");
    }

    /**
     * 返回指定股票代码的股票交易数据，默认返回过去一个月的全部交易数据
     *
     * @param name   指定股票代码,如"sh600000"
     * @param start  起始时间，格式'YYYY-mm-dd
     * @param end    结束时间，格式'YYYY-mm-dd'
     * @param fields 指定数据字段，格式 name1+name2,如"open+high+close"
     * @return 指定股票代码的股票交易数据json字符串
     */
    public String getStockJson(String name, String start, String end, String fields) {
         if (start.equals("2014-10-13") && end.equals("2014-10-14") && fields.equals("open+high+close")) {
             return new String("{\"status\": \"ok\", " +
                     " \"data\": {\"trading_info\": " +
                     "[{\"date\": \"2014-10-13\", \"high\": 9.83, \"open\": 9.79, \"close\": 9.73}," +
                     " {\"date\": \"2014-10-14\", \"high\": 9.82, \"open\": 9.72, \"close\": 9.72}], " +
                     "  \"name\": \"sh600000\"}}");
         }
         else {
             return new String("Not Found");
         }
    }

    /**
     * 获取所有可用的benchmark，大盘指数，目前只有沪深300指数
     *
     * @return 沪深300指数json数据字符串
     * <p>
     * {"status": "ok",
     * "data":[{"link": "http://121.41.106.89:8010/api/benchmark/hs300",
     * "name": "hs300"}]}
     */
    public String getAllBenchmarkJson() {
        return new String("{\"status\": \"ok\", " +
                " \"data\": [ {\"link\": \"http://121.41.106.89:8010/api/benchmark/hs300\"," +
                " \"name\": \"hs300\"}] }");
    }

    /**
     * 获取指定大盘指数的数据
     *
     * @param bench
     * @param start  起始时间，格式 YYYY-mm-dd
     * @param end    结束时间，格式 YYYY-mm-dd
     * @param fields 指定数据字段，格式 name1+name2,如"open+close"
     * @return 指定大盘指数的数据json数据字符串
     */
    public String getBenchmarkJson(String bench, String start, String end, String fields) {
        if (bench.equals("hs300")&&start.equals("2015-01-30")&& end.equals("2015-01-30")&&fields.equals("open+close"))
            return new String("{\"status\": \"ok\", " +
                    " \"data\": {\"trading_info\": " +
                    "      [{\"date\": \"2015-03-02\", \"high\": 3608.691, \"open\": 3603.452, \"close\": 3601.265}, " +
                    "{\"date\": \"2015-03-03\", \"high\": 3579.315, \"open\": 3579.315, \"close\": 3507.9}], " +
                    "          \"name\": \"hs300\"} }");
        else
            return new String ("Not Found");
    }

    /**
     * 查看股票的可用交易数据字段，例如开盘价，收盘价等。
     * <p>
     * {"status": "ok",
     * "data":["open", "high", "low", "close",
     * "adj_price","VolumeChartVO", "turnover", "pe_ttm", "pb"]}
     * open: 开盘价 high: 最高价 low: 最低价 close: 收盘价
     * adj_price:后复权价 VolumeChartVO: 成交量 turnover: 换手率
     * pe: 市盈率 pb: 市净率
     *
     * @return 股票的可用交易数据字段
     */
    public String getAllFieldsJson() {
        return new String("{\"status\": \"ok\", " +
                " \"data\": [\"open\", \"high\", \"low\", \"close\", \"adj_price\", \"VolumeChartVO\", \"turnover\", \"pe_ttm\", \"pb\"] }");
    }
}
