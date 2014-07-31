package de.dhbw.horb.routePlanner.ui;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.data.JDomGraphDataCreator;
import de.dhbw.horb.routePlanner.data.OverpassDownloader;

public class RoutePlannerMainApp extends Application {

    public Stage primaryStage;
    private BorderPane rootLayout;
    private AnchorPane MainWindow;
    private BorderPane splashLayout;
    private AnchorPane splashAnchor;
    private Task<Integer> task;
    private StartupMainController startupController;

    public RoutePlannerMainApp() {
    }

    @Override
    public void start(Stage primaryStage) {
	this.primaryStage = primaryStage;
	this.primaryStage.setTitle("Routenplaner");

	initSplashLayout();

	showSplash();

	final Stage finalStage = primaryStage;
	task = new Task<Integer>() {
	    @Override
	    protected Integer call() throws Exception {
		int iterations;
		updateMessage("Initialisiere...");
		for (iterations = 0; iterations < 5; iterations++) {
		    if (isCancelled()) {
			updateMessage("Cancelled");
			break;
		    }
		    updateProgress(iterations, 4);
		    switch (iterations) {
		    case 0:
			if (checkPrerequisites() == false) {
			    updateMessage("Startbedingungen nicht erf�llt!");
			    try {
				Thread.sleep(5000);
			    } catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			    }
			    this.cancel();
			    break;
			} else {
			    updateMessage("�berpr�fe Startvorraussetzungen...");
			}
			break;

		    case 1:
			if (checkGraphDataXML() == false) {
			    updateMessage("GraphDataXML wird erzeugt...");
			    String area = "Deutschland";
			    OverpassDownloader odl = new OverpassDownloader();
			    odl.downloadGraphData(area);
			    //			    try {
			    //				Thread.sleep(5000);
			    //			    } catch (InterruptedException ex) {
			    //				Thread.currentThread().interrupt();
			    //			    }
			    //			    this.cancel();
			    //			    break;
			} else {
			    try {
				Thread.sleep(1000);
			    } catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			    }
			    updateMessage("�berpr�fe GraphDataXML...");
			}
			break;

		    case 2:
			if (checkNodeXML() == false) {
			    if (checkRouteXML() == true) {

			    }
			    updateMessage("NodeXML wird erzeugt...");
			    JDomGraphDataCreator dom = new JDomGraphDataCreator();
			    dom.createNewXMLFiles();
			    //			    try {
			    //				Thread.sleep(5000);
			    //			    } catch (InterruptedException ex) {
			    //				Thread.currentThread().interrupt();
			    //			    }
			    //			    this.cancel();
			    //			    break;
			} else {
			    try {
				Thread.sleep(1000);
			    } catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			    }
			    updateMessage("�berpr�fe NodeXML...");
			}
			break;

		    case 3:
			if (checkRouteXML() == false) {
			    updateMessage("RouteXML wird erzeugt...");
			    JDomGraphDataCreator dom = new JDomGraphDataCreator();
			    dom.createNewXMLFiles();
			    //			    try {
			    //				Thread.sleep(5000);
			    //			    } catch (InterruptedException ex) {
			    //				Thread.currentThread().interrupt();
			    //			    }
			    //			    this.cancel();
			    //			    break;
			} else {
			    try {
				Thread.sleep(1000);
			    } catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			    }
			    updateMessage("�berpr�fe RouteXML...");
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
		finalStage.close();
		initRootLayout();
		showMainWindow();
	    }
	});

	task.setOnCancelled(new EventHandler<WorkerStateEvent>() {
	    @Override
	    public void handle(WorkerStateEvent event) {
		finalStage.close();
	    }
	});

	startupController.getProgressBar().progressProperty().bind(task.progressProperty());
	startupController.getLabel().textProperty().bind(task.messageProperty());

	Thread th = new Thread(task);
	th.setDaemon(true);
	th.start();

    }

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
	    //scene.setFill(Color.TRANSPARENT);
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
	    primaryStage = new Stage();

	    // Load root layout from fxml file.
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(RoutePlannerMainApp.class.getResource(Constants.FXML_ROOT));
	    rootLayout = (BorderPane) loader.load();

	    // Show the scene containing the root layout.
	    Scene scene = new Scene(rootLayout);
	    primaryStage.setTitle("Routenplaner");
	    primaryStage.setScene(scene);
	    primaryStage.initStyle(StageStyle.DECORATED);
	    //scene.setFill(Color.TRANSPARENT);
	    primaryStage.show();

	    RoutePlannerRootController controller = loader.getController();
	    controller.setRoutePlannerMainApp(this);

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

    private boolean checkPrerequisites() {

	boolean result = true;

	try {
	    InetAddress.getByName("overpass-api.de").isReachable(10000);
	} catch (Exception e) {
	    result = false;
	}

	return result;

    }

    private boolean checkNodeXML() {

	boolean result = true;

	try {
	    File file = new File(Constants.XML_NODES);
	    result = file.exists();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return result;

    }

    private boolean checkRouteXML() {

	boolean result = true;

	try {
	    File file = new File(Constants.XML_ROUTES);
	    result = file.exists();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return result;

    }

    private boolean checkGraphDataXML() {

	boolean result = true;

	try {
	    File file = new File(Constants.XML_GRAPHDATA);
	    result = file.exists();
	} catch (Exception e) {
	    e.printStackTrace();
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
