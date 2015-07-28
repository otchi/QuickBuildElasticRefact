package com.edifixio.amine.beans;

public class ResponseBean {
private String voitureName;
private String pays;
private Number year;

public Number  getYear() {
	return year;
}

public void setYear(Number  year) {
	this.year = year;
}

public String getPays() {
	return pays;
}

public void setPays(String pays) {
	this.pays = pays;
}

public String getVoitureName() {
	return voitureName;
}

public void setVoitureName(String voitureName) {
	this.voitureName = voitureName;
}

@Override
public String toString() {
	// TODO Auto-generated method stub
	return voitureName+"--"+pays+"--"+year;
}

}
