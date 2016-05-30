package bl.graphbl;

import blservice.graphblservice.PieChartBLService;
import data.factory.DataFactoryTxtImpl;
import data.stockdata.CacheDataTxtImpl;
import po.TradeInfoPO;
import tool.exception.NotFoundException;
import tool.utility.DateCount;
import vo.chart.piechart.PieChartVO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JiachenWang on 2016/4/10.
 */
public class PieChartBLImpl implements PieChartBLService {

    private CacheDataTxtImpl cacheDataService;

    public PieChartBLImpl() {
        cacheDataService = (CacheDataTxtImpl) DataFactoryTxtImpl.getInstance().getCacheDataService();
    }

    @Override
    public PieChartVO getPieVolumnVO(String name, String number, String start, String end) throws NotFoundException {
        double buy = 0;
        double sold = 0;
        double mid = 0;
        while (!start.equalsIgnoreCase(end)) {
            TradeInfoPO po = null;
            try {
                po = cacheDataService.getLatestDayTradeInfo(number, start);
            } catch (IOException e) {
                start = DateCount.count(start, 1);
                continue;
//                throw new NotFoundException("信息未能获取");
            } catch (NotFoundException e) {
                start = DateCount.count(start, 1);
                continue;
            }
            String[] list = po.getTotalVolumn().split("-");
            buy += Double.parseDouble(list[0]);
            sold += Double.parseDouble(list[1]);
            mid += Double.parseDouble(list[2]);
            start = DateCount.count(start, 1);
        }
        try {
            TradeInfoPO po = cacheDataService.getLatestDayTradeInfo(number, end);
            String[] list = po.getTotalVolumn().split("-");
            buy += Double.parseDouble(list[0]);
            sold += Double.parseDouble(list[1]);
            mid += Double.parseDouble(list[2]);
        } catch (IOException e) {
//            throw new NotFoundException("信息未能获取");
        } catch (NotFoundException e) {

        }

        Map<String, Double> originMap = new HashMap<>();
        originMap.put("买盘", buy);
        originMap.put("卖盘", sold);
        originMap.put("中性盘", mid);
        return new PieChartVO("交易量(单位:手)", originMap);
    }

    @Override
    public PieChartVO getPieAmountVO(String name, String number, String start, String end) throws NotFoundException {
        double buy = 0;
        double sold = 0;
        double mid = 0;
        while (!start.equalsIgnoreCase(end)) {
            TradeInfoPO po = null;
            try {
                po = cacheDataService.getLatestDayTradeInfo(number, start);
            } catch (IOException e) {
                start = DateCount.count(start, 1);
                continue;
//                throw new NotFoundException("信息未能获取");
            } catch (NotFoundException e) {
                start = DateCount.count(start, 1);
                continue;
            }
            String[] list = po.getTotalAmount().split("-");
            buy += Double.parseDouble(list[0]);
            sold += Double.parseDouble(list[1]);
            mid += Double.parseDouble(list[2]);
            start = DateCount.count(start, 1);
        }
        try {
            TradeInfoPO po = cacheDataService.getLatestDayTradeInfo(number, end);
            String[] list = po.getTotalAmount().split("-");
            buy += Double.parseDouble(list[0]);
            sold += Double.parseDouble(list[1]);
            mid += Double.parseDouble(list[2]);
        } catch (IOException e) {
//            throw new NotFoundException("信息未能获取");
        } catch (NotFoundException e) {
        }

        Map<String, Double> originMap = new HashMap<>();
        originMap.put("买盘", buy/1000000);
        originMap.put("卖盘", sold/1000000);
        originMap.put("中性盘", mid/1000000);
        return new PieChartVO("交易金额(单位:百万)", originMap);
    }

}
