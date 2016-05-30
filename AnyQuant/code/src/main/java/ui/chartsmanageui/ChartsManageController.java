package ui.chartsmanageui;

import bl.blfactory.BLFactory;
import blservice.graphblservice.BarChartBLService;
import blservice.graphblservice.PieChartBLService;
import blservice.statisticsblservice.SingleStatisticBLService;
import blservice.statisticsblservice.SingleViewService;
import blservice.stockviewblservice.StockViewService;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tool.constant.StockAttribute;
import tool.enums.Attribute;
import tool.enums.ChartsState;
import tool.enums.PeriodEnum;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import tool.utility.DateCount;
import ui.UIController;
import ui.chartui.barchart.AmountInADayBarchart;
import ui.chartui.barchart.BarChartController;
import ui.chartui.klinear.CandleStickChart;
import ui.chartui.klinear.KLinearController;
import ui.chartui.linearchart.LinearChartController;
import ui.chartui.piechart.PieChartController;
import ui.stockviewui.SingleStockViewController;
import vo.analyse.single.BasicSingleVO;
import vo.chart.barchart.MixSingleVolumeVO;
import vo.chart.barchart.SingleVolumeVO;
import vo.chart.common.StockVO;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

//import javafx.scene.l
//import javax.swing.border.LineBorder;


/**
 * Created by Lin on 2016/3/20.
 */
public class ChartsManageController extends UIController {
    @FXML
    Label candle,line,history,analysis;

    @FXML
    GridPane chartsPane;

    @FXML
    VBox stocksInfoPane;

    @FXML
    Label today, name;

    @FXML
    Label msg, start, end;

    @FXML
    Button search;

    @FXML
    DatePicker startDatePicker;

    @FXML
    DatePicker endDatePicker;

    @FXML
    GridPane headPane,dataPane,mainPane;

    @FXML
    GridPane analysisPane,linePane,piePane;

    @FXML
    Button day, week, three, five, month;

    @FXML
    Accordion stocks;

    @FXML
    HBox pickers;

    private StockViewService stockView;


    private String thisDate = DateCount.count(DateCount.dateToStr(new Date()), -1);

    //历史数据pane
    GridPane hisitoryPane;



    //默认股票
    private String CurrStockNum = "sh600151";
    private String CurrStockName = "航天机电";

    //时间选择器起止时间
    private String startDate;
    private String endDate;


    //用于记录所有股票的名字和编号
    private HashMap<String, String> namesAndNums;

    //Label管理数组
    private Label[][] labels;
    private int row;
    private int column;

    //时间段管理Bar
    private HBox h;

    //股票名称label管理List
    private ArrayList<Label> nameList;

    //导航Label管理
    private ArrayList<Label> navigations;

    //图表性质
    private PeriodEnum periodState = PeriodEnum.DAY;

    //图表类型
    private ChartsState chartsState;

    //单个界面controller
    private SingleStockViewController singleStockViewController;

    //判断折线图的调用方法
    private boolean isFirst = true;

    private StackPane[] stackPanes;

    private StackPane Line,Bar,Candle;

    private Label current;



    public static Parent launch() throws IOException {
        FXMLLoader loader = new FXMLLoader(ChartsManageController.class.getResource("chartsManage.fxml"));
        Pane pane = loader.load();

        return pane;
    }

    @FXML
    public void initialize() throws IOException, NotFoundException {

        stockView = BLFactory.getInstance().getStockViewService();

        int length = stockView.getAllFields().size();
        row = (length - 1) / 2 + 1;
        column = 4;

        //label数组用以持有各个label的引用
        //要注意数组的i，j与Gridpane的i，j是互换的
        labels = new Label[column][row];
        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[0].length; j++) {
                labels[i][j] = new Label();
                if (i % 2 == 0) {
//                    labels[i][j].setPrefSize(60, 40);
//                    labels[i][j].setPadding(new Insets(0, 0, 0, 0));
//                    labels[i][j].setFont(Font.font("微软雅黑", 13));
                } else {
//                    labels[i][j].setPrefSize(100, 30);
//                    labels[i][j].setPadding(new Insets(0, 0, 0, 0));
//                    labels[i][j].setFont(Font.font(12));
                }

                dataPane.add(labels[i][j], i, j);
            }
        }

        //初始化名称label list
        nameList = new ArrayList<>();

        //navigation Label 加入管理List
        navigations = new ArrayList<Label>();
        navigations.add(candle);
        navigations.add(line);
        navigations.add(history);
        navigations.add(analysis);

        //日期初始化
        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(LocalDate.now().minusMonths(2));
        setDates();

        namesAndNums = new HashMap<>();

        //初始化股票信息Pane
        stocks = new Accordion();
        stocks = initialAccordion(stocks);

        stocksInfoPane.getChildren().add(0, stocks);

        today.setText(thisDate);


//        //初始化K线图按钮
        h = new HBox();
        h.getChildren().add(0, day);
        h.getChildren().add(1, three);
        h.getChildren().add(2, five);
        h.getChildren().add(3, week);
        h.getChildren().add(4, month);

        StackPane s = new StackPane();
//        s.setStyle("-fx-background-color:lightblue");
        s.getChildren().add(h);
        chartsPane.add(s, 0, 0);

        //初始化历史数据和个股分析的Container
        hisitoryPane = new GridPane();
        analysisPane = new GridPane();


        Line = new StackPane();
        Bar = new StackPane();
        Candle = new StackPane();


        Image warning = new Image(getClass().getResourceAsStream("tinywarning.png"));
        msg.setGraphic(new ImageView((warning)));
        msg.setOpacity(0);
//        setWarningMsg("dfdf");


    }

    private Accordion initialAccordion(Accordion stocks) {
        HashMap<String, HashMap<String, String>> names = stockView.getNamesAndNumbers();

        ArrayList<TitledPane> titledPanes = new ArrayList<>();

        //遍历行业Map
        Iterator<Map.Entry<String, HashMap<String, String>>> iter = names.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = iter.next();
            String occupation = (String) entry.getKey();

            //新建单个行业的TitledPane
            TitledPane one = new TitledPane();
            one.setText(occupation);
            one.setFont(Font.font(15));
            HashMap<String, String> stocksInOne = (HashMap<String, String>) entry.getValue();

            //获得所有的股票名称和编号的map
            namesAndNums.putAll(stocksInOne);

            GridPane stockNames = new GridPane();
            stockNames.setPadding(new Insets(5));

            //遍历行业内股票Map
            Iterator<Map.Entry<String, String>> subiter = stocksInOne.entrySet().iterator();
            int i = 0;
            while (subiter.hasNext()) {
                Map.Entry subentry = subiter.next();

                //根据股票名称新建对应button
                String singleName = (String) subentry.getKey();
                Label label = new Label(singleName);
                label.setAlignment(Pos.CENTER);
                label.setPrefSize(300,30);
                stockNames.add(label,0,i);
                nameList.add(label);
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
//                        setNameList(label.getText());
                        String nameCH = label.getText();
                        CurrStockName = nameCH;
                        if(chartsState.getState()==3){
                            current.setText(CurrStockName);
                        }
                        CurrStockNum = namesAndNums.get(nameCH);
                        name.setText(nameCH);
                        refreshInfoPane();
                        refreshCharts();
                    }
                });
                label.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        label.setScaleX(1.5);
                        label.setScaleY(1.5);
                    }
                });

                label.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        label.setScaleX(1);
                        label.setScaleY(1);
                    }
                });

                i++;
            }

            one.setContent(stockNames);
            titledPanes.add(one);
        }


        for (TitledPane t : titledPanes) {
            stocks.getPanes().add(t);

        }
        return stocks;

    }

//    public void setNameList(String text){
//        String clicked = "-fx-background-color:lightblue";
//        for(Label l:nameList){
//            if(l.getText().equals(text)){
//                l.setStyle(clicked);
//            }else{
//                l.setStyle("");
//            }
//        }
//    }

    public void refreshInfoPane() {
        List<String> fields = stockView.getAllFields();

        String f = fields.get(0);
        for (int i = 1; i < fields.size(); i++) {
            f = f + "+" + fields.get(i);
        }

        today.setText(thisDate);

        StockVO vo = new StockVO();
        try {
            vo = stockView.getStock(CurrStockNum, thisDate, thisDate, f, new ArrayList<>());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (BadInputException e) {
            e.printStackTrace();
        }


        //对vo是否为空的预判断
        HashMap<String, String> att = new HashMap<>();

        List<StockAttribute> stockAttributes = vo.getAttribute();

        if (stockAttributes.size() == 1) {
            StockAttribute stockAttribute = stockAttributes.get(0);
            att = stockAttribute.getAttribute();
        }

        for (int i = 0; i < fields.size(); i++) {
            String fie = fields.get(i);

            String data = "";
            if (att.isEmpty()) {
                data = "- -";
            } else {
                data = att.get(fie);
                if (data == null) {
                    data = "- -";
                }
            }

            int temp = i / 2;
            if (i % 2 == 0) {
                labels[0][temp].setText(Attribute.getCHbyEN(fie));
                labels[1][temp].setText(data);
            } else {
                labels[2][temp].setText(Attribute.getCHbyEN(fie));
                labels[3][temp].setText(data);
            }

        }


    }

    public void rewriteInfoPane(){
//        Label[][] labels = new Label[2][6];
        today.setText("");
        SingleViewService singleViewService = null;
        try {
            singleViewService = BLFactory.getInstance().getSingleViewService();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        String begin = DateCount.count(endDate,-15);
        BasicSingleVO basicSingleVO = null;
        try {
            basicSingleVO = singleViewService.getBasicSingleInfo(CurrStockName,CurrStockNum,PeriodEnum.DAY,begin,endDate);
        } catch (BadInputException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }


        labels[0][0].setText("涨跌幅");
        labels[2][0].setText("振荡幅度");
        labels[0][1].setText("均换手率");
//        labels[0][1].setPrefSize(150,40);
        labels[2][1].setText("均市盈率");
        labels[0][2].setText("均市净率");
        labels[2][2].setText("心理指数");

        System.out.println(basicSingleVO==null);

        System.out.println(basicSingleVO.getRiseAndFall());
        labels[1][0].setText(formatDouble(basicSingleVO.getRiseAndFall()));
        labels[3][0].setText(formatDouble(basicSingleVO.getVariableRange()));
        labels[1][1].setText(formatDouble(basicSingleVO.getAvgTurnover()));
        labels[3][1].setText(formatDouble(basicSingleVO.getAvgPe()));
        labels[1][2].setText(formatDouble(basicSingleVO.getAvgPb()));
        labels[3][2].setText(formatDouble(basicSingleVO.getPsychologicalValue()));

        for(int i = 3;i<5;i++){
            for(int j = 0;j<4;j++) {
                labels[j][i].setText("");
            }
        }





    }

    public String formatDouble(Double d){
        BigDecimal b   =   new   BigDecimal(d);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }


    private void setChartsShow(boolean b){
        chartsPane.setVisible(b);

        hisitoryPane.setVisible(b&&false);

        analysisPane.setVisible(false);
    }


    public void showCandle() {
        chartsState = ChartsState.CANDLE;

        refreshInfoPane();
        setChartsShow(true);
        recoverPickers();

        Line.setVisible(false);
        Candle.setVisible(false);
        Candle = new StackPane();


        KLinearController kLinearController = new KLinearController();
        CandleStickChart candleChart = null;
        try {

            candleChart = kLinearController.createKLinearChart(CurrStockNum, periodState, startDate, endDate);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (BadInputException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Candle.getChildren().add(candleChart);

        chartsPane.add(Candle, 0, 1);


        showBar();

    }




    public void reducePickers(){
        Label tempDate = new Label("日期");
        tempDate.setPadding(new Insets(0,40,0,0));
        pickers.setVisible(false);
        pickers = new HBox();
        pickers.setAlignment(Pos.CENTER_LEFT);
        pickers.getChildren().add(0,tempDate);
        pickers.getChildren().add(1,endDatePicker);
        pickers.getChildren().add(2,search);
        pickers.getChildren().add(3,msg);
        msg.setPadding(new Insets(0,0,0,30));
        headPane.add(pickers,1,0);

        h.setVisible(false);

    }


    public void recoverPickers(){
        pickers.setVisible(false);
        pickers = new HBox();
        pickers.setAlignment(Pos.CENTER_LEFT);
        pickers.getChildren().add(0,start);
        pickers.getChildren().add(1,startDatePicker);
        end.setPadding(new Insets(0,10,0,30));
        pickers.getChildren().add(2,end);
        pickers.getChildren().add(3,endDatePicker);
        pickers.getChildren().add(4,search);
        msg.setPadding(new Insets(0,0,0,30));
        pickers.getChildren().add(5,msg);
        headPane.add(pickers,1,0);

        h.setVisible(true);
    }



    public void showLine() {
        chartsState = ChartsState.LINE;

        refreshInfoPane();
        setChartsShow(true);
        reducePickers();

        Candle.setVisible(false);
        Line.setVisible(false);
        Line = new StackPane();
//        Line.setPrefSize(885, 410);


        //获得折线图
        LinearChartController linearChartController = new LinearChartController();
        LineChart lineChart = null;
        try {
            if(isFirst){
                lineChart = linearChartController.getLineChart(CurrStockNum);
            }else{
                lineChart = linearChartController.getLineChart(CurrStockNum,endDate);
            }

            Line.getChildren().add(lineChart);
        } catch (NotFoundException e) {
            e.printStackTrace();
            setWarningMsg(e.getMessage());
            Line = new StackPane();
        } catch (BadInputException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        chartsPane.add(Line, 0, 1);

        showBarInaDay();

    }

    public void showBar() {

        Bar.setVisible(false);
        Bar = new StackPane();


        BarChartController barChartController = new BarChartController();
        BarChart barChart = null;
        try {
            BarChartBLService barChartBLService = BLFactory.getInstance().getBarChartBLService();

            SingleVolumeVO singlevo = barChartBLService.getSingleVolumeVO(CurrStockName, CurrStockNum, periodState, startDate, endDate);

            Bar = barChartController.createBarChart(singlevo);


            chartsPane.add(Bar, 0, 2);
        } catch (BadInputException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBarInaDay(){
        Bar.setVisible(false);
        Bar = new StackPane();

        AmountInADayBarchart amountInADayBarchart = new AmountInADayBarchart();

        StackPane barChart = null;
        try {

            if(isFirst){
                barChart = amountInADayBarchart.getAmountInADayBarchart(CurrStockNum);
            }else{
                barChart = amountInADayBarchart.getAmountInADayBarchart(CurrStockNum,endDate);
            }

        } catch (NotFoundException e) {
            e.printStackTrace();
            setWarningMsg(e.getMessage());
        }

        if(barChart!=null){
            Bar.getChildren().add(barChart);
        }
        chartsPane.add(Bar,0,2);

    }


    public void showHistory() {
        chartsState = ChartsState.HISTORY;

        refreshInfoPane();
        setChartsShow(false);
        recoverPickers();

        singleStockViewController = new SingleStockViewController();

        Parent pane = new StackPane();

        try {
            pane = singleStockViewController.launch(CurrStockNum,startDate,endDate);
        } catch (IOException e) {
            e.printStackTrace();
        }

        hisitoryPane = new GridPane();
        hisitoryPane.add(pane,0,0);
        hisitoryPane.setPadding(new Insets(5,5,5,5));
        mainPane.add(hisitoryPane,1,0);


    }

    public void resetPickers(){
        pickers.setVisible(false);
        pickers = new HBox();
        pickers.setAlignment(Pos.CENTER_LEFT);

        current = new Label(CurrStockName);
        current.setPadding(new Insets(0,0,0,380));
        current.setGraphicTextGap(10);
        pickers.getChildren().add(0,current);

        Label vs = new Label("vs");
        vs.setPadding(new Insets(0,20,0,20));
        pickers.getChildren().add(1,vs);

        Label result = new Label("相关系数：");
        result.setPrefSize(300,25);
        result.setPadding(new Insets(0,0,0,40));

        Label bargain = new Label(" ");
        bargain.setPadding(new Insets(0,10,0,10));


        ChoiceBox<String> cb = new ChoiceBox<String>();
        int i = 0;
        for(Label label:nameList){
            String temp = label.getText();
            if(temp.equals(CurrStockName)){

            }else{
                cb.getItems().add(i,temp);
                i++;
            }

        }
        cb.getSelectionModel().selectFirst();

        Button button = new Button("确认");
        button.setFont(Font.font(14));

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String choice = namesAndNums.get(cb.getValue());
                try {
                    SingleStatisticBLService singleStatisticBLService = BLFactory.getInstance().getSingleStatisticBLService();
                    String relation = singleStatisticBLService.cal90CC(choice,CurrStockNum);
                    result.setText("相关系数："+relation);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                } catch (BadInputException e) {
                    e.printStackTrace();
                }

            }
        });

        pickers.getChildren().add(2,cb);
        pickers.getChildren().add(3,bargain);
        pickers.getChildren().add(4,button);
        pickers.getChildren().add(5,result);
        headPane.add(pickers,1,0);

    }




    public void showAnalysis(){
        chartsState = ChartsState.ANALYSIS;

        rewriteInfoPane();

        resetPickers();
        chartsPane.setVisible(false);
        hisitoryPane.setVisible(false);


        analysisPane.setVisible(false);
        analysisPane = new GridPane();
        initialAnalysisPane();


        BarChartBLService barChartBLService = null;
        SingleViewService singleViewService = null;

        try {
            barChartBLService = BLFactory.getInstance().getBarChartBLService();
            singleViewService = BLFactory.getInstance().getSingleViewService();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }


        String begDate = DateCount.count(endDate,-30);

        //新建混合柱状图
        BarChartController barChartController = new BarChartController();
        StackPane barChart = null;


        MixSingleVolumeVO singlevo = null;
        try {
            singlevo = barChartBLService.getMixSingleVolumeVO(CurrStockName, CurrStockNum, periodState, begDate, endDate);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (BadInputException e) {
            e.printStackTrace();
        }
        barChart = barChartController.createBarChart(singlevo);
        stackPanes[2].getChildren().add(barChart);


        refreshPiePane("volume");

        refreshLinearPane("MACD");

        Label predict = new Label();
        predict.setWrapText(true);

        begDate = DateCount.count(endDate,-15);

        String conclusion = "";
        try {
            conclusion = singleViewService.getBasicSingleInfo(CurrStockName,CurrStockNum,PeriodEnum.DAY,begDate,endDate).getConclusion();
        } catch (BadInputException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        conclusion = "      "+conclusion;
        predict.setText(conclusion);
        predict.setPrefSize(200,325);
        predict.setAlignment(Pos.CENTER_LEFT);

        stackPanes[3].getChildren().add(predict);
        stackPanes[3].setAlignment(Pos.CENTER);


    }



    public void refreshLinearPane(String type){
        stackPanes[0].setVisible(false);

        LinearChartController linearcontroller = new LinearChartController();

        String begDate = DateCount.count(endDate,-150);
        LineChart lineChart = null;

        try {

            if(type.equals("MACD")){
                lineChart = linearcontroller.getMACDLineChart(CurrStockNum,begDate,endDate);
            }else if(type.equals("EMA")){
                lineChart = linearcontroller.getEMALineChart(CurrStockNum,begDate,endDate);
            }else if(type.equals("RSI")){
                lineChart = linearcontroller.getRSILineChart(CurrStockNum,begDate,endDate);
            }

        } catch (BadInputException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stackPanes[0] = new StackPane();
        stackPanes[0].setPrefSize(510,300);
        stackPanes[0].getChildren().add(lineChart);

        linePane.add(stackPanes[0],0,1);

    }

    public void refreshPiePane(String id){
        stackPanes[1].setVisible(false);

        //饼图
        PieChartBLService pieChartBLService = null;
        try {
            pieChartBLService = BLFactory.getInstance().getPieChartBLService();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        PieChartController pieChartController = new PieChartController();
        stackPanes[1] = new StackPane();
        stackPanes[1].setPrefSize(350,300);


        String tempDate = DateCount.dateToStr(new Date());
        tempDate = DateCount.count(tempDate,-2);
        String beginDate = DateCount.count(tempDate,-17);


        try {
            if(id.equals("amount")){
                stackPanes[1] = pieChartController.createPieChart(pieChartBLService.getPieAmountVO(CurrStockName,CurrStockNum,beginDate,tempDate));
            }else if(id.equals("volume")){
                stackPanes[1] = pieChartController.createPieChart(pieChartBLService.getPieVolumnVO(CurrStockName,CurrStockNum,beginDate,tempDate));
            }


        } catch (NotFoundException e) {
            e.printStackTrace();
        }


        piePane.add(stackPanes[1],0,1);


    }

    public void initialAnalysisPane(){
        linePane = new GridPane();
        linePane.setPrefSize(550,325);
        stackPanes = new StackPane[4];
        stackPanes[0] = new StackPane();
        stackPanes[0].setPrefSize(550,300);

        Button[] linebuttons = new Button[3];
        linebuttons[0] = new Button("MACD");
        linebuttons[1] = new Button("RSI");
        linebuttons[2] = new Button("EMA");

        HBox lineBox = new HBox();
        lineBox.setPrefSize(550,25);
        for(int i = 0;i<linebuttons.length;i++){
            linebuttons[i].setPrefSize(120,25);
            linebuttons[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    String type = ((Button)event.getSource()).getText();
//                    System.out.println(type);
                    refreshLinearPane(type);
                    ((Button)event.getSource()).requestFocus();
                }
            });
            lineBox.getChildren().add(i,linebuttons[i]);
        }

        linePane.add(lineBox,0,0);
        linePane.add(stackPanes[0],0,1);


        piePane = new GridPane();
        piePane.setPrefSize(335,325);

        Button[] pieButtons = new Button[2];
        pieButtons[0] = new Button("交易量");
        pieButtons[0].setId("volume");
        pieButtons[1] = new Button("交易金额");
        pieButtons[1].setId("amount");

        HBox pieBox = new HBox();
        pieBox.setPrefSize(550,25);
        for(int i = 0;i<pieButtons.length;i++){
            pieButtons[i].setPrefSize(120,25);
            pieButtons[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    String id = ((Button)event.getSource()).getId();
//                    System.out.println(id);
                    refreshPiePane(id);
                }
            });
            pieBox.getChildren().add(i,pieButtons[i]);
        }

        stackPanes[1] = new StackPane();
        stackPanes[1].setPrefSize(335,300);
        piePane.add(pieBox,0,0);
        piePane.add(stackPanes[1],0,1);



        stackPanes[2] = new StackPane();
        stackPanes[2].setPrefSize(550,325);

        stackPanes[3] = new StackPane();
        stackPanes[3].setPrefSize(335,325);

        analysisPane = new GridPane();
        analysisPane.add(linePane,0,0);
        analysisPane.add(stackPanes[2],0,1);
        analysisPane.add(piePane,1,0);
        analysisPane.add(stackPanes[3],1,1);


        mainPane.add(analysisPane,1,0);
    }


    public void setDates() {
        startDate = startDatePicker.getValue().toString();
        endDate = endDatePicker.getValue().toString();

    }

    public void refreshCharts() {
        int i = chartsState.getState();
        switch (i) {
            case 0:
                showCandle();
                setNavigations(candle.getId(),navigations);
                break;
            case 1:
                showLine();
                setNavigations(line.getId(),navigations);
                break;
            case 2:
                showHistory();
                setNavigations(history.getId(),navigations);
                break;
            case 3:
                showAnalysis();
                setNavigations(analysis.getId(),navigations);
                break;
            default:break;
        }
    }

    public void search() {
        int i = chartsState.getState();
        //判断是否是折线图
        if(i==1){
            isFirst = false;
        }else{
            isFirst = true;
        }
        setDates();
        refreshCharts();
    }

    public void setWarningMsg(String text) {
//        msg = new Label(text);

        msg.setText(text);
        msg.setTextFill(Color.RED);

        FadeTransition fadeTransition = new FadeTransition(javafx.util.Duration.millis(3000),msg);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setAutoReverse(false);
        fadeTransition.play();


    }



    public void setPeriodState(Event event) {
        String period = ((Button) event.getSource()).getText();
        periodState = PeriodEnum.getPeriodEnum(period);
        int n = periodState.getNum();

        startDatePicker.setValue(LocalDate.now().minusMonths(n));
        endDatePicker.setValue(LocalDate.now());
        setDates();

        showCandle();
    }


    public void setStock(String num) {
        CurrStockNum = num;

        Iterator<Map.Entry<String, String>> iter = namesAndNums.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry entry = iter.next();
            if (entry.getValue().equals(num)) {
                CurrStockName = (String) entry.getKey();
            }
        }

        name.setText(CurrStockName);
        showCandle();
        refreshInfoPane();

    }

    public void setNavigations(String id,ArrayList<Label> labels){
        String common = "";
        String clicked = "";
        for(Label label:labels){
            if(label.getId().equals(id)){
                label.setStyle(common);
            }else{
                label.setStyle(clicked);
            }
        }
    }


}
