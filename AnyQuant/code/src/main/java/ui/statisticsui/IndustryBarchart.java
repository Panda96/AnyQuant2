package ui.statisticsui;

import bl.blfactory.BLFactory;
import blservice.statisticsblservice.IndustryViewService;
import javafx.scene.layout.StackPane;
import tool.enums.IndustryPeriodEnum;
import tool.exception.NotFoundException;
import ui.chartui.barchart.BarChartController;
import vo.analyse.industry.IndustryVolumeVO;

import java.io.IOException;

/**
 * Created by Seven on 16/4/1.
 *
 * 行业一段时间内的资金流向统计信息(柱状图)
 */
public class IndustryBarchart {

    private IndustryViewService industryViewService;

    private BarChartController chartController;

    public IndustryBarchart() throws IOException, NotFoundException {
        this.industryViewService = BLFactory.getInstance().getIndustryViewService();
        this.chartController = new BarChartController();
    }

    public StackPane getIndustryBarChart(String industryName, IndustryPeriodEnum period) throws NotFoundException {
        IndustryVolumeVO industryVolume = this.industryViewService.getIndustryVolume(industryName,period);
        return chartController.createBarChart(industryVolume);
    }
}
