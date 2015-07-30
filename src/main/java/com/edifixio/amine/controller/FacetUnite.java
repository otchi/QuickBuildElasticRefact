package com.edifixio.amine.controller;

public  class FacetUnite {


	private Integer count;
	private Boolean checked;
	private String term;
	
	

	public FacetUnite(String term,Integer count, Boolean checked) {
		super();
		this.count = count;
		this.checked = checked;
		this.term = term;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\n\t"+term+"||"+count+"||"+checked;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
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
