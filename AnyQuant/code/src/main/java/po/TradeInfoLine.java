package po;

import tool.enums.TradeEnum;

/**
 * Created by kylin on 16/3/20.
 */
public class TradeInfoLine {

    /**
     * id
     */
    private int id;

    /**
     * 交易时间形式15:00:00
     */
    private String time;

    /**
     * 涨跌幅
     */
    private double price;

    /**
     * 价格变化-0.01 or --
     */
    private double change;

    /**
     * 成交手
     */
    private double volume;

    /**
     * 成交金额(元)
     */
    private double amount;

    /**
     * 买卖类型【买盘、卖盘、中性盘】

     */
    private TradeEnum type;

    public TradeInfoLine(int id, String time, double price, double change,
                         double volume, double amount, TradeEnum type) {
        this.id = id;
        this.time = time;
        this.price = price;
        this.change = change;
        this.volume = volume;
        this.amount = amount;
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }

    public double getChange() {
        return change;
    }

    public double getVolume() {
        return volume;
    }

    public double getAmount() {
        return amount;
    }

    public TradeEnum getType() {
        return type;
    }
}
