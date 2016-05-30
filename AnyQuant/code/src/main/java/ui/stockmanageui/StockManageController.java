package ui.stockmanageui;

import bl.blfactory.BLFactory;
import blservice.stockmanageblservice.StockManageService;
import blservice.stockviewblservice.StockViewService;
import tool.enums.Attribute;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import tool.constant.*;
import tool.exception.NotFoundException;
import tool.utility.TimeConvert;
import ui.UIController;
import ui.dialog.WarningDialogController;
import vo.chart.common.StockVO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by JiachenWang on 16/3/4.
 */
public class StockManageController extends UIController {
    public Label stockNum_Label;
    public DatePicker datePicker;

    public HBox HBox;
    public List<CheckBox> boxList = new ArrayList<CheckBox>();

    public TableView<StockVO> stock_TableView;

    public TableColumn<StockVO, String> code_Column;
    public TableColumn<StockVO, String> name_Column;


    private StockManageService stockManage;
    private StockViewService stockViewService;

    public static Parent launch() throws IOException {
        FXMLLoader loader = new FXMLLoader(StockManageController.class.getResource("stockManagement.fxml"));
        Pane pane = loader.load();

        return pane;
    }

    @FXML
    public void initialize() throws NotFoundException, IOException {

            stockManage = BLFactory.getInstance().getStockManageService();
            stockViewService = BLFactory.getInstance().getStockViewService();

        this.stock_TableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        datePicker.setValue(LocalDate.now());

        List<String> fields = stockViewService.getAllFields();
        addCheckBox(fields);

        stock_TableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    StockVO selected = stock_TableView.getSelectionModel().getSelectedItem();
//                    mainUI.gotoSingleStockView(selected.getNumber());
                    mainUI.gotoChartsManage(selected.getNumber());
                }

            }
        });

        this.refresh();
    }

    /**
     * 初始化checkbox
     * @param list 股票属性
     */
    private void addCheckBox(List<String> list) {
        for (String field : list) {
            field = Attribute.getCHbyEN(field);
            CheckBox box = new CheckBox(field);
            box.setSelected(true);
            box.selectedProperty().addListener
                    (new ChangeListener<Boolean>() {
                        public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                            List<String> fields = new ArrayList<String>();
                            for (CheckBox box : boxList) {
                                if (box.isSelected())
                                    fields.add(Attribute.getENbyCH(box.getText()));
                            }
                            renewColumns(fields);
                        }
                    });
            boxList.add(box);
            HBox.getChildren().add(box);
        }

    }

    /**
     * 重新布局表格的列
     * @param list 股票属性集合
     */
    private void renewColumns(List<String> list) {
        stock_TableView.getColumns().remove(2, stock_TableView.getColumns().size());
        code_Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumber()));
        name_Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        for (String field : list) {
            final String fieldCH = Attribute.getCHbyEN(field);
            TableColumn<StockVO, String> column = new TableColumn<>(fieldCH);
            column.setMinWidth(50);
            column.setCellValueFactory(cellData -> {
                List<StockAttribute> tmplist = cellData.getValue().getAttribute();
                StockAttribute tmp = tmplist.get(0);
                return new SimpleStringProperty(tmp.getAttribute(field));
            });
            stock_TableView.getColumns().add(column);
        }

    }

    /**
     * 日期搜索监听
     * @throws NotFoundException
     */
    public void search() throws NotFoundException {
        this.refresh();
    }

    /**
     * 获得收藏股数据（自动检索至存在数据最近的一天）
     * @return 收藏股信息集合
     * @throws NotFoundException
     */
    private List<StockVO> getList() throws NotFoundException {
        Calendar calendar = TimeConvert.convertDate(datePicker.getValue());
        List<StockVO> list = stockManage.getUserStock(new UserInfo(LocalInfo.getLocalName(), LocalInfo.getPassword()), TimeConvert.getDisplayDate(calendar));
        //make sure  size!=0
        if (list.size() != 0) {
            while (list.get(0).getAttribute().size() == 0) {
                calendar = TimeConvert.convertDate(datePicker.getValue());
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                datePicker.setValue(TimeConvert.convertCalendar(calendar));
                list = stockManage.getUserStock(new UserInfo(LocalInfo.getLocalName(), LocalInfo.getPassword()), TimeConvert.getDisplayDate(calendar));
            }
        }
        return list;
    }

    /**
     * 刷新界面
     * @throws NotFoundException
     */
    public void refresh() throws NotFoundException {
        List<StockVO> list = this.getList();
        stock_TableView.setItems(FXCollections.observableArrayList(new ArrayList<StockVO>(list)));

        List<String> fields = stockViewService.getAllFields();
        this.renewColumns(fields);

        stockNum_Label.setText(list.size() + "");
    }

    /**
     * 取消关注监听
     * @throws NotFoundException
     */
    public void cancelFollow() throws NotFoundException {
        ResultMsg cancelOrNot = new ResultMsg(false, "cancelOrNot");
        ObservableList<StockVO> selected = this.stock_TableView.getSelectionModel().getSelectedItems();
        if (selected.size() == 0)
            return;
        ArrayList<StockVO> list = this.transObervableList2List(selected);
        WarningDialogController dialog = null;
        try {
            dialog = WarningDialogController.newDialog("确定要取消关注吗", cancelOrNot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.stage.showAndWait();
        if (cancelOrNot.isPass()) {
            for (StockVO stock : list)
                stockManage.deleteUserCollection(new UserInfo(LocalInfo.getLocalName(), LocalInfo.getPassword()), stock.getNumber());
        }
        this.refresh();
    }

    /**
     * javafx ArrayList 和 ObservableList 转换
     * @param observableList
     * @return
     */
    private ArrayList<StockVO> transObervableList2List(ObservableList<StockVO> observableList) {
        ArrayList<StockVO> ans = new ArrayList<StockVO>(observableList.size());
        for (StockVO stockVO : observableList) {
            ans.add(stockVO);
        }
        return ans;
    }

}