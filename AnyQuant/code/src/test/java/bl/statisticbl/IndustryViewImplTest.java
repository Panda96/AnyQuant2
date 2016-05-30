package bl.statisticbl;

import bl.blfactory.BLFactory;
import org.junit.Test;
import tool.enums.IndustryPeriodEnum;
import tool.exception.NotFoundException;
import vo.analyse.industry.IndustryVO;

import java.io.IOException;

/**
 * Created by Seven on 16/4/11.
 */
public class IndustryViewImplTest {
    IndustryViewImpl industryView;

    public IndustryViewImplTest() throws IOException, NotFoundException {
        industryView = new IndustryViewImpl(BLFactory.getInstance().getStockViewService());
    }

    @Test
    public void testGetBasicIndustryInfo() throws Exception {
        IndustryVO industryVO1=industryView.getBasicIndustryInfo("酒业", IndustryPeriodEnum.FIRST);
        System.out.println(industryVO1.getIndustryBasicInfo().getAverageChange());
        IndustryVO industryVO2=industryView.getBasicIndustryInfo("酒业", IndustryPeriodEnum.SECOND);
        System.out.println(industryVO2.getIndustryBasicInfo().getAverageChange());
        IndustryVO industryVO3=industryView.getBasicIndustryInfo("酒业", IndustryPeriodEnum.THIRD);
        System.out.println(industryVO3.getIndustryBasicInfo().getAverageChange());
        IndustryVO industryVO4=industryView.getBasicIndustryInfo("酒业", IndustryPeriodEnum.FOURTH);
        System.out.println(industryVO4.getIndustryBasicInfo().getAverageChange());
    }

    @Test
    public void testGetIndustryPrice() throws Exception{
    }

    @Test
    public void testGetCompareLinearChartVO() throws Exception {

    }

    @Test
    public void testGetIndustryVolume() throws Exception {

    }
}