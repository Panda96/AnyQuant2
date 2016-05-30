package vo.chart.common;

import java.util.List;

import tool.constant.StockAttribute;

/**
 * Created by kylin on 16/3/4.
 * Modified by seven on 16/3/5
 */
public class BenchmarkVO extends StockVO{
//    //大盘编号
//    String number;
//    //大盘指标信息
//    List<StockAttribute> attribute;

//
//    /**
//     * 根据获得的数据构造BenchmarkVO
//     * @param number
//     * @param attribute
//     */
    public BenchmarkVO(String number,List<StockAttribute> attribute){
//        this.number=number;
//        this.attribute=attribute;
    	super(number,"", attribute);
    }
//
//    /**
//     * 获得所有的大盘指标信息
//     * @return
//     */
//    public List<StockAttribute> getAttribute(){
//        return attribute;
//    }
//
//    /**
//     * 设置大盘的指标信息
//     * @param attribute
//     */
//    public void setAttribute(List<StockAttribute> attribute){
//        this.attribute=attribute;
//    }
}
