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

public class RouteplannerStartup extends Application {

    @FXML
    private ProgressIndicator startupProgressIndicator;

    @FXML
    private ProgressBar startupProgressBar;

    private Stage primaryStage;
    private BorderPane splashLayout;
    private AnchorPane splashAnchor;

    @Override
    public void start(Stage primaryStage) {
	this.primaryStage = primaryStage;
	//this.primaryStage.setTitle("Routeplanner - StartupSplash");

	initSplashLayout();

	showSplash();
    }

    /**
     * Initializes the root layout.
     */
    public void initSplashLayout() {
	try {
	    // Load root layout from fxml file.
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(RouteplannerStartup.class.getResource("StartupRoot.fxml"));
	    splashLayout = (BorderPane) loader.load();

	    // Show the scene containing the root layout.
	    Scene scene = new Scene(splashLayout);
	    primaryStage.setScene(scene);
	    primaryStage.initStyle(StageStyle.TRANSPARENT);
	    scene.setFill(Color.TRANSPARENT);
	    primaryStage.show();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showSplash() {
	try {
	    // Load person overview.
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(RouteplannerStartup.class.getResource("StartupMain.fxml"));
	    splashAnchor = (AnchorPane) loader.load();

	    // Set person overview into the center of root layout.
	    splashLayout.setCenter(splashAnchor);

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
	return primaryStage;
    }

}
