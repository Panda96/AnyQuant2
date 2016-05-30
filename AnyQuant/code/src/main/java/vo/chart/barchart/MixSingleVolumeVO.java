package vo.chart.barchart;

import tool.enums.TypeOfVolumn;

import java.util.ArrayList;

/**
 * Created by JiachenWang on 2016/4/1.
 */
public class MixSingleVolumeVO extends VolumeVO {

    String stock_name;
    String stock_num;

    public MixSingleVolumeVO(ArrayList<VolumeChartVO> list, TypeOfVolumn type, String stock_name, String stock_num) {
        super(list, type);
        this.stock_name = stock_name;
        this.stock_num = stock_num;
    }

}
