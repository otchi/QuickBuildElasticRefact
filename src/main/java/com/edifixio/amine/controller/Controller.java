package com.edifixio.amine.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import com.edifixio.amine.beans.RequestBean;
import com.edifixio.amine.config.MainConfig;
import com.edifixio.amine.config.MappingAlias;
import com.edifixio.amine.configDAO.MainConfigDAO;
import com.edifixio.amine.utiles.Utiles;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.Search.Builder;

public class Controller {
	public static final String JSON_CONFIG_PATH = "/home/amine/workspace" + "/QuickBuildElasticRefact"
			+ "/src/resources/model.json";
	public static final String RESULT_SOURCE="hits::hits" ;
	MainConfig mainConfig;
	JsonObject query;
	JestClient jestClient;
	JestResult jestResult;

	public Controller() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		init();
	}

	public Controller(Object request) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		init();
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
	
	public JestResult getResultSourse() throws IOException{
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
	
	public List<Object> getResultObject() throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException{
		JsonArray source=Utiles.seletor(RESULT_SOURCE, jestResult.getJsonObject()).getAsJsonArray();
		System.out.println(source);
		MappingAlias responseMapping=mainConfig.getMetaResponceMapping();
		Properties mapping=responseMapping.getMapping();
		Iterator<Entry<Object, Object>> mappingIter=mapping.entrySet().iterator();
		List<Object> result=new LinkedList<Object>();
		for(int i=0;i<source.size();i++)
			result.add(responseMapping.getClass().newInstance());
		
		while(mappingIter.hasNext()){
			Entry<Object, Object> entry=mappingIter.next();
			String key=(String) entry.getKey();
			String methodeName="set"+key.substring(0, 1).toUpperCase()+key.substring(1);
			System.out.println(methodeName);
			Method method=responseMapping.getClass().getMethod(methodeName, String.class);
			
			
		}
		
		
		return result;
	}

	public void init() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		JsonObject jo = new JsonParser().parse(new FileReader(new File(JSON_CONFIG_PATH))).getAsJsonObject();
		mainConfig = new MainConfigDAO(jo).getConfig();
		this.query = jo.get("_query").getAsJsonObject();
	}

	public static void main(String args[])
			throws JsonIOException, JsonSyntaxException, NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, IOException, InstantiationException {
		Controller c = new Controller(new RequestBean());
		c.processRequest(new RequestBean());
		c.connection();
		System.out.println(c.mainConfig.getRequestMapping());
		System.out.println(c.query);
		System.out.println(c.getResultSourse().getJsonObject());
		c.getResultObject();
	}

}
