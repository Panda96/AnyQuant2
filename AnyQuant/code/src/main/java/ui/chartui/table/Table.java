package ui.chartui.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import vo.chart.table.SingleLineVO;
import vo.chart.table.TableVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seven on 16/3/10.
 */
public class Table{
    TableView table;
    List<String> fields;
    List<SingleLineVO> singleLineVOs;

    /**
     * 根据tableVO建立表格
     * @param tableVO
     * @return
     */
    public TableView<SingleLineVO> createTable(TableVO tableVO){
        table=new TableView<SingleLineVO>();
        fields=tableVO.getFields();
        singleLineVOs=tableVO.getLineVOs();
        table.setLayoutX(36.0);
        table.setLayoutY(87.0);
        table.setPrefWidth(1130.0);
        table.setPrefHeight(586.0);

        table.setItems(FXCollections.observableArrayList(new ArrayList<>(singleLineVOs)));
        //生成对应的列
        for(String key:fields){
            TableColumn<SingleLineVO,String> column=new TableColumn<>(key);
            column.setMinWidth(150);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatas().get(key)));
           table.getColumns().add(column);
        }
        return table;
    }
}
