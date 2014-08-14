package de.dhbw.horb.routePlanner.ui;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.data.JDomGraphDataCreator;
import de.dhbw.horb.routePlanner.data.OverpassDownloader;
import de.dhbw.horb.routePlanner.data.SettingsManager;
import de.dhbw.horb.routePlanner.data.XMLFileManager;

public class RoutePlannerMainApp extends Application {

    public Stage primaryStage;
    private BorderPane rootLayout;
    private AnchorPane MainWindow;
    private BorderPane splashLayout;
    private AnchorPane splashAnchor;
    private Task<Integer> task;
    private StartupMainController startupController;
    public XMLFileManager fileManager;
    public boolean allXMLsExist;
    public RoutePlannerMainController controller;
    private String cause;

    public RoutePlannerMainApp() {

	this.fileManager = new XMLFileManager();

    }

    @Override
    public void start(Stage primaryStage) {
	this.primaryStage = primaryStage;

	checkXMLs();
	executeStartupTask();

    }

    public void executeStartupTask() {

	this.primaryStage.close();

	initSplashLayout();
	showSplash();

	final RoutePlannerMainApp owner = this;
	this.task = new Task<Integer>() {
	    @Override
	    protected Integer call() throws Exception {
		int iterations;
		updateMessage(Constants.STARTUP_INITIALIZE);
		for (iterations = 0; iterations < 5; iterations++) {
		    if (isCancelled()) {
			try {
			    Thread.sleep(1000);
			} catch (InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			updateMessage(Constants.STARTUP_CANCEL);
			break;
		    }
		    updateProgress(iterations, 4);
		    switch (iterations) {
		    case 0:
			if (checkPrerequisites() == false) {
			    updateMessage(Constants.STARTUP_ERROR_PREREQUISITES);
			    try {
				Thread.sleep(2000);
			    } catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			    }
			    if (RoutePlannerMainApp.this.cause == "folder")
				updateMessage(Constants.STARTUP_ERROR_FOLDER);
			    else updateMessage(Constants.STARTUP_ERROR_INTERNET);
			    try {
				Thread.sleep(3000);
			    } catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			    }
			    this.cancel();
			    break;
			} else updateMessage(Constants.STARTUP_CHECK_PREREQUISITES);
			break;

		    case 1:
			if (RoutePlannerMainApp.this.allXMLsExist == false)
			    try {
				updateMessage(Constants.STARTUP_CREATE_XML_GRAPHDATA);
				String area = SettingsManager.getValue(Constants.SETTINGS_COUNTRY, "Deutschland");
				OverpassDownloader odl = new OverpassDownloader();
				odl.downloadGraphData(area);
			    } catch (Exception e) {
				updateMessage(Constants.STARTUP_ERROR_XML_GRAPHDATA);
				this.cancel();
				break;
			    }
			else {
			    try {
				Thread.sleep(1000);
			    } catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			    }
			    updateMessage(Constants.STARTUP_CHECK_XML_GRAPHDATA);
			}
			break;

		    case 2:
			if (RoutePlannerMainApp.this.allXMLsExist == false)
			    try {
				updateMessage(Constants.STARTUP_CREATE_XML_NODES);
				JDomGraphDataCreator.createNodeXML();
			    } catch (Exception e) {
				updateMessage(Constants.STARTUP_ERROR_XML_NODES);
				this.cancel();
				break;
			    }
			else {
			    try {
				Thread.sleep(1000);
			    } catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			    }
			    updateMessage(Constants.STARTUP_CHECK_XML_NODES);
			}
			break;

		    case 3:
			if (RoutePlannerMainApp.this.allXMLsExist == false)
			    try {
				updateMessage(Constants.STARTUP_CREATE_XML_ROUTES);
				JDomGraphDataCreator.createRouteXML();
			    } catch (Exception e) {
				updateMessage(Constants.STARTUP_ERROR_XML_ROUTES);
				this.cancel();
				break;
			    }
			else {
			    try {
				Thread.sleep(1000);
			    } catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			    }
			    updateMessage(Constants.STARTUP_CHECK_XML_ROUTES);
			}
			break;

		    default:
			try {
			    Thread.sleep(1000);
			} catch (InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			break;
		    }
		}

		return iterations;
	    }
	};

	this.task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
	    @Override
	    public void handle(WorkerStateEvent event) {
		owner.primaryStage.close();
		initRootLayout();
		showMainWindow();
	    }
	});

	this.task.setOnCancelled(new EventHandler<WorkerStateEvent>() {
	    @Override
	    public void handle(WorkerStateEvent event) {
		owner.primaryStage.close();
	    }
	});

	this.startupController.getProgressBar().progressProperty().bind(this.task.progressProperty());
	this.startupController.getLabel().textProperty().bind(this.task.messageProperty());

	Thread th = new Thread(this.task);
	th.setDaemon(true);
	th.start();
    }

    /**
     * Splash-Layout initialisieren, laden und anzeigen Alle XMLs releasen
     */
    public void initSplashLayout() {
	try {
	    try {
		this.fileManager.releaseAllXML();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    this.primaryStage = new Stage();

	    // Load root layout from fxml file.
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(RouteplannerStartup.class.getResource("StartupRoot.fxml"));
	    this.splashLayout = (BorderPane) loader.load();

	    // Show the scene containing the root layout.
	    Scene scene = new Scene(this.splashLayout);
	    this.primaryStage.setScene(scene);
	    this.primaryStage.initStyle(StageStyle.TRANSPARENT);
	    this.primaryStage.show();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Eigentliches Splash anzeigen Controller zuweisen
     */
    public void showSplash() {
	try {
	    // Load person overview.
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(RouteplannerStartup.class.getResource("StartupMain.fxml"));
	    this.splashAnchor = (AnchorPane) loader.load();

	    // Set person overview into the center of root layout.
	    this.splashLayout.setCenter(this.splashAnchor);

	    this.startupController = loader.getController();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
	try {

	    this.primaryStage = new Stage();
	    this.primaryStage.setTitle("DHBW-Routenplaner 2014");

	    // Load root layout from fxml file.
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(RoutePlannerMainApp.class.getResource(Constants.FXML_ROOT));
	    this.rootLayout = (BorderPane) loader.load();

	    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
	    this.primaryStage.setX(screenBounds.getMinX());
	    this.primaryStage.setY(screenBounds.getMinY());
	    this.primaryStage.setWidth(screenBounds.getWidth());
	    this.primaryStage.setHeight(screenBounds.getHeight());
	    this.primaryStage.setMinWidth(1440);
	    this.primaryStage.setMinHeight(900);

	    // Show the scene containing the root layout.
	    Scene scene = new Scene(this.rootLayout);
	    this.primaryStage.setTitle("Routenplaner");
	    this.primaryStage.setScene(scene);
	    this.primaryStage.initStyle(StageStyle.DECORATED);
	    //scene.setFill(Color.TRANSPARENT);
	    this.primaryStage.show();

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
	    this.MainWindow = (AnchorPane) loader.load();

	    // Set person overview into the center of root layout.
	    this.rootLayout.setCenter(this.MainWindow);

	    this.controller = loader.getController();
	    this.controller.setRoutePlannerMainApp(this);

	    try {
		this.fileManager.lockAllXML();
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Check the programs prerequisites Check connection to the overpass-website Check, if the program-folder exists,
     * and creates it if not
     *
     * @return true if program has access to the required website and if the program folder exists
     */
    private boolean checkPrerequisites() {

	boolean result = true;

	try {
	    InetAddress.getByName("overpass-api.de").isReachable(10000);
	} catch (Exception e) {
	    result = false;
	    this.cause = "internet";
	}

	try {
	    File f = new File(Constants.PROGRAM_HOME);
	    if (f.exists() && f.isDirectory()) {

	    } else {
		File progDir = new File(Constants.PROGRAM_HOME);
		result = progDir.mkdir();
	    }
	} catch (Exception e) {
	    result = false;
	    this.cause = "folder";
	}

	return result;

    }

    private void checkXMLs() {
	this.allXMLsExist = true;
	if (XMLFileManager.fileExists(XMLFileManager.getExtendedXMLFileName(Constants.XML_GRAPHDATA)) == false)
	    this.allXMLsExist = false;
	if (XMLFileManager.fileExists(XMLFileManager.getExtendedXMLFileName(Constants.XML_NODES)) == false)
	    this.allXMLsExist = false;
	if (XMLFileManager.fileExists(XMLFileManager.getExtendedXMLFileName(Constants.XML_ROUTES)) == false)
	    this.allXMLsExist = false;
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
	return this.primaryStage;
    }

    public static void main(String[] args) {
	launch(args);
    }

}
