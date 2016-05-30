package bl.graphbl;

import blservice.graphblservice.LinearChartBLService;
import data.factory.DataFactoryTxtImpl;
import dataservice.stockdataservice.CacheDataService;
import po.TradeInfoPO;
import tool.enums.LinearChartType;
import tool.exception.NotFoundException;
import tool.utility.TimeConvert;
import ui.chartui.linearchart.MyChartSeries;
import vo.chart.common.StockPriceInfo;
import vo.chart.linearchart.LinearChartVO;

import java.io.IOException;
import java.util.*;

/**
 * Created by kylin on 16/3/20.
 */
public class LinearChartImpl implements LinearChartBLService {

    private HashMap<String, StockPriceInfo> cachedTradeInfoPO;

    private CacheDataService cacheDataService;
    private HashMap<String, String> numAndNames;

    public LinearChartImpl() {
        this.cacheDataService = DataFactoryTxtImpl.getInstance().getCacheDataService();
        this.cachedTradeInfoPO = new HashMap<>();
//        this.init();
    }

    private void init() {
        try {
            this.numAndNames = this.cacheDataService.getStockNumAndName();
            //读取本地所有的股票一日交易信息并缓存
            String date = this.cacheDataService.getLatestDayOfLatestInfo();

            for (Map.Entry<String, String> entry : numAndNames.entrySet()) {
                String number = entry.getKey();
//                System.out.println(number);
                TradeInfoPO tradeInfoPO = this.cacheDataService.getLatestDayTradeInfo(number, date);
                //从PO转化为VO
                StockPriceInfo stockPriceInfo = tradeInfoPO.getPriceInfoVOFromTradeInfoPO(tradeInfoPO);
                this.cachedTradeInfoPO.put(number, stockPriceInfo);
            }
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public LinearChartVO getLineChartVO(String number, String date) throws NotFoundException {

        StockPriceInfo stockPriceInfo = this.getTradeInfoVO(number, date);

        // get info form vo
        HashMap<String, Double> timeAndPrice = stockPriceInfo.getTimeAndPrice();
        HashMap<String, Double> timeAndAccumulatePrice = stockPriceInfo.getTimeAndAccumlatePrice();

        List<String> validSeconds = TimeConvert.getSecondsInTrade(1);

        HashMap<String, Double> xyItem1 = new HashMap<>();
        HashMap<String, Double> xyItem2 = new HashMap<>();

        int stepIndex = 0;
        int step = 20;

        for (String oneSecond : validSeconds) {
            // 如果这一秒存在数据
            if (stockPriceInfo.timeGotInfo(oneSecond)) {
                stepIndex++;
                // 间隔获取数据,不显示所有秒的所有数据
                if (stepIndex % step == 0) {
                    double price = timeAndPrice.get(oneSecond);
                    double accuPrice = timeAndAccumulatePrice.get(oneSecond);
                    xyItem1.put(oneSecond, price);
                    xyItem2.put(oneSecond, accuPrice);
                }
            }
        }

        double high = stockPriceInfo.getHighest();
        double low = stockPriceInfo.getLowest();
        double stepx = (high - low);

        double lowBound = low - stepx * 0.15;
        double upBound = high + stepx * 0.15;

        MyChartSeries series1 = new MyChartSeries("即时价格", xyItem1);
        MyChartSeries series2 = new MyChartSeries("累计均价", xyItem2);

        List<MyChartSeries> myChartSeries = new ArrayList<>();
        myChartSeries.add(series1);
        myChartSeries.add(series2);

        return new LinearChartVO(LinearChartType.STOCK, myChartSeries, upBound, lowBound);
    }

    private StockPriceInfo getTradeInfoVO(String number, String date) throws NotFoundException {
        TradeInfoPO tradeInfoPO = null;
        try {
            tradeInfoPO = this.cacheDataService.getLatestDayTradeInfo(number, date);
        } catch (IOException e) {
            throw new NotFoundException("对应股票缓存文件异常,请重试");
        }
        return tradeInfoPO.getPriceInfoVOFromTradeInfoPO(tradeInfoPO);
    }


}
