package vo.analyse.industry;

import tool.enums.TypeOfVolumn;
import vo.chart.barchart.VolumeChartVO;
import vo.chart.barchart.VolumeVO;

import java.util.ArrayList;

/**
 * Created by kylin on 16/3/31.
 */
public class IndustryVolumeVO extends VolumeVO {

    public IndustryVolumeVO(ArrayList<VolumeChartVO> list, TypeOfVolumn type) {
        super(list, type);
    }

}
