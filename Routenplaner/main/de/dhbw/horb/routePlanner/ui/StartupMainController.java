package de.dhbw.horb.routePlanner.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

public class StartupMainController {

    public StartupMainController() {
    }

    @FXML
    private ProgressIndicator startupProgressIndicator;

    @FXML
    private ProgressBar startupProgressBar;

    @FXML
    private Label startupLabel;

    public void setProgress(double progress) {
	startupProgressBar.setProgress(progress);
    }

    public void setMessage(String msg) {
	startupLabel.setText(msg);
    }

    public ProgressBar getProgressBar() {
	return startupProgressBar;
    }

    public Label getLabel() {
	return startupLabel;
    }
}
