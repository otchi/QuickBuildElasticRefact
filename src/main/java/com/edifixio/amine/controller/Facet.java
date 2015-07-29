package com.edifixio.amine.controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class Facet extends LinkedList<FacetUnite>{
	private String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\n\t"+this.name+"::"+super.toString();
	}
	
    public List<String> getCheckedTerms(){
    	List<String> checkedTerm=new LinkedList<String>();
    	Iterator<FacetUnite> facetIter=this.iterator();
    	while(facetIter.hasNext()){
    		FacetUnite facetUnite=facetIter.next();
    		if(facetUnite.getChecked()) 
    			checkedTerm.add(facetUnite.getFacetUnit());
    	}
    	return checkedTerm;
  
    }


}
