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
public class Junction {
	
	private int price;
	private List<String> ids = new ArrayList<String>();
	
	public Junction(List<String> ids, int price) {
		this.ids = ids;
		this.price = price;
	}
	
	public Junction(){
		
	}

	public Junction(List<String> ids) {
		this.ids = ids;
	}
	
	public List<String> getIds(){
		return ids;
	}
	
	public void setId(int index, String id){
		ids.set(index, id);
	}
	
	public void setPrice(int price){
		this.price = price;
	}
	
	public int getPrice(){
		return price;
	}
}
