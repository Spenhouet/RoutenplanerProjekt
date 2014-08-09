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
    private XMLFileManager fileManager;

    public RoutePlannerMainApp() {

	fileManager = new XMLFileManager();

    }

    @Override
    public void start(Stage primaryStage) {
	this.primaryStage = primaryStage;

	executeStartupTask();

    }

    public void executeStartupTask() {

	primaryStage.close();

	initSplashLayout();
	showSplash();

	final RoutePlannerMainApp owner = this;
	task = new Task<Integer>() {
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
				Thread.sleep(5000);
			    } catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			    }
			    this.cancel();
			    break;
			} else {
			    updateMessage(Constants.STARTUP_CHECK_PREREQUISITES);
			}
			break;

		    case 1:
			if (XMLFileManager.fileExists(XMLFileManager.getExtendedXMLFileName(Constants.XML_GRAPHDATA)) == false) {
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
			} else {
			    try {
				Thread.sleep(1000);
			    } catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			    }
			    updateMessage(Constants.STARTUP_CHECK_XML_GRAPHDATA);
			}
			break;

		    case 2:
			if (XMLFileManager.fileExists(XMLFileManager.getExtendedXMLFileName(Constants.XML_NODES)) == false) {
			    try {
				updateMessage(Constants.STARTUP_CREATE_XML_NODES);
				JDomGraphDataCreator.createNodeXML();
			    } catch (Exception e) {
				updateMessage(Constants.STARTUP_ERROR_XML_NODES);
				this.cancel();
				break;
			    }
			} else {
			    try {
				Thread.sleep(1000);
			    } catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			    }
			    updateMessage(Constants.STARTUP_CHECK_XML_NODES);
			}
			break;

		    case 3:
			if (XMLFileManager.fileExists(XMLFileManager.getExtendedXMLFileName(Constants.XML_ROUTES)) == false) {
			    try {
				updateMessage(Constants.STARTUP_CREATE_XML_ROUTES);
				JDomGraphDataCreator.createRouteXML();
			    } catch (Exception e) {
				updateMessage(Constants.STARTUP_ERROR_XML_ROUTES);
				this.cancel();
				break;
			    }

			} else {
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

	task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
	    @Override
	    public void handle(WorkerStateEvent event) {
		owner.primaryStage.close();
		initRootLayout();
		showMainWindow();
	    }
	});

	task.setOnCancelled(new EventHandler<WorkerStateEvent>() {
	    @Override
	    public void handle(WorkerStateEvent event) {
		owner.primaryStage.close();
	    }
	});

	startupController.getProgressBar().progressProperty().bind(task.progressProperty());
	startupController.getLabel().textProperty().bind(task.messageProperty());

	Thread th = new Thread(task);
	th.setDaemon(true);
	th.start();
    }

    /**
     * Splash-Layout initialisieren, laden und anzeigen
     * Alle XMLs releasen
     */
    public void initSplashLayout() {
	try {
	    try {
		fileManager.releaseAllXML();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    primaryStage = new Stage();

	    // Load root layout from fxml file.
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(RouteplannerStartup.class.getResource("StartupRoot.fxml"));
	    splashLayout = (BorderPane) loader.load();

	    // Show the scene containing the root layout.
	    Scene scene = new Scene(splashLayout);
	    primaryStage.setScene(scene);
	    primaryStage.initStyle(StageStyle.TRANSPARENT);
	    primaryStage.show();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Eigentliches Splash anzeigen
     * Controller zuweisen
     */
    public void showSplash() {
	try {
	    // Load person overview.
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(RouteplannerStartup.class.getResource("StartupMain.fxml"));
	    splashAnchor = (AnchorPane) loader.load();

	    // Set person overview into the center of root layout.
	    splashLayout.setCenter(splashAnchor);

	    startupController = loader.getController();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
    * Initializes the root layout.
    */
    public void initRootLayout() {
	try {
	    //	    try {
	    //		fileManager.lockAllXML();
	    //	    } catch (IOException e) {
	    //		e.printStackTrace();
	    //	    }

	    primaryStage = new Stage();
	    primaryStage.setTitle("Routenplaner");

	    // Load root layout from fxml file.
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(RoutePlannerMainApp.class.getResource(Constants.FXML_ROOT));
	    rootLayout = (BorderPane) loader.load();

	    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
	    primaryStage.setX(screenBounds.getMinX());
	    primaryStage.setY(screenBounds.getMinY());
	    primaryStage.setWidth(screenBounds.getWidth());
	    primaryStage.setHeight(screenBounds.getHeight());

	    // Show the scene containing the root layout.
	    Scene scene = new Scene(rootLayout);
	    primaryStage.setTitle("Routenplaner");
	    primaryStage.setScene(scene);
	    primaryStage.initStyle(StageStyle.DECORATED);
	    //scene.setFill(Color.TRANSPARENT);
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
	    MainWindow = (AnchorPane) loader.load();

	    // Set person overview into the center of root layout.
	    rootLayout.setCenter(MainWindow);

	    RoutePlannerMainController controller = loader.getController();
	    controller.setRoutePlannerMainApp(this);

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Check the programs prerequisites
     * Check connection to the overpass-website
     * Check, if the program-folder exists, and creates it if not
     * @return true if program has access to the required website and if the program folder exists
     */
    private boolean checkPrerequisites() {

	boolean result = true;

	try {
	    InetAddress.getByName("overpass-api.de").isReachable(10000);
	} catch (Exception e) {
	    result = false;
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
	}

	return result;

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
