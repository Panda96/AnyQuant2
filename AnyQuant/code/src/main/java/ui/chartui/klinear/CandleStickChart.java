package ui.chartui.klinear;

import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Seven on 16/3/21.
 * K线图基本组件
 */
public class CandleStickChart extends XYChart<String, Number> {

    /**
     * Construct a new CandleStickChart with the given axis
     *
     * @param xAxis The x axis to use
     * @param yAxis The y axis to use
     */
    public CandleStickChart(Axis<String> xAxis, Axis<Number> yAxis) {
        super(xAxis, yAxis);
        setAnimated(false);

        xAxis.setAnimated(false);
        yAxis.setAnimated(false);

    }

    /**
     * Construct a new CandleStickChart with the given axis and data.
     *
     * @param xAxis The x axis to use
     * @param yAxis The y axis to use
     * @param data  The data to use, this is the actual list used so any
     *              changes to it will be reflected in the chart
     */

    public CandleStickChart(Axis<String> xAxis, Axis<Number> yAxis,
                            ObservableList<Series<String, Number>> data) {
        this(xAxis, yAxis);
        setUserData(data);
    }

    /**
     * Called to update and layout the content for the plot
     */
    @Override
    protected void layoutPlotChildren() {
        // we have nothing to layout if no data is present
        if (getData() == null) {
            return;
        }
        // update candle positions
        for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
            Series<String, Number> series = getData().get(seriesIndex);
            Iterator<Data<String, Number>> iter = getDisplayedDataIterator(series);
            Path seriesPath = null;
            if (series.getNode() instanceof Path) {
                seriesPath = (Path) series.getNode();
                seriesPath.getElements().clear();
            }
            while (iter.hasNext()) {
                Data<String, Number> item = iter.next();
                double x = getXAxis().getDisplayPosition(
                        getCurrentDisplayedXValue(item));
                double y = getYAxis().getDisplayPosition(
                        getCurrentDisplayedYValue(item));
                Node itemNode = item.getNode();
                CandleStickExtraValues extra = (CandleStickExtraValues) item
                        .getExtraValue();
                if (itemNode instanceof Candle && extra != null) {
                    Candle candle = (Candle) itemNode;

                    double close = getYAxis().getDisplayPosition(
                            extra.getClose());
                    double high = getYAxis().getDisplayPosition(
                            extra.getHigh());
                    double low = getYAxis().getDisplayPosition(
                            extra.getLow());
                    // calculateRSI candle width
                    double candleWidth = -1;
                    if (getXAxis() instanceof CategoryAxis) {
                        CategoryAxis xa = (CategoryAxis) getXAxis();
                        //****//candleWidth = xa.getDisplayPosition(xa.getTickLength()) * 0.90; // use 90% width
                        // between ticks
                    }
                    // update candle
                    // System.out.format("x = %f y=%f%n",x,y);
                    candle.update(close - y, high - y, low - y, candleWidth);
                    candle.updateTooltip(item.getYValue().doubleValue(),
                            extra.getClose(), extra.getHigh(),
                            extra.getLow());

                    // position the candle
                    candle.setLayoutX(x);
                    candle.setLayoutY(y);
                }
                if (seriesPath != null) {
                    if (seriesPath.getElements().isEmpty()) {
                        seriesPath.getElements().add(
                                new MoveTo(x, getYAxis()
                                        .getDisplayPosition(
                                                extra.getAverage())));
                    } else {
                        seriesPath.getElements().add(
                                new LineTo(x, getYAxis()
                                        .getDisplayPosition(
                                                extra.getAverage())));
                    }
                }
            }
        }
    }

    @Override
    protected void dataItemChanged(Data<String, Number> item) {

    }

    @Override
    protected void dataItemAdded(Series<String, Number> series,
                                 int itemIndex, Data<String, Number> item) {
        //System.out.println("DatItemAdded");
        Node candle = createCandle(getData().indexOf(series), item,
                itemIndex);
        if (shouldAnimate()) {
            candle.setOpacity(0);
            getPlotChildren().add(candle);
            // fade in new candle
            FadeTransition ft = new FadeTransition(Duration.millis(500),
                    candle);
            ft.setToValue(1);
            ft.play();
        } else {
            getPlotChildren().add(candle);
        }
    }

    @Override
    protected void dataItemRemoved(Data<String, Number> item,
                                   Series<String, Number> series) {

        final Node candle = item.getNode();
        if (shouldAnimate()) {
            // fade out old candle
            FadeTransition ft = new FadeTransition(Duration.millis(500),
                    candle);
            ft.setToValue(0);
            ft.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    getPlotChildren().remove(candle);
                }
            });
            ft.play();
        } else {
            getPlotChildren().remove(candle);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void seriesAdded(Series<String, Number> series,
                               int seriesIndex) {
        // createChart
        // handle any data already in series

        for (int j = 0; j < series.getData().size(); j++) {
            Data item = series.getData().get(j);
            Node candle = createCandle(seriesIndex, item, j);
            if (shouldAnimate()) {
                candle.setOpacity(0);
                getPlotChildren().add(candle);
                // fade in new candle
                FadeTransition ft = new FadeTransition(
                        Duration.millis(500), candle);
                ft.setToValue(1);
                ft.play();
            } else {
                getPlotChildren().add(candle);
            }
        }
        // create series path
        Path seriesPath = new Path();
        seriesPath.getStyleClass().setAll("candlestick-average-line",
                "series" + seriesIndex);
        series.setNode(seriesPath);
        getPlotChildren().add(seriesPath);

        this.getStyleClass().add("KLinearChart");
    }

    @Override
    protected void seriesRemoved(Series<String, Number> series) {
        // remove all candle nodes
        for (XYChart.Data<String, Number> d : series.getData()) {
            final Node candle = d.getNode();
            if (shouldAnimate()) {
                // fade out old candle
                FadeTransition ft = new FadeTransition(
                        Duration.millis(500), candle);
                ft.setToValue(0);
                ft.setOnFinished(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        getPlotChildren().remove(candle);
                    }
                });
                ft.play();
            } else {
                getPlotChildren().remove(candle);
            }
        }
    }

    /**
     * Create a new Candle node to represent a single data item
     *
     * @param seriesIndex The index of the series the data item is in
     * @param item        The data item to create node for
     * @param itemIndex   The index of the data item in the series
     * @return New candle node to represent the give data item
     */
    @SuppressWarnings("rawtypes")
    private Node createCandle(int seriesIndex, final Data item,
                              int itemIndex) {
        Node candle = item.getNode();
        // check if candle has already been created
        if (candle instanceof Candle) {
            ((Candle) candle).setSeriesAndDataStyleClasses("series"
                    + seriesIndex, "data" + itemIndex);
        } else {
            candle = new Candle("series" + seriesIndex, "data" + itemIndex);
            item.setNode(candle);
        }
        return candle;
    }

    /**
     * This is called when the range has been invalidated and we need to
     * update it. If the axis are auto ranging then we compile a list of all
     * data that the given axis has to plot and call invalidateRange() on
     * the axis passing it that data.
     */
    @Override
    protected void updateAxisRange() {
        // called once :)
        // For candle stick chart we need to override this method as we need
        // to let the axis know that they need to be able
        // to cover the whole area occupied by the high to low range not
        // just its center data value
        final Axis<String> xa = getXAxis();
        final Axis<Number> ya = getYAxis();
        List<String> xData = null;
        List<Number> yData = null;
        if (xa.isAutoRanging()) {
            xData = new ArrayList<String>();
        }
        if (ya.isAutoRanging()) {
            yData = new ArrayList<Number>();
        }
        if (xData != null || yData != null) {
            for (Series<String, Number> series : getData()) {
                for (Data<String, Number> data : series.getData()) {
                    if (xData != null) {
                        xData.add(data.getXValue());
                    }
                    if (yData != null) {
                        CandleStickExtraValues extras = (CandleStickExtraValues) data
                                .getExtraValue();
                        if (extras != null) {
                            yData.add(extras.getHigh());
                            yData.add(extras.getLow());
                        } else {
                            yData.add(data.getYValue());
                        }
                    }
                }
            }
            if (xData != null) {
                xa.invalidateRange(xData);
            }
            if (yData != null) {
                ya.invalidateRange(yData);
            }
        }
    }

}
