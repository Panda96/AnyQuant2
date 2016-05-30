package vo.chart.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by Seven on 16/4/5.
 */
public class TableVO {

    //列名
    private List<String> fields;
    //每一行存入的数据VO
    private List<SingleLineVO> lineVOs;

    //表头名称,表内数据
    public TableVO(List<SingleLineVO> lineVOs,List<String> fields){
        this.lineVOs=lineVOs;
        this.fields=fields;
    }

    public List<String> getFields(){
        return fields;
    }

    public List<SingleLineVO> getLineVOs(){
        return lineVOs;
    }
}
