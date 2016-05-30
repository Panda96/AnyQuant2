package ui.chartui.barchart;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Created by JiachenWang on 2016/4/13.
 */
public class BTooltipContent extends GridPane {
    private Label valueLabel = new Label();

    BTooltipContent() {
        Label info = new Label("value:");
        info.getStyleClass().add("candlestick-tooltip-label");
        setConstraints(info, 0, 0);
        setConstraints(valueLabel, 1, 0);
        getChildren().addAll(info, valueLabel);
    }

    public void update(double value) {
        valueLabel.setText(Double.toString(value));
    }
}

