package ui.stockviewui;

import bl.blfactory.BLFactory;
import blservice.stockviewblservice.StockViewService;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import tool.enums.Attribute;
import tool.constant.ConditionSelect;
import tool.constant.R;
import tool.constant.StockAttribute;
import tool.exception.*;
import ui.UIController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seven on 16/3/8.
 *
 *
 */
public class SingleStockViewController extends UIController {

    public TableView<StockAttribute> attribute_TabelView;
    public TextField openfrom_TF;
    public TextField opento_TF;
    public TextField closefrom_TF;
    public TextField closeto_TF;
    public TextField highfrom_TF;
    public TextField highto_TF;
    public TextField lowfrom_TF;
    public TextField lowto_TF;


    @FXML
    Label msg;

    //根据当前fields信息生成相应的选择框
    public HBox hBox=new HBox();
//    List<CheckBox> boxes=new ArrayList<CheckBox>();

    //默认一直显示日期列
    public TableColumn<StockAttribute,String> date_Column;

    public Button reset;
    public Button confirm;
    public static String startDate, endDate;
    public StockViewService stockViewService;
    public List<ConditionSelect> conditions;
    List<String> fields=new ArrayList<>();
    private static String stockname;


    public static Parent launch(String stockName,String beginDate,String endDate) throws IOException {
        FXMLLoader loader = new FXMLLoader(SingleStockViewController.class.getResource("SingleStockViewn.fxml"));
        SingleStockViewController.stockname = stockName;
        SingleStockViewController.startDate = beginDate;
        SingleStockViewController.endDate = endDate;
        Pane pane = loader.load();

        return pane;
    }

    @FXML
    public void initialize() throws NotFoundException, IOException {
        stockViewService=BLFactory.getInstance().getStockViewService();
        this.attribute_TabelView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //默认显示前一个月到今天的数据
        //默认筛选条件为空
        conditions=new ArrayList<>();
        //获得所有可用列
        fields=stockViewService.getAllFields();
        date_Column.setMinWidth(80);

        Image warning = new Image(getClass().getResourceAsStream("tinywarning.png"));
        msg.setGraphic(new ImageView((warning)));
        msg.setOpacity(0);
        this.refresh();
    }

        /**
         * 重新布局表格的列
         * @param list 股票属性集合
         */
    private void renewColumns(List<String> list) {
        attribute_TabelView.getColumns().remove(1, attribute_TabelView.getColumns().size());
        date_Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        for (String field : list) {
            final String fieldCH = Attribute.getCHbyEN(field);
            TableColumn<StockAttribute, String> column = new TableColumn<>(fieldCH);
            column.setMinWidth(80);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAttribute(field)));
            attribute_TabelView.getColumns().add(column);
        }
    }


    public static void setStock(String name){
        SingleStockViewController.stockname = name;
    }

    public void confirm() throws NotFoundException, IOException {

        conditions=new ArrayList<ConditionSelect>();

         if (checkNull(openfrom_TF,opento_TF)){
             conditions.add(new ConditionSelect(R.field.open,openfrom_TF.getText(),opento_TF.getText()));
         }
         if(checkNull(closefrom_TF,closeto_TF)){
            conditions.add(new ConditionSelect(R.field.close,closefrom_TF.getText(),closeto_TF.getText()));
        }
        if (checkNull(highfrom_TF,highto_TF)){
            conditions.add(new ConditionSelect(R.field.high,highfrom_TF.getText(),highto_TF.getText()));
        }
        if (checkNull(lowfrom_TF,lowto_TF)){
            conditions.add(new ConditionSelect(R.field.low,lowfrom_TF.getText(),lowto_TF.getText()));
        }

        this.refresh();
    }

    private boolean checkNull(TextField from,TextField to){
        if ((!from.getText().equals(""))&&(!to.getText().equals(""))){
            return true;
        }
        else
            return false;
    }

    public void reset() throws NotFoundException, IOException {
        openfrom_TF.clear();
        opento_TF.clear();
        closefrom_TF.clear();
        closeto_TF.clear();
        highfrom_TF.clear();
        highto_TF.clear();
        lowfrom_TF.clear();
        lowto_TF.clear();

        conditions=new ArrayList<ConditionSelect>();
        this.refresh();
    }

//    private void clear(TextField textField){
//        textField.setText("");
//    }

    public void refresh(){
        try {
            stockViewService = BLFactory.getInstance().getStockViewService();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            setWarningMsg(e.getMessage());
            e.printStackTrace();
        }

        //如果日期或者数据域输入不合规范,则抛出异常
         List<StockAttribute> list = new ArrayList<>();
        try {
            list = stockViewService.getStock(stockname, startDate, endDate, R.field.all, conditions).getAttribute();
        }catch (BadInputException e){
            setWarningMsg(e.getMessage());
            e.printStackTrace();
        } catch (NotFoundException e) {
            setWarningMsg(e.getMessage());

            e.printStackTrace();
        }
        attribute_TabelView.setItems(FXCollections.observableArrayList(new ArrayList<>(list)));

        List<String> fields = stockViewService.getAllFields();
        this.renewColumns(fields);

    }

    public void setWarningMsg(String text) {
//        msg = new Label(text);

        msg.setTextFill(Color.RED);
        msg.setText(text);


        FadeTransition fadeTransition = new FadeTransition(javafx.util.Duration.millis(3000),msg);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setAutoReverse(false);
        fadeTransition.play();


    }


}
