package com.edifixio.amine.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Facet extends LinkedList<FacetUnite>{
	private String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public void facetProduice(Facet facet){
	
		Map<String,FacetUnite> facetMap =new HashMap<String, FacetUnite>();
		for(int i=0;i<this.size();i++){
			FacetUnite facetUnite=this.get(i);
			facetUnite.setCount(0);
			facetMap.put(facetUnite.getTerm(),facetUnite);
		}
		
		Iterator<FacetUnite> facetIter=facet.iterator();
		
		while(facetIter.hasNext()){
			FacetUnite facetUnite=facetIter.next();
			facetMap.get(facetUnite.getTerm()).setCount(facetUnite.getCount());
		}	
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
    			checkedTerm.add(facetUnite.getTerm());
    	}
    	return checkedTerm;
  
    }
  
   public static void main(String args[]){
	  Facet f= new Facet();
	  f.setName("my");
	  f.add(new FacetUnite("cc",5, false));
	  f.add(new FacetUnite("dd", 1, true));
	   
	  Facet f1=new Facet();
	  f1.add(new FacetUnite("cc", 7, true));
	  f1.add(new FacetUnite("dd", 7, true));
	  f.facetProduice(f1);
	  System.out.println(f);
	  
   }


}
