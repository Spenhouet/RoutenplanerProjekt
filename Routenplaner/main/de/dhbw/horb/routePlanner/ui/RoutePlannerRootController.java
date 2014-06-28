package de.dhbw.horb.routePlanner.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class RoutePlannerRootController {

	// Reference to the main application.
	private RoutePlannerMainApp routePlannerMainApp;
	
	public RoutePlannerRootController() {
	}
	
    @FXML
    private MenuItem closeMenuButton;

    @FXML
    void closeMenuButtonClicked(ActionEvent event) {
    	routePlannerMainApp.primaryStage.close();
    }
    
    /**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setRoutePlannerMainApp(RoutePlannerMainApp routePlannerMainApp) {
		this.routePlannerMainApp = routePlannerMainApp;
	}

}