package de.dhbw.horb.routePlanner.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import de.dhbw.horb.routePlanner.Constants;

/**
 * Klasse für das Splash-Window beim Programmstart
 * 
 * @author robin
 * 
 */
public class RouteplannerStartup extends Application {

    private Stage primaryStage;
    private BorderPane splashLayout;
    private AnchorPane splashAnchor;

    @FXML
    private ProgressIndicator startupProgressIndicator;
    @FXML
    private ProgressBar startupProgressBar;

    @Override
    public void start(Stage primaryStage) {
	this.primaryStage = primaryStage;

	initSplashLayout();
	showSplash();
    }

    /**
     * Initialisiert das Root-Layout des Splashs
     */
    public void initSplashLayout() {
	try {
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(RouteplannerStartup.class.getResource(Constants.FXML_ROOT_STARTUP));
	    this.splashLayout = (BorderPane) loader.load();

	    Scene scene = new Scene(this.splashLayout);
	    this.primaryStage.setScene(scene);
	    this.primaryStage.initStyle(StageStyle.TRANSPARENT);
	    scene.setFill(Color.TRANSPARENT);
	    this.primaryStage.show();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Läd das Splash in dessen Root-Layout und zeigt es an
     */
    public void showSplash() {
	try {
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(RouteplannerStartup.class.getResource(Constants.FXML_MAIN_STARTUP));
	    this.splashAnchor = (AnchorPane) loader.load();
	    this.splashLayout.setCenter(this.splashAnchor);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Gibt die primaryStage zurück
     * 
     * @return Stage primaryStage
     */
    public Stage getPrimaryStage() {
	return this.primaryStage;
    }
}
