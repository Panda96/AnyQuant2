package ui.chartui.barchart;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import vo.analyse.industry.IndustryVolumeVO;
import vo.chart.barchart.MixSingleVolumeVO;
import vo.chart.barchart.SingleVolumeVO;
import vo.chart.barchart.VolumeChartVO;
import vo.chart.barchart.VolumeVO;

/**
 * Created by JiachenWang on 2016/4/1.
 */
interface BarChartStrategy {

    public StackPane creatChart(VolumeVO volumevo);

}

class SingleStrategy implements BarChartStrategy {

    ObservableList<BarChart.Data> barChartData;
    Label caption;

    public StackPane creatChart(VolumeVO volumevo) {

        //The data of Chart, will be fill by a thread
        barChartData = FXCollections.observableArrayList();

        StackPane pane = new StackPane();

        //Init Label
        caption = new Label("");
        caption.setVisible(false);
        caption.getStyleClass().addAll("chart-line-symbol", "chart-series-line");
        caption.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        caption.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

        SingleVolumeVO singlevo = (SingleVolumeVO) volumevo;

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        // create Chart  and set Title
        BarChart barChart = new BarChart(xAxis, yAxis, barChartData);
        pane.getChildren().add(barChart);

        //Add Label to StackPane
        pane.getChildren().add(caption);

        //set labels
        barChart.setTitle(singlevo.getTitle());
        xAxis.setLabel(singlevo.getxLabel());
        yAxis.setLabel(singlevo.getyLabel());

        // set value
        XYChart.Series series = new XYChart.Series();
        series.setName("成交量");
        for (VolumeChartVO vo : singlevo.getList()) {
            XYChart.Data data = new XYChart.Data(vo.getTime(), vo.getVolumn());
            boolean isRise = vo.isRise();
            data.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
                    if (newNode != null) {
                        if (isRise) {
                            newNode.setStyle("-fx-bar-fill: #ce2700;");
                        } else {
                            newNode.setStyle("-fx-bar-fill: #95ce1b;");
                        }
                        Tooltip tooltip = new Tooltip();
                        BTooltipContent bcontent = new BTooltipContent();
                        tooltip.setGraphic(bcontent);
                        Tooltip.install(newNode, tooltip);
                        bcontent.update((double)data.getYValue());
                    }
                }
            });

            series.getData().add(data);
        }
        barChart.getData().add(series);

        barChart.setCategoryGap(2);
        barChart.setBarGap(2);

        return pane;
    }
}

class IndustryStrategy implements BarChartStrategy {

    ObservableList<BarChart.Data> barChartData;
    Label caption;

    public StackPane creatChart(VolumeVO volumevo) {

        //The data of Chart, will be fill by a thread
        barChartData = FXCollections.observableArrayList();

        StackPane pane = new StackPane();

        //Init Label
        caption = new Label("");
        caption.setVisible(false);
        caption.getStyleClass().addAll("chart-line-symbol", "chart-series-line");
        caption.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        caption.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

        IndustryVolumeVO industryvo = (IndustryVolumeVO) volumevo;

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        // create Chart  and set Title
        BarChart barChart = new BarChart(xAxis, yAxis);
        pane.getChildren().add(barChart);

        //Add Label to StackPane
        pane.getChildren().add(caption);

        //set labels
        barChart.setTitle(industryvo.getTitle());
        xAxis.setLabel(industryvo.getxLabel());
        yAxis.setLabel(industryvo.getyLabel());

        // set value
        XYChart.Series series = new XYChart.Series();
        series.setName("成交量");
        for (VolumeChartVO vo : industryvo.getList()) {
            XYChart.Data data = new XYChart.Data(vo.getTime(), vo.getVolumn());
            data.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
                    if (newNode != null) {
                        newNode.setStyle("-fx-bar-fill: darkorange;");
                    }
                    Tooltip tooltip = new Tooltip();
                    BTooltipContent bcontent = new BTooltipContent();
                    tooltip.setGraphic(bcontent);
                    Tooltip.install(newNode, tooltip);
                    bcontent.update((double)data.getYValue());
                }
            });
            series.getData().add(data);
        }
        barChart.getData().add(series);

        barChart.setCategoryGap(0);
        barChart.setBarGap(0);

        return pane;
    }
}

class MixStrategy implements BarChartStrategy {

    ObservableList<BarChart.Data> barChartData;
    Label caption;

    public StackPane creatChart(VolumeVO volumevo) {

        //The data of Chart, will be fill by a thread
        barChartData = FXCollections.observableArrayList();

        StackPane pane = new StackPane();

        //Init Label
        caption = new Label("");
        caption.setVisible(false);
        caption.getStyleClass().addAll("chart-line-symbol", "chart-series-line");
        caption.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        caption.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

        MixSingleVolumeVO mixvo = (MixSingleVolumeVO) volumevo;

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        // create Chart  and set Title
        BarChart barChart = new BarChart(xAxis, yAxis);
        pane.getChildren().add(barChart);

        //Add Label to StackPane
        pane.getChildren().add(caption);

        //set labels
        barChart.setTitle(mixvo.getTitle());
        xAxis.setLabel(mixvo.getxLabel());
        yAxis.setLabel(mixvo.getyLabel());

        // set value
        XYChart.Series s1 = new XYChart.Series();
        XYChart.Series s2 = new XYChart.Series();
        s1.setName("成交量");
        s2.setName("成交金额");
//        s1.getNode().getStyleClass().add("-fx-stroke: #95ce1b;");
//        s1.nodeProperty().addListener(new ChangeListener<Node>() {
//            @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
//                if (newNode != null) {
////                    newNode.setStyle("-fx-stroke: #95ce1b;");
//                    newNode.setStyle("-fx-series-stroke: #95ce1b;");
//                }
//            }
//        });
        for (VolumeChartVO vo : mixvo.getList()) {
            final XYChart.Data data1 = new XYChart.Data(vo.getTime(), vo.getVolumn());
            boolean isRise = vo.isRise();
            data1.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
                    if (newNode != null) {
                        if (isRise) {
                            newNode.setStyle("-fx-bar-fill: #ce2700;");
                        } else {
                            newNode.setStyle("-fx-bar-fill: #95ce1b;");
                        }
                        Tooltip tooltip = new Tooltip();
                        BTooltipContent bcontent = new BTooltipContent();
                        tooltip.setGraphic(bcontent);
                        Tooltip.install(newNode, tooltip);
                        bcontent.update((double)data1.getYValue());
                    }
                }
            });
            s1.getData().add(data1);

            final XYChart.Data data2 = new XYChart.Data(vo.getTime(), vo.getSumPrice());
            data2.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
                    if (newNode != null) {
                        newNode.setStyle("-fx-bar-fill: darkorange;");
                        Tooltip tooltip = new Tooltip();
                        BTooltipContent bcontent = new BTooltipContent();
                        tooltip.setGraphic(bcontent);
                        Tooltip.install(newNode, tooltip);
                        bcontent.update((double)data2.getYValue());
                    }
                }
            });
            s2.getData().add(data2);
        }
        barChart.getData().add(s1);
        barChart.getData().add(s2);

        final double maxBarWidth = 50;
        final double minCategoryGap = 0;

        //width&gap
        double barWidth = 0;
        do {
            double catSpace = xAxis.getCategorySpacing();
            double avilableBarSpace = catSpace - (minCategoryGap + barChart.getBarGap());
            barWidth = Math.min(maxBarWidth, (avilableBarSpace / barChart.getData().size()) - barChart.getBarGap());
            avilableBarSpace = (barWidth + barChart.getBarGap()) * barChart.getData().size();
            barChart.setCategoryGap(catSpace - avilableBarSpace - barChart.getBarGap());
        } while (barWidth < maxBarWidth && barChart.getCategoryGap() > minCategoryGap);

        return pane;
    }
}