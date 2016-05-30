package ui.chartui.barchart;

import javafx.scene.layout.StackPane;
import ui.UIController;
import vo.chart.barchart.VolumeVO;

/**
 * Created by WJC on 16/3/21.
 */
public class BarChartController extends UIController {

    public StackPane createBarChart(VolumeVO volumevo){
        BarChartStrategy strategy;
        switch (volumevo.getType()) {
            case SINGLE:
                strategy = new SingleStrategy();
                break;
            case INDUSTRY:
                strategy = new IndustryStrategy();
                break;
            case MIX:
                strategy = new MixStrategy();
                break;
            default:
                strategy = new SingleStrategy();
        }
        return strategy.creatChart(volumevo);
    }
}
