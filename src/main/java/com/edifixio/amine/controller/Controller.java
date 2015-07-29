package com.edifixio.amine.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import com.edifixio.amine.beans.RequestBean;
import com.edifixio.amine.config.MainConfig;
import com.edifixio.amine.config.Mapping;
import com.edifixio.amine.config.MappingAlias;
import com.edifixio.amine.configDAO.MainConfigDAO;
import com.edifixio.amine.utiles.MyEntry;
import com.edifixio.amine.utiles.Utiles;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.Search.Builder;

public class Controller {
	private String config ;
	public static final String RESULT_SOURCE="hits::hits" ;
	private MainConfig mainConfig;
	private JsonObject query;
	private JestClient jestClient;
	private JestResult jestResult;
	private List<Facet> facets;
	private Object request;


	public Controller(String config) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		this.config=config;
		init();
	}
	
	
	public void setRequest(Object request){
		this.request=request;
	}
	
	public void setFacets(List<Facet> facets){
		this.facets=facets;
	}

	
	
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
	
	
	public void processFacetRequest(){
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

	public void connection() {
		jestClient=ElasticClient.getElasticClient(mainConfig.getHost()).getClient();
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
	
	
	
	public JestResult processJsonResult() throws IOException{
		Properties indexes=mainConfig.getIndexes();
		Iterator<Entry<Object, Object>> indexesIter=indexes.entrySet().iterator();
		Builder builder=new Search.Builder(query.toString());
		
		while(indexesIter.hasNext()){
			Entry<Object, Object> entry=indexesIter.next();
			System.out.println(entry.getKey());
			builder.addIndex((String) entry.getKey());
		}
		return (this.jestResult=this.jestClient.execute(builder.build()));  
	}
	
	
	
	public List<Object> processResultObjects() throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		Mapping responseMapping=mainConfig.getResponseMapping();
		JsonArray source=Utiles.seletor(RESULT_SOURCE, jestResult.getJsonObject()).getAsJsonArray();
		//////System.out.println(responseMapping.getBeanClass());
		///////System.out.println(source);
		Properties mapping=responseMapping.getMapping();
		Iterator<Entry<Object, Object>> mappingIter=mapping.entrySet().iterator();
		List<Object> result=new LinkedList<Object>();
		
		for(int i=0;i<source.size();i++)
			result.add(responseMapping.getBeanClass().newInstance());
		
		while(mappingIter.hasNext()){
			Entry<Object, Object> entry=mappingIter.next();
			String key=(String) entry.getKey();
			String methodeName="set"+key.substring(0, 1).toUpperCase()+key.substring(1);	
			Iterator<JsonElement> sourceIter=source.iterator();
			String value=(String) entry.getValue();
			String selector=(value.substring(0, 2).equals("$$"))?"_source::"+value.substring(2):value;
			
			if(sourceIter.hasNext()){
				JsonObject jo=sourceIter.next().getAsJsonObject();
				JsonElement sourceValue=Utiles.seletor(selector, jo);
				Class<?> c=null;
				JsonPrimitive jop=sourceValue.getAsJsonPrimitive();
				if(jop.isString()) c=String.class;
				if(jop.isNumber()) c=Number.class;
				if(jop.isBoolean()) c=Boolean.class;
				
				Method method=responseMapping.getBeanClass().getMethod(methodeName, c);
				int i=0;
				while(sourceIter.hasNext()){
					 jo=sourceIter.next().getAsJsonObject();
					 sourceValue=Utiles.seletor(selector, jo);
					//System.out.println(result.get(i++));
					//System.out.println(sourceValue);
					if(c==String.class)
						method.invoke(result.get(i++),sourceValue.getAsString());	
					if(c==Number.class)
						method.invoke(result.get(i++),sourceValue.getAsNumber());
					if(c==Boolean.class)
						method.invoke(result.get(i++),sourceValue.getAsBoolean());
				}
			}
		}
		
		return result;
	}
	
	
	public List<Facet> processFirstFacetList() {
		List<String> conf;
		List<Facet> result = new LinkedList<Facet>();
		Facet facet;

		if ((conf = mainConfig.getFacets()) == null)
			return new ArrayList<Facet>();

		Iterator<String> confIter = conf.iterator();

		while (confIter.hasNext()) {
			facet = new Facet();
			String facetName = confIter.next();
			facet.setName(facetName);
			JsonElement facetResult = Utiles.seletor("aggregations::" + facetName + "::buckets",
					jestResult.getJsonObject());
	
			Iterator<JsonElement> facetResultIter = facetResult.getAsJsonArray().iterator();

			while (facetResultIter.hasNext()) {
				JsonObject jso = facetResultIter.next().getAsJsonObject();
				facet.add(new FacetUnite(jso.get("key").getAsString(), jso.get("doc_count").getAsInt(), false));

			}
			result.add(facet);
		}
		return result;
	}
	
	
	
	public void init() throws  JsonSyntaxException, FileNotFoundException {
		JsonObject jo = new JsonParser().parse(new FileReader(new File(config))).getAsJsonObject();
		mainConfig = new MainConfigDAO(jo).getConfig();
		this.query = jo.get("_query").getAsJsonObject();
	}


	public static void main(String args[])
			throws JsonIOException, JsonSyntaxException, NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, IOException, InstantiationException {
		Facet l=new Facet();
		List<Facet> h=new LinkedList<Facet>();
		l.add(new FacetUnite("5", 0, true));
		l.setName("test");
		h.add(l);
		Controller c = new Controller(	"/home/amine/workspace" 
				+ "/QuickBuildElasticRefact"
				+ "/src/resources/model.json");
		c.setFacets(h);
		c.setRequest(new RequestBean());
		c.processRequest();
		c.connection();
		System.out.println(c.mainConfig.getRequestMapping());
		System.out.println(c.query);
		c.processFacetRequest();
		System.out.println(c.processJsonResult().getJsonObject());
		System.out.println(c.processResultObjects());
		System.out.println("---"+c.processFirstFacetList());
		System.out.println(c.getAggTypeFiled("test"));
		
	}

}
