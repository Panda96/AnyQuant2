package ui.chartui.piechart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import ui.UIController;
import vo.chart.piechart.PieChartVO;

import java.util.Map;
import java.util.Set;

/**
 * Created by JiachenWang on 2016/4/10.
 */
public class PieChartController extends UIController {

    public StackPane createPieChart(PieChartVO pievo) {
        ObservableList<PieChart.Data> pieChartData;
        Label caption;

        //The data of Chart, will be fill by a thread
        pieChartData = FXCollections.observableArrayList();

        StackPane pane = new StackPane();

        //Init Label
        caption = new Label("");
        caption.setVisible(false);
        caption.getStyleClass().addAll("chart-line-symbol", "chart-series-line");
        caption.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        caption.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

        //Init PieChart
        PieChart pieChart = new PieChart(pieChartData);
        pane.getChildren().add(pieChart);

        //Add Label to StackPane
        pane.getChildren().add(caption);
        Set<Map.Entry<String, Double>> vo_datas = pievo.originMap.entrySet();
        for (Map.Entry<String, Double> vo_data : vo_datas) {
            PieChart.Data data = new PieChart.Data(vo_data.getKey(), vo_data.getValue());
            pieChartData.add(data);
            //When mouse entered (on the slice of PieChart)
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            //Set Label ignores the mouse
                            caption.setMouseTransparent(true);
                            //I move Label near the mouse cursor, with a offset !
                            caption.setTranslateX(e.getX() + 50);
                            caption.setTranslateY(e.getY() + 20);
                            //I change text of Label
                            caption.setText(data.getPieValue() + "\n" + data.getName());
                            //Change the color of popup, to adapt it to slice
                            if (caption.getStyleClass().size() == 4) {
                                caption.getStyleClass().remove(3);
                            }
                            caption.getStyleClass().add(data.getNode().getStyleClass().get(2));
                            //I display Label
                            caption.setVisible(true);
                        }
                    });
            //Need to add a event when the mouse move hover the slice
            //If I don't the popup stay blocked on edges of the slice.
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    //Keep Label near the mouse
                    caption.setTranslateX(e.getX() + 50);
                    caption.setTranslateY(e.getY() + 20);
                }
            });

            //When mouse exited (the slice of PieChart)
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    //I Hide Label
                    caption.setVisible(false);
                }
            });
        }
        pieChart.setTitle(pievo.title);

        return pane;
    }

}
