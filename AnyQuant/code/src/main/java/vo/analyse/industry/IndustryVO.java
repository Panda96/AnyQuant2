package vo.analyse.industry;

import org.python.antlr.ast.Str;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by kylin on 16/3/31.
 */
public class IndustryVO {

    /**
     * 行业名称
     */
    private String industryName;

    /**
     * 行业统计信息
     */
    private IndustryBasicInfo industryBasicInfo;

    /**
     * 领头股信息
     */
    private TypicalStockInfo typicalStockInfo;

    public IndustryVO(String industryName, IndustryBasicInfo industryBasicInfo, TypicalStockInfo typicalStockInfo) {
        this.industryName = industryName;
        this.industryBasicInfo = industryBasicInfo;
        this.typicalStockInfo = typicalStockInfo;
    }

    public String getIndustryName() {
        return industryName;
    }

    public IndustryBasicInfo getIndustryBasicInfo() {
        return industryBasicInfo;
    }

    public TypicalStockInfo getTypicalStockInfo() {
        return typicalStockInfo;
    }

    //获取这个数据包所有的数据名称 与 对应的数据
    public HashMap<String, String> getKeyNameAndData(){
        List<String> dataNames = this.getDataNames();
        return new HashMap<String, String>(){
            {

                DecimalFormat df = new DecimalFormat("0.000");
                //行业名称
                 put(dataNames.get(0),industryName);
                //行业统计信息
                put(dataNames.get(1),Integer.toString(industryBasicInfo.getNumberOfstocks()));
                put(dataNames.get(2),Double.toString(industryBasicInfo.getAveragePrice()));
                put(dataNames.get(3),Double.toString(industryBasicInfo.getAverageChange()));
                put(dataNames.get(4),Double.toString(industryBasicInfo.getAverageProfit()));
                put(dataNames.get(5),df.format(industryBasicInfo.getTotalVolume()));
                put(dataNames.get(6),df.format(industryBasicInfo.getTotalAmount()));

                //领头股信息
                put(dataNames.get(7),typicalStockInfo.getStockName());
                put(dataNames.get(8),Double.toString(typicalStockInfo.getLatestPrice()));
                put(dataNames.get(9),Double.toString(typicalStockInfo.getChange()));
            }
        };
    }

    public List<String> getDataNames(){
        return new ArrayList<String>(){
            {
                add("行业名称");
                add("股票数量");
                add("平均价格");
                add("平均涨跌幅");
                add("平均每股收益");
                add("总成交量(万股)");
                add("总成交额(百万)");

                add("领头股名称");
                add("最新价格");
                add("涨跌幅");
            }
        };
    }

}
