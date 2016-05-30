package tool.enums;

/**
 * Created by Lin on 2016/3/26.
 */
public enum ChartsState {
    CANDLE(0),LINE(1),HISTORY(2),ANALYSIS(3);

    private int number;

    ChartsState(int i){
        this.number = i;
    }

    public int getState(){
        return number;
    }
}
