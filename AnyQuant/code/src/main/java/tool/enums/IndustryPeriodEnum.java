package tool.enums;

import bl.statisticbl.Industry;

/**
 * Created by Seven on 16/4/1.
 */
public enum IndustryPeriodEnum{
    FIRST("第一季度"),SECOND("第二季度"),THIRD("第三季度"),FOURTH("第四季度");
    String periodName;

    IndustryPeriodEnum(String season){
        periodName=season;
    }

    public static IndustryPeriodEnum getPeriodEnum(String season){
        IndustryPeriodEnum[] periods=IndustryPeriodEnum.values();
        for(int i = 0;i<periods.length;i++){
            if(periods[i].getName().equals(season)){
                return periods[i];
            }
        }
        return IndustryPeriodEnum.FOURTH;
    }

    private String getName(){
        return periodName;
    }

    public static String getStartDate(IndustryPeriodEnum period) {
        switch (period) {
            case FIRST:
                return new String("2015-01-01");
            case SECOND:
                return new String("2015-04-01");
            case THIRD:
                return new String("2015-07-01");
            case FOURTH:
                return new String("2015-10-01");

        }
        return new String("2015-10-01");
    }
}
