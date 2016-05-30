package ui.industryanalysisui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import tool.enums.Industry;
import tool.enums.IndustryPeriodEnum;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import ui.UIController;
import ui.statisticsui.IndustryBarchart;
import ui.statisticsui.IndustryLinearCharts;
import ui.statisticsui.IndustryTable;
import vo.chart.table.SingleLineVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lin on 2016/4/6.
 */
public class IndustryAnalysisController extends UIController {
    @FXML
    GridPane industryPane,chartsPane;

    @FXML
    Label first,second,third,fourth;

    @FXML
    Button single,compare;

    @FXML
    Label all,coal,wine,bank,source;

    private ArrayList<Label> seasons;
    private ArrayList<Label> navigations;
    private IndustryPeriodEnum periodEnum = IndustryPeriodEnum.FIRST;
    private boolean isTable = true;
    private boolean isSingle = true;
    private TableView industryView;
    private String CurrIndustry = "银行业";

    private StackPane table = new StackPane();
    private StackPane linerChartPane = new StackPane();
    private StackPane barChartPane = new StackPane();


    public static Parent launch() throws IOException {
        FXMLLoader loader = new FXMLLoader(IndustryAnalysisController.class.getResource("industryanalysis.fxml"));
        Pane pane = loader.load();

        return pane;
    }

    @FXML
    public void initialize(){
        industryView = new TableView();


        seasons = new ArrayList<>();
        seasons.add(first);
        seasons.add(second);
        seasons.add(third);
        seasons.add(fourth);

        navigations = new ArrayList<>();
        navigations.add(all);
        navigations.add(coal);
        navigations.add(wine);
        navigations.add(bank);
        navigations.add(source);

//        setLabels();


        showTable();
        setNavigations(all.getId(),navigations);
        setNavigations(first.getId(),seasons);

    }

    public void showTable(){
        setChartsShow(false);
        table.setVisible(false);
        table = new StackPane();
        try {
            IndustryTable industryTable = new IndustryTable();
//            System.out.println(periodEnum.toString());
            industryView = industryTable.getIndustryTable(periodEnum);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            //TODO 加提示语句

        }

        table.getChildren().add(industryView);
        industryPane.add(table,1,0);

        setNavigations(all.getId(),navigations);

    }

    public void showCharts(){
        setChartsShow(true);
        showLinerCharts();
        showBarChart();

    }

    public void showBarChart(){
        barChartPane.setVisible(false);
        barChartPane = new StackPane();
        BarChart barChart = null;

        try {
            IndustryBarchart industryBarchart = new IndustryBarchart();
            barChartPane = industryBarchart.getIndustryBarChart(CurrIndustry,periodEnum);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chartsPane.add(barChartPane,0,2);

    }

    public void showLinerCharts(){
        linerChartPane.setVisible(false);
        linerChartPane = new StackPane();
        LineChart lineChart = null;
        IndustryLinearCharts industryLinearCharts = new IndustryLinearCharts();

        try {
            if(isSingle){
                lineChart = industryLinearCharts.getIndustryLineChart(CurrIndustry,periodEnum);

            }else{
                lineChart = industryLinearCharts.getIndustryBenchmarkCompareLineChart(CurrIndustry,periodEnum);
            }
        } catch (BadInputException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        linerChartPane.getChildren().add(lineChart);
        chartsPane.add(linerChartPane,0,1);
    }

    public void setChartsShow(boolean b){
        chartsPane.setVisible(b);
        table.setVisible(!b);
        isTable = !b;
    }



    public void showFirst(){
        periodEnum = IndustryPeriodEnum.FIRST;
        setNavigations(first.getId(),seasons);
        refresh();
    }

    public void showSecond(){
        periodEnum = IndustryPeriodEnum.SECOND;
        setNavigations(second.getId(),seasons);
        refresh();
    }

    public void showThird(){
        periodEnum = IndustryPeriodEnum.THIRD;
        setNavigations(third.getId(),seasons);
        refresh();
    }

    public void showFourth(){
        periodEnum = IndustryPeriodEnum.FOURTH;
        setNavigations(fourth.getId(),seasons);
        refresh();
    }

    public void refresh(){

        if(isTable){
            showTable();
        }else{
            showCharts();
        }

    }

    public void setLinerChart(MouseEvent e){
        String temp = ((Button)e.getSource()).getId();
        if(temp.equals("single")){
            isSingle = true;
        }else{
            isSingle = false;
        }
        showLinerCharts();
    }

    public void setIndustry(MouseEvent e){
        String id = ((Label)e.getSource()).getId();
        String industry = Industry.getName(id);
        CurrIndustry = industry;
        setNavigations(id,navigations);
        showCharts();

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
