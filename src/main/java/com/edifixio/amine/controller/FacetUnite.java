package com.edifixio.amine.controller;

public  class FacetUnite {


	private Integer count;
	private Boolean checked;
	private String facetUnit;
	
	

	public FacetUnite(String facetUnit,Integer count, Boolean checked) {
		super();
		this.count = count;
		this.checked = checked;
		this.facetUnit = facetUnit;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\n\t"+facetUnit+"||"+count+"||"+checked;
	}

	public String getFacetUnit() {
		return facetUnit;
	}

	public void setFacetUnit(String facetUnit) {
		this.facetUnit = facetUnit;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}


}
