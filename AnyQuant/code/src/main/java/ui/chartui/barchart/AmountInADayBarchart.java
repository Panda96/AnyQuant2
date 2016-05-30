package ui.chartui.barchart;

import data.factory.DataFactoryTxtImpl;
import data.stockdata.CacheDataTxtImpl;
import javafx.scene.layout.StackPane;
import po.TradeInfoPO;
import tool.enums.PeriodEnum;
import tool.enums.TypeOfVolumn;
import tool.exception.NotFoundException;
import tool.utility.DateCount;
import tool.utility.TimeConvert;
import vo.analyse.industry.IndustryVolumeVO;
import vo.chart.barchart.VolumeChartVO;
import vo.chart.common.StockPriceInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kylin on 16/4/7.
 */
public class AmountInADayBarchart {

    private BarChartController barChartController;

    private CacheDataTxtImpl cacheDataService;

    public AmountInADayBarchart() {
        barChartController = new BarChartController();
        cacheDataService = (CacheDataTxtImpl) DataFactoryTxtImpl.getInstance().getCacheDataService();
    }

    public StackPane getAmountInADayBarchart(String number) throws NotFoundException {
        return this.getAmountInADayBarchart(number, DateCount.getRecentWorkDay());
    }

    public StackPane getAmountInADayBarchart(String number, String date) throws NotFoundException {
        try {
            //获取每一秒的时间与金额
            TradeInfoPO tradeInfoPO = this.cacheDataService.getLatestDayTradeInfo(number,date);
            StockPriceInfo stockPriceInfo = tradeInfoPO.getPriceInfoVOFromTradeInfoPO(tradeInfoPO);
            HashMap<String, Double> timeAndAmount = stockPriceInfo.getTimeAndVolume();

            //添加间隔一定秒数的数据
            ArrayList<VolumeChartVO> volumeChartVOs = new ArrayList<>();

            List<String> validSeconds = TimeConvert.getSecondsInTrade(1);

            int stepIndex = 0;
            int step = 20;

            for(String oneSecond : validSeconds){
                // 如果这一秒存在数据
                if(stockPriceInfo.timeGotInfo(oneSecond)){
                    stepIndex++;
                    // 间隔获取数据,不显示所有秒的所有数据
                    if(stepIndex%step == 0){
                        double amount = timeAndAmount.get(oneSecond);
                        VolumeChartVO vo = new VolumeChartVO(PeriodEnum.SECOND,oneSecond, (int) amount);
                        volumeChartVOs.add(vo);
                    }
                }
            }

            IndustryVolumeVO volumeVO = new IndustryVolumeVO(volumeChartVOs, TypeOfVolumn.INDUSTRY);
            volumeVO.setLabelInfo("分时成交量统计图", "时间", "成交量/手");
            return this.barChartController.createBarChart(volumeVO);
        } catch (IOException e) {
            throw new NotFoundException("股票 "+number+"在 "+date+" 的数据不存在");
        }

    }

}
