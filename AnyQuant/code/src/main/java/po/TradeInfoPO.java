package po;

import tool.utility.DateCount;
import vo.chart.common.StockPriceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kylin on 16/3/20.
 */
public class TradeInfoPO {

    private HashMap<String,TradeInfoLine> stockTradeInfo;

    private int linesNumber;

    private double open;

    private double close;

    public TradeInfoPO(HashMap<String, TradeInfoLine> stockTradeInfo) {
        this.stockTradeInfo = stockTradeInfo;
        this.linesNumber = stockTradeInfo.size();
    }

    /**
     * 得到每一秒瞬间的价格(如果存在)
     *
     * @param time
     * @return
     */
    public double getPriceBySeconds(String time) {
        if(this.stockTradeInfo.containsKey(time))
            return this.stockTradeInfo.get(time).getPrice();
        else
            return -1;
    }

    /**
     * 得到每一秒瞬间的交易量(如果存在)
     *
     * @param time
     * @return
     */
    public double getVolumeBySeconds(String time) {
        if(this.stockTradeInfo.containsKey(time))
            return this.stockTradeInfo.get(time).getVolume();
        else
            return -1;
    }

    /**
     * 得到每一秒瞬间的交易量(如果存在)
     *
     * @param time
     * @return
     */
    public double getAmountBySeconds(String time) {
        if(this.stockTradeInfo.containsKey(time))
            return this.stockTradeInfo.get(time).getAmount();
        else
            return -1;
    }

    /**
     *
     * 得到起点时间到终点时间内的平均价格(交易金额除以交易量)
     *
     * @param start
     * @param end
     * @return
     */
    public double getAccumPriceBySeconds(String start,String end){
        double accumPrice = 0;
        double accumVolume = 0;
        List<String> times = DateCount.getSeconds(start,end,1);

        //累计所有的交易金额与交易量
        for(String time:times){
            double price = this.getPriceBySeconds(time);
            if(price != -1)
                accumPrice += price;
            double volume = this.getVolumeBySeconds(time);
            if(accumPrice != -1)
                accumVolume += volume*100;
        }
        return accumPrice/accumVolume;
    }

    public List<String> getTimes(){
        ArrayList<String> times = new ArrayList<>();
        for (Map.Entry<String, TradeInfoLine> entry : stockTradeInfo.entrySet()) {
            String number = entry.getKey();
            times.add(number);
        }
        return times;
    }

    /**
     * 获得三种交易量综合数据
     * @return BUY + “-” + SOLD + “-” + MID
     */
    public String getTotalVolumn(){
        double buy = 0;
        double sold = 0;
        double mid = 0;
        for(String key : this.stockTradeInfo.keySet()){
            TradeInfoLine line = this.stockTradeInfo.get(key);
            switch (line.getType()){
                case BUY:buy +=line.getVolume();break;
                case SOLD:sold +=line.getVolume();break;
                case MID:mid +=line.getVolume();break;
            }
        }
        return buy + "-" + sold + "-" + mid;
    }

    /**
     * 获得三种交易金额综合数据
     * @return BUY + “-” + SOLD + “-” + MID
     */
    public String getTotalAmount(){
        double buy = 0;
        double sold = 0;
        double mid = 0;
        for(String key : this.stockTradeInfo.keySet()){
            TradeInfoLine line = this.stockTradeInfo.get(key);
            switch (line.getType()){
                case BUY:buy +=line.getAmount();break;
                case SOLD:sold +=line.getAmount();break;
                case MID:mid +=line.getAmount();break;
            }
        }
        return buy + "-" + sold + "-" + mid;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getOpen() {
        return open;
    }

    public double getClose() {
        return close;
    }

    public int getLinesNumber() {
        return linesNumber;
    }


    /**
     * PO至VO的转换
     *
     * @param tradeInfoPO PO
     * @return StockPriceInfo
     */
    public StockPriceInfo getPriceInfoVOFromTradeInfoPO(TradeInfoPO tradeInfoPO){
        List<String> times = tradeInfoPO.getTimes();
        List<Double> prices = new ArrayList<>();
        List<Double> volumes = new ArrayList<>();
        List<Double> amounts = new ArrayList<>();
        for (String time:times){
            Double price = tradeInfoPO.getPriceBySeconds(time);
            prices.add(price);
            Double volume = tradeInfoPO.getVolumeBySeconds(time);
            volumes.add(volume);
            Double amount = tradeInfoPO.getAmountBySeconds(time);
            amounts.add(amount);
        }
        return new StockPriceInfo(times,prices,volumes,amounts);
    }
}
