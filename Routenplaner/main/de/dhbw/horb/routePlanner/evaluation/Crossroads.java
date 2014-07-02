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

	private int price;
	private List<String> ids = new ArrayList<String>();

	public Crossroads() {

	}

	public Crossroads(List<String> ids) {
		this.ids = ids;
	}
	
	public List<String> getIds(){
		return ids;
	}
	
	public void setPrice(int price){
		this.price = price;
	}
	
	public int getPrice(){
		return price;
	}
}
