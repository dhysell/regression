package repository.gw.generate.custom;

import repository.gw.enums.LivestockType;

import java.util.ArrayList;

public class LivestockList {
	
	private Livestock livestock;
	private Livestock deathOfLivestock;
	private Livestock fourH;
	
	public LivestockList(Livestock livestock, Livestock deathOfLivestock, Livestock fourH) throws Exception {
		setLivestock(livestock);
		setDeathOfLivestock(deathOfLivestock);
		setFourH(fourH);
	}
	
	public ArrayList<Livestock> getAllLivestockAsList() {
		ArrayList<Livestock> toReturn = new ArrayList<Livestock>();
		toReturn.add(getLivestock());
		toReturn.add(getDeathOfLivestock());
		toReturn.add(getFourH());
		
		return toReturn;
	}
	
	public Livestock getLivestock() {
		return livestock;
	}
	
	public void setLivestock(Livestock livestock) throws Exception {
		if(livestock != null) {
			if(livestock.getType() != repository.gw.enums.LivestockType.Livestock) {
				throw new Exception("ERROR: This must be of type LIVESTOCK");
			}
		}
		this.livestock = livestock;
	}
	
	public Livestock getDeathOfLivestock() {
		return deathOfLivestock;
	}
	
	public void setDeathOfLivestock(Livestock deathOfLivestock) throws Exception {
		if(deathOfLivestock != null) {
			if(deathOfLivestock.getType() != repository.gw.enums.LivestockType.DeathOfLivestock) {
				throw new Exception("ERROR: This must be of type DEATHOFLIVESTOCK");
			}
		}
		this.deathOfLivestock = deathOfLivestock;
	}
	
	public Livestock getFourH() {
		return fourH;
	}
	
	public void setFourH(Livestock fourH) throws Exception {
		if(fourH != null) {
			if(fourH.getType() != LivestockType.FourH) {
				throw new Exception("ERROR: This must be of type 4H");
			}
		}
		this.fourH = fourH;
	}

}
