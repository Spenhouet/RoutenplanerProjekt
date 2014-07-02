package de.dhbw.horb.routePlanner.evaluation;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse Crossroads
 * Repräsentiert eine Kreuzung mit verschiedenen Auf- Abfahrten
 * Besitzt Eigenschaft Kosten, mit der die Kreuzung erreicht werden kann
 * @author Simon
 *
 */
public class Crossroads {

	private int coast;
	private List<String> ids = new ArrayList<String>();
	
	public Crossroads(List<String> ids){
		this.ids = ids;
	}
}
