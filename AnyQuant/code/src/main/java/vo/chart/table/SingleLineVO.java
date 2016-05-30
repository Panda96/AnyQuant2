package vo.chart.table;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Seven on 16/4/5.
 */
public class SingleLineVO {
    //列表中的数据 表头--数据
    private HashMap<String,String> datas;

    public SingleLineVO(HashMap<String,String> data){
        datas=data;
    }

    public HashMap<String ,String> getDatas(){
        return datas;
    }

    public String getAttribute(String key){
        return datas.get(key);
    }
}
