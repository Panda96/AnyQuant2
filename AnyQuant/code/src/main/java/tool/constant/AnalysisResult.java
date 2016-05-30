package tool.constant;

/**
 * Created by JiachenWang on 2016/3/16.
 */
public class AnalysisResult {
    //目标分析对象
    private String target;
    //分析结果
    private String message;

    public AnalysisResult(String target, String message) {
        this.target = target;
        this.message = message;
    }

    public String getTarget() {
        return target;
    }

    public String getMessage() {
        return message;
    }
}
