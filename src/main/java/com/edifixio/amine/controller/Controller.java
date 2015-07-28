package com.edifixio.amine.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.edifixio.amine.beans.RequestBean;
import com.edifixio.amine.config.MainConfig;
import com.edifixio.amine.config.Mapping;
import com.edifixio.amine.config.MappingAlias;
import com.edifixio.amine.configDAO.MainConfigDAO;
import com.edifixio.amine.utiles.MyEntry;
import com.edifixio.amine.utiles.Utiles;
import com.edifixio.jsonFastBuild.ArrayBuilder.IBuildJsonArray;
import com.edifixio.jsonFastBuild.ObjectBuilder.IPutProprety;
import com.edifixio.jsonFastBuild.ObjectBuilder.IRootJsonBuilder;
import com.edifixio.jsonFastBuild.ObjectBuilder.JsonObjectBuilder;
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
	public static final String JSON_CONFIG_PATH = "/home/amine/workspace" + "/QuickBuildElasticRefact"
			+ "/src/resources/model.json";
	public static final String RESULT_SOURCE="hits::hits" ;
	private MainConfig mainConfig;
	private JsonObject query;
	private JestClient jestClient;
	private JestResult jestResult;
	private Map<String,List<Entry<Entry<String,Number>, Boolean>>> facets;

	public Controller() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		init();
	}

	public Controller(Object request) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		init();
	}
	
	public Controller(Object request,Map<String,List<Entry<Entry<String,Number>, Boolean>>> facets) throws JsonIOException, JsonSyntaxException, FileNotFoundException{
		init();
		this.facets=facets;
	}
	
	
	public void processFacetsRequest(){
		if(facets==null|| facets.size()==0)return;
		Iterator<Entry<String,List<Entry<Entry<String,Number>, Boolean>>>> facetsIter=
																	facets.entrySet().iterator();
		while(facetsIter.hasNext()){
			Entry<String,List<Entry<Entry<String,Number>, Boolean>>> entryfacet=facetsIter.next();
			JsonElement queryFacet=Utiles.seletor("aggs::"+entryfacet.getKey(), query);
			System.out.println("-----"+queryFacet);
			String facetField=Utiles.seletor("terms::field", queryFacet).getAsString();
			System.out.println(facetField);
			Iterator<Entry<Entry<String,Number>, Boolean>> facetValueIter=entryfacet.getValue().iterator();
			
			IBuildJsonArray<IPutProprety<IPutProprety<IRootJsonBuilder>>> arraybuild=
			JsonObjectBuilder.init().begin()
								.putObject("terms")
								.begin()
									.putArray(facetField).begin();
			while(facetValueIter.hasNext()){
				Entry<Entry<String,Number>, Boolean> entryFacetValue=facetValueIter.next();
				arraybuild.putValue(entryFacetValue.getKey().getKey());
			}
			query.add("post_filter", arraybuild.end().end().end().getJsonElement());
			
			System.out.println("%%%%%%%%%%"+query);
		}
	}

	public void connection() {
		jestClient=ElasticClient.getElasticClient(mainConfig.getHost()).getClient();
	}

	public void processRequest(Object request) throws NoSuchMethodException, SecurityException, IllegalAccessException,
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,List<Entry<Entry<String,Number>, Boolean>>> processFirstFacetList(){
		
		List<String> conf=mainConfig.getFacets();
		Iterator<String> confIter= conf.iterator();
		Map<String,List<Entry<Entry<String,Number>, Boolean>>> result=
				new HashMap<String, List<Entry<Entry<String,Number>,Boolean>>>();
		
		while(confIter.hasNext()){
			
			String facetName=confIter.next();
			////////System.out.println(facetName);
			JsonElement facetResult=Utiles.seletor("aggregations::"+facetName+"::buckets",jestResult.getJsonObject());
		    ///////System.out.println(facetResult);
		    Iterator<JsonElement> facetResultIter=facetResult.getAsJsonArray().iterator();
			List<Entry<Entry<String,Number>, Boolean>> facetList=
					new LinkedList<Map.Entry<Entry<String,Number>,Boolean>>();
			while(facetResultIter.hasNext()){
				JsonObject jso=facetResultIter.next().getAsJsonObject();
				facetList.add(
						new MyEntry(
								new MyEntry<String, Number>(jso.get("key").getAsString(),
										jso.get("doc_count").getAsNumber()),(Boolean) false));
			}
			
			result.put(facetName,facetList);			
		}
		
		return result;
	} 
	
	public void init() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		JsonObject jo = new JsonParser().parse(new FileReader(new File(JSON_CONFIG_PATH))).getAsJsonObject();
		mainConfig = new MainConfigDAO(jo).getConfig();
		this.query = jo.get("_query").getAsJsonObject();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String args[])
			throws JsonIOException, JsonSyntaxException, NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, IOException, InstantiationException {
		List<Entry<Entry<String,Number>, Boolean>> l=
								new LinkedList<Map.Entry<Entry<String,Number>,Boolean>>();
		HashMap<String, List<Entry<Entry<String,Number>, Boolean>>> h=
								new HashMap<String, List<Entry<Entry<String,Number>,Boolean>>>();
		l.add(new MyEntry(new MyEntry("5", 0), true));
		h.put("test", l);
		Controller c = new Controller(new RequestBean(),h);
		c.processRequest(new RequestBean());
		c.connection();
		System.out.println(c.mainConfig.getRequestMapping());
		System.out.println(c.query);
		c.processFacetsRequest();
		System.out.println(c.processJsonResult().getJsonObject());
		System.out.println(c.processResultObjects());
		System.out.println(c.processFirstFacetList());
	}

}
