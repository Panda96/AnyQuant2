package ui.stockviewui;

import bl.blfactory.BLFactory;
import blservice.stockmanageblservice.StockManageService;
import blservice.stockviewblservice.StockViewService;
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
import javafx.scene.layout.Pane;
import tool.enums.Attribute;
import tool.constant.LocalInfo;
import tool.constant.StockAttribute;
import tool.constant.UserInfo;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import tool.utility.TimeConvert;
import ui.UIController;
import vo.chart.common.StockVO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AllStockViewController extends UIController {

    public DatePicker date_DatePicker;
    public TableView<StockVO> stockVO_TableView;
    public TableColumn<StockVO, String> stockNum_Column;
    public TableColumn<StockVO, String> name_Column;

    public javafx.scene.layout.HBox HBox;
    public List<CheckBox> boxList = new ArrayList<CheckBox>();

    public Button search;
    static boolean isBenchMark;
    List<StockVO> list;
    StockViewService stockViewService;

    private StockManageService stockManage;

    public static Parent launch() throws IOException {
        FXMLLoader loader = new FXMLLoader(AllStockViewController.class.getResource("allStockTableView.fxml"));
        Pane pane = loader.load();

        return pane;
    }

    @FXML
    public void initialize() throws NotFoundException, BadInputException, IOException {
            stockViewService = BLFactory.getInstance().getStockViewService();
            stockManage = BLFactory.getInstance().getStockManageService();

        date_DatePicker.setValue(LocalDate.now());

        List<String> fields = stockViewService.getAllFields();
        addCheckBox(fields);

        stockVO_TableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    StockVO selected = stockVO_TableView.getSelectionModel().getSelectedItem();
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
        stockVO_TableView.getColumns().remove(2, stockVO_TableView.getColumns().size());
        stockNum_Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumber()));
        name_Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        for (String field : list) {
            final String fieldCH = Attribute.getCHbyEN(field);
            TableColumn<StockVO, String> column = new TableColumn<>(fieldCH);
            column.setMinWidth(65);
            column.setCellValueFactory(cellData -> {
                List<StockAttribute> tmplist = cellData.getValue().getAttribute();
                StockAttribute tmp = tmplist.get(0);
                return new SimpleStringProperty(tmp.getAttribute(field));
            });
            stockVO_TableView.getColumns().add(column);
        }

    }
    public void search() throws NotFoundException, BadInputException {
        this.refresh();
    }

    public static void setIsBenchmark(boolean isBenchmark){
        isBenchMark=isBenchmark;
    }

    private List<StockVO> getList() throws NotFoundException, BadInputException {
        Calendar calendar = TimeConvert.convertDate(date_DatePicker.getValue());
        List<StockVO> list = null;
        try {
            list = stockViewService.getAllStock("2015", "sh", TimeConvert.getDisplayDate(calendar));
            list.addAll(stockViewService.getAllStock("2015","sz",TimeConvert.getDisplayDate(calendar)));
            while(list.get(0).getAttribute().size()==0){
                calendar = TimeConvert.convertDate(date_DatePicker.getValue());
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                date_DatePicker.setValue(TimeConvert.convertCalendar(calendar));
                list = stockViewService.getAllStock("2015", "sh", TimeConvert.getDisplayDate(calendar));
                list.addAll(stockViewService.getAllStock("2015","sz",TimeConvert.getDisplayDate(calendar)));
            }
        } catch (BadInputException e) {
            e.printStackTrace();
        }

        return list;
    }

    private List<StockVO> getBenchmark() throws NotFoundException, BadInputException {
        Calendar calendar = TimeConvert.convertDate(date_DatePicker.getValue());

        List<StockVO> list = new ArrayList<>();
        try {

            list = stockViewService.getAllBenchmark(TimeConvert.getDisplayDate(calendar));
            while(list.get(0).getAttribute().size()==0){
                calendar = TimeConvert.convertDate(date_DatePicker.getValue());
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                date_DatePicker.setValue(TimeConvert.convertCalendar(calendar));
                list = stockViewService.getAllBenchmark(TimeConvert.getDisplayDate(calendar));
            }
        } catch (BadInputException e) {
            e.printStackTrace();
        }

        return list;
    }
    public void refresh() throws NotFoundException, BadInputException {
        //这里默认指标全选,以后要改成根据checkbox的选中状态来进行调整
        if(isBenchMark){
            list=this.getBenchmark();
        }else {
            list = this.getList();
        }

        stockVO_TableView.setItems(FXCollections.observableArrayList(new ArrayList<StockVO>(list)));
        List<String> fields = stockViewService.getAllFields();
        this.renewColumns(fields);
    }

    public void follow() throws NotFoundException, BadInputException {
        ObservableList<StockVO> selected = this.stockVO_TableView.getSelectionModel().getSelectedItems();
        if (selected.size() == 0)
            return;
        ArrayList<StockVO> list = this.transObervableList2List(selected);
            for (StockVO stock : list)
                stockManage.addUserCollection(new UserInfo(LocalInfo.getLocalName(), LocalInfo.getPassword()), stock.getNumber());
        this.refresh();
    }

    private ArrayList<StockVO> transObervableList2List(ObservableList<StockVO> observableList) {
        ArrayList<StockVO> ans = new ArrayList<StockVO>(observableList.size());
        for (StockVO stockVO : observableList) {
            ans.add(stockVO);
        }
        return ans;
    }
}
