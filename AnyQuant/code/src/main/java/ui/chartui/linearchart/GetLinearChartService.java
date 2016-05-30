package ui.chartui.linearchart;

import javafx.scene.chart.LineChart;
import vo.chart.linearchart.LinearChartVO;

/**
 * Created by kylin on 16/4/1.
 */
public interface GetLinearChartService {

    LineChart<String, Number> getStrAndNumLineChart(LinearChartVO chartVO);

}
