package com.edifixio.amine.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.edifixio.amine.beans.RequestBean;
import com.edifixio.amine.config.MappingAlias;
import com.edifixio.amine.utiles.MyEntry;
import com.edifixio.amine.utiles.Utiles;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class IterateBuildController extends FirstBuildController{
	

	
	private Object request;


	public IterateBuildController(JsonObject jsonRequest) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		super(jsonRequest);
	}
	
	
	public void setRequest(Object request){
		this.request=request;
	}
	

	
/***************************** recupere the name and the field agreged********************************************/	
	public Entry<String,String> getAggTypeFiled(String name){
		JsonObject queryFacet = Utiles.seletor("aggs::"+name, query).getAsJsonObject();
		
		Entry<String, JsonElement> agg=queryFacet.entrySet().iterator().next();
		MyEntry< String, String> typeAgg=new MyEntry<String, String>(
														agg.getKey(),
														agg.getValue()
															.getAsJsonObject().entrySet()
															.iterator().next()
															.getValue().getAsString());
		
		System.out.println(agg);
		return typeAgg;
	}
	

	public void putFilterOfFacetsRequest(){
		if(facets==null|| facets.size()==0)return;
		Iterator<Facet> facetsIter=facets.iterator();
		Facet facet;
		while(facetsIter.hasNext()){
			facet=facetsIter.next();
			Entry<String,String> facetAggsType=getAggTypeFiled(facet.getName());
			
			if(facetAggsType.getKey().equals("terms")) 
				new TermsProcessFilter(query,facet.getCheckedTerms(),facetAggsType.getValue()).putFilter();
			
		}
		System.out.println("%%%%%%%%%%"+query);	
	}


	public void processRequest() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		MappingAlias reqMap = mainConfig.getRequestMapping();
		if (reqMap.getBeanClass() != request.getClass())
			return;
		reqMap.resolve();
		Properties mapping = reqMap.getMapping();
		Iterator<Entry<Object, Object>> mappingIter = mapping.entrySet().iterator();

		while (mappingIter.hasNext()) {
			Entry<Object, Object> entry = mappingIter.next();
			String attr = ((String) entry.getKey());
			String getMethodeName = "get" + attr.substring(0, 1).toUpperCase() + attr.substring(1);
			Method method = request.getClass().getMethod(getMethodeName);
			Object result = method.invoke(request);

			@SuppressWarnings("unchecked")
			List<String> substitutions = (List<String>) entry.getValue();
			Iterator<String> substIter = substitutions.iterator();

			while (substIter.hasNext()) {
				String sub = substIter.next();
				int lastSelectorIndex = sub.lastIndexOf("::");
				String prefix = sub.substring(0, lastSelectorIndex);
				String lastSelector = sub.substring(lastSelectorIndex + 2);
				JsonObject jso = Utiles.seletor(prefix, query).getAsJsonObject();
				if (jso.get(lastSelector).getAsString().equals("??")) {
					jso.remove(lastSelector);
					jso.addProperty(lastSelector, (String) result);
				}
			}
		}
		request.getClass().getMethods();
	}
	
	

	
	public List<Facet> processFacetList(List<Facet> facets) {
		Map<String,Facet> facetsMap=new HashMap<String, Facet>();
		
		for(int i=0;i<this.facets.size();i++){
			Facet facet=facets.get(i);
			facetsMap.put(facet.getName(), facet);
		}
	
		Iterator<Facet> facetsIter=this.facets.iterator();
		while(facetsIter.hasNext()){
			Facet facet=facetsIter.next();
			facetsMap.get(facet.getName()).facetProduice(facet);
		}
	
		return facets;
	}
	

	public static void main(String args[]) throws JsonIOException, JsonSyntaxException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException  {
	
		
		JsonObject fjo = new JsonParser().parse(new FileReader(new File("/home/amine/Bureau/confQuery/Voiture/query.json")))
				.getAsJsonObject();
		FirstBuildController fbc=new FirstBuildController(fjo);
	
		System.out.println(fbc.query);
		fbc.connection();
		System.out.println(fbc.jestClient);
		System.out.println(fbc.execute().jestResult.getJsonString());
		List<Facet> initFacet;
		fbc.processFacetList();
		System.out.println(initFacet=fbc.facets);
		System.out.println(fbc.processJsonToObjects().toString());	
		
		
		
		
	/*********************** inject request in the second request ************************************************************/	
		JsonObject jo = new JsonParser().parse(new FileReader(new File("/home/amine/workspaceHistory/workspace/"
				+ "QuickBuildElasticRefact/src/resources"
				+ "/model.json")))
				.getAsJsonObject();
		
		IterateBuildController ibc = new IterateBuildController(jo);
		ibc.setRequest(new RequestBean());
		
		
		
		System.out.println(ibc.request);
		System.out.println(ibc.query);
		
		
		
	
		
		
	}

}
