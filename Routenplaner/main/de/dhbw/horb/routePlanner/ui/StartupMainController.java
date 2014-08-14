package de.dhbw.horb.routePlanner.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

/**
 * Controller-Klasse für das Splash-Window. Stellt Funktionen für das Splash zur
 * Verfügung
 *
 * @author robin
 *
 */
public class StartupMainController {

	public StartupMainController() {

	}

	@FXML
	private ProgressIndicator startupProgressIndicator;
	@FXML
	private ProgressBar startupProgressBar;
	@FXML
	private Label startupLabel;

	/**
	 * Setzt den Wert der ProgressBar
	 * 
	 * @param progress
	 *            Wert der ProgressBar als double
	 */
	public void setProgress(double progress) {
		startupProgressBar.setProgress(progress);
	}

	/**
	 * Setzt den Label-Text
	 * 
	 * @param msg
	 *            Label-Text als String
	 */
	public void setMessage(String msg) {
		startupLabel.setText(msg);
	}

	/**
	 * Gibt ProgressBar zurück
	 * 
	 * @return Referenz auf ProgressBar
	 */
	public ProgressBar getProgressBar() {
		return startupProgressBar;
	}

	/**
	 * Gibt Label zurück
	 * 
	 * @return Referenz auf Label
	 */
	public Label getLabel() {
		return startupLabel;
	}
}
