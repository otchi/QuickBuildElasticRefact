package com.edifixio.amine.beans;

public class ResponseBean {
private Number id;
private String voiture;
private Number MPG;
private Number cylendres;
private Number vitesseMax;
private Number nbrChauveaux;
private Number poid;
private Number acceleration;
private Number model;
private String origine;



public Number getId() {
	return id;
}



public void setId(Number id) {
	this.id = id;
}



public String getVoiture() {
	return voiture;
}



public void setVoiture(String voiture) {
	this.voiture = voiture;
}



public Number getMPG() {
	return MPG;
}



public void setMPG(Number mPG) {
	MPG = mPG;
}



public Number getCylendres() {
	return cylendres;
}



public void setCylendres(Number cylendres) {
	this.cylendres = cylendres;
}



public Number getVitesseMax() {
	return vitesseMax;
}



public void setVitesseMax(Number vitesseMax) {
	this.vitesseMax = vitesseMax;
}



public Number getNbrChauveaux() {
	return nbrChauveaux;
}



public void setNbrChauveaux(Number nbrChauveaux) {
	this.nbrChauveaux = nbrChauveaux;
}



public Number getPoid() {
	return poid;
}



public void setPoid(Number poid) {
	this.poid = poid;
}



public Number getAcceleration() {
	return acceleration;
}



public void setAcceleration(Number acceleration) {
	this.acceleration = acceleration;
}



public Number getModel() {
	return model;
}



public void setModel(Number model) {
	this.model = model;
}



public String getOrigine() {
	return origine;
}



public void setOrigine(String origine) {
	this.origine = origine;
}



@Override
public String toString() {
	// TODO Auto-generated method stub
	return this.voiture;
}

}
