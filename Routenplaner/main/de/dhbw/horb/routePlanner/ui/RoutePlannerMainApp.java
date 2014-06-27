package de.dhbw.horb.routePlanner.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import de.dhbw.horb.routePlanner.Constants;

public class RoutePlannerMainApp extends Application {

	public Stage primaryStage;
    private BorderPane rootLayout;
    
    public RoutePlannerMainApp() {
    }
    
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Draw Line");

        initRootLayout();

        showMainWindow();
        
        primaryStage.setFullScreen(true);
	}
	
	/**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RoutePlannerMainApp.class.getResource(Constants.FXML_ROOT));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setTitle("Routenplaner");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showMainWindow() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RoutePlannerMainApp.class.getResource(Constants.FXML_MAIN));
            AnchorPane MainWindow = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(MainWindow);
            
            RoutePlannerMainController controller = loader.getController();
            controller.setRoutePlannerMainApp(this);
            
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
    
    public static void main(String[] args) {
    	launch(args);
    }

}
