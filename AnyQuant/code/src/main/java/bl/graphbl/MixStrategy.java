package bl.graphbl;

import bl.blfactory.BLFactory;
import blservice.stockviewblservice.StockViewService;
import tool.constant.CalHelper;
import tool.constant.StockAttribute;
import tool.enums.PeriodEnum;
import tool.enums.TypeOfVolumn;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import vo.chart.barchart.MixSingleVolumeVO;
import vo.chart.barchart.VolumeChartVO;
import vo.chart.common.StockVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiachenWang on 2016/4/5.
 */
interface MixStrategy {
    String field = "volume+open+close";
    String volume = "volume";
    String startPrice =  "open";
    String endPrice =  "close";


    public MixSingleVolumeVO calVolumn(String name, String number, String start, String end) throws NotFoundException, BadInputException;
}

class DayMStrategy implements MixStrategy {
    public MixSingleVolumeVO calVolumn(String name, String number, String start, String end) throws NotFoundException, BadInputException {

        StockViewService stockViewService = null;
        try {
            stockViewService = BLFactory.getInstance().getStockViewService();
        } catch (IOException e) {
            throw new NotFoundException("信息未能获取");
        }
        ArrayList<VolumeChartVO> list = new ArrayList<VolumeChartVO>();

        StockVO stock = stockViewService.getStock(number, start, end, field, new ArrayList<>());
        List<StockAttribute> attributes = stock.getAttribute();
        for (StockAttribute attribute : attributes) {
            boolean isRise = CalHelper.isRise(Double.parseDouble(attribute.getAttribute(startPrice)), Double.parseDouble(attribute.getAttribute(endPrice)));
            double sum =   Double.parseDouble(attribute.getAttribute(volume)) * (Double.parseDouble(attribute.getAttribute(startPrice))+Double.parseDouble(attribute.getAttribute(endPrice)))/2;
            VolumeChartVO vo = new VolumeChartVO(PeriodEnum.DAY, attribute.getDate().substring(5), (int) Double.parseDouble(attribute.getAttribute(volume))/1000, isRise, sum/10000);
            list.add(vo);
        }
        MixSingleVolumeVO mvo = new MixSingleVolumeVO(list, TypeOfVolumn.MIX, name, number);
        mvo.setLabelInfo("成交量&成交金额对比图", "时间", "千手&万元");
        return mvo;
    }
}
