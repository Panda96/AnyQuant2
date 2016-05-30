package tool.constant;

/**
 * Created by JiachenWang on 2016/4/5.
 */
public class CalHelper {

    public static boolean isRise(double start, double end){
        if((end - start)>=0)
            return true;
        else
            return false;
    }
}
