package com.edifixio.amine.controller;

import java.util.Iterator;
import java.util.List;

import com.edifixio.amine.utiles.Utiles;
import com.edifixio.jsonFastBuild.ArrayBuilder.IBuildJsonArray;
import com.edifixio.jsonFastBuild.ObjectBuilder.IPutProprety;
import com.edifixio.jsonFastBuild.ObjectBuilder.IRootJsonBuilder;
import com.edifixio.jsonFastBuild.ObjectBuilder.JsonObjectBuilder;
import com.google.gson.JsonObject;

public class TermsProcessFilter implements IProcessFilter{

	JsonObject query;
	List<String> checkedTerm;
	String field;
	
	
	public TermsProcessFilter(){
		super();
	}
	
	public TermsProcessFilter(JsonObject query,List<String> checkedTerm,String field){
		this.query=query;
		this.checkedTerm=checkedTerm;
		this.field=field;
	
	}
		
	
	public JsonObject putFilter() {
		// TODO Auto-generated method stub
		IBuildJsonArray<IPutProprety<IPutProprety<IRootJsonBuilder>>> arrayField=
		JsonObjectBuilder.init()
							.begin()
									.putObject("term")
									.begin()
										.putArray(field)
										.begin();
		Iterator<String> terms=this.checkedTerm.iterator();
		
		while(terms.hasNext()){
			arrayField.putValue(terms.next());
		}
		
		JsonObject jso=arrayField.end().end().end().getJsonElement().getAsJsonObject();
		
		return 
				(Utiles
						.seletor("term::"+field, jso)
						.getAsJsonArray().size()!=0)			?
								
								(arrayField.end()
										.end()
									.end()
								.getJsonElement()
								.getAsJsonObject())					
								
																:	
								null;
								
	}

}
