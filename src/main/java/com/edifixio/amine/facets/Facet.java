package com.edifixio.amine.facets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Facet {
	private String name;
	private List<FacetUnite> terms=new LinkedList<FacetUnite>();


	public List<FacetUnite> getTerms() {
		return terms;
	}

	public void setTerms(List<FacetUnite> terms) {
		this.terms = terms;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public void facetProduice(Facet facet){
	
		Map<String,FacetUnite> facetMap =new HashMap<String, FacetUnite>();
		for(int i=0;i<this.terms.size();i++){
			FacetUnite facetUnite=this.terms.get(i);
			facetUnite.setCount(0);
			facetMap.put(facetUnite.getTerm(),facetUnite);
		}
		
		Iterator<FacetUnite> facetIter=facet.terms.iterator();
		
		while(facetIter.hasNext()){
			FacetUnite facetUnite=facetIter.next();
			facetMap.get(facetUnite.getTerm()).setCount(facetUnite.getCount());
		}	
	}
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\n\t"+this.name+"::"+this.terms;
	}
	
    public List<String> getCheckedTerms(){
    	List<String> checkedTerm=new LinkedList<String>();
    	Iterator<FacetUnite> facetIter=this.terms.iterator();
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
	  f.terms.add(new FacetUnite("cc",5, false));
	  f.terms.add(new FacetUnite("dd", 1, true));
	   
	  Facet f1=new Facet();
	  f1.terms.add(new FacetUnite("cc", 7, true));
	  f1.terms.add(new FacetUnite("dd", 7, true));
	  f.facetProduice(f1);
	  System.out.println(f);
	  
   }


}
