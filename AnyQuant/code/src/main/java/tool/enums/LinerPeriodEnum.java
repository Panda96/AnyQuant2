package tool.enums;

/**
 * Created by kylin on 16/3/23.
 */
public enum  LinerPeriodEnum {

    Second("分时"),FiveMinute("5分"),FifteenMin("15分"),Hour("60分");

    LinerPeriodEnum(String s) {
    }

    public String getLinerPeriodEnum(String period){
        return PeriodEnum.valueOf(period).toString();
    }
}
