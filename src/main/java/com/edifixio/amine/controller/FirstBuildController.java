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

import com.edifixio.amine.config.MainConfig;
import com.edifixio.amine.config.Mapping;
import com.edifixio.amine.configDAO.MainConfigDAO;
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

public  class FirstBuildController {
	protected static final String RESULT_SOURCE = "hits::hits";
	protected MainConfig mainConfig;
	protected JsonObject query;
	protected JestClient jestClient;
	protected JestResult jestResult;
	protected List<Facet> facets;

	/******************************* constructer ****************************************************************************/
	public FirstBuildController(JsonObject jsonRequest)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		mainConfig = new MainConfigDAO(jsonRequest).getConfig();
		this.query = jsonRequest.get("_query").getAsJsonObject();
	}

	/******************************
	 * connection to jest client
	 **********************************************************************/
	public void connection() {
		jestClient = ElasticClient.getElasticClient(mainConfig.getHost()).getClient();
	}

	/***********************************************
	 * recuperate a json fromate result
	 ************************************************/

	public FirstBuildController execute() throws IOException {
		Properties indexes = mainConfig.getIndexes();
		Iterator<Entry<Object, Object>> indexesIter = indexes.entrySet().iterator();
		Builder builder = new Search.Builder(query.toString());

		while (indexesIter.hasNext()) {
			Entry<Object, Object> entry = indexesIter.next();
			System.out.println(entry.getKey());
			builder.addIndex((String) entry.getKey());
		}
		this.jestResult = this.jestClient.execute(builder.build());
		return this;
	}

	/*******************************************************
	 * process to object folowing a config mapping
	 ********************************/

	public List<Object> processJsonToObjects() throws InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		Mapping responseMapping = mainConfig.getResponseMapping();
		JsonArray source = Utiles.seletor(RESULT_SOURCE, jestResult.getJsonObject()).getAsJsonArray();
		////// System.out.println(responseMapping.getBeanClass());
		/////// System.out.println(source);
		Properties mapping = responseMapping.getMapping();
		Iterator<Entry<Object, Object>> mappingIter = mapping.entrySet().iterator();
		List<Object> result = new LinkedList<Object>();

		for (int i = 0; i < source.size(); i++)
			result.add(responseMapping.getBeanClass().newInstance());

		while (mappingIter.hasNext()) {
			Entry<Object, Object> entry = mappingIter.next();
			String key = (String) entry.getKey();
			String methodeName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
			Iterator<JsonElement> sourceIter = source.iterator();
			String value = (String) entry.getValue();
			String selector = (value.substring(0, 2).equals("$$")) ? "_source::" + value.substring(2) : value;

			if (sourceIter.hasNext()) {
				JsonObject jo = sourceIter.next().getAsJsonObject();

				JsonElement sourceValue = Utiles.seletor(selector, jo);
				Class<?> c = null;
				JsonPrimitive jop = sourceValue.getAsJsonPrimitive();
				if (jop.isString())
					c = String.class;
				if (jop.isNumber())
					c = Number.class;
				if (jop.isBoolean())
					c = Boolean.class;

				Method method = responseMapping.getBeanClass().getMethod(methodeName, c);
				int i = 0;
				if (c == String.class)
					method.invoke(result.get(i++), sourceValue.getAsString());
				if (c == Number.class)
					method.invoke(result.get(i++), sourceValue.getAsNumber());
				if (c == Boolean.class)
					method.invoke(result.get(i++), sourceValue.getAsBoolean());
				while (sourceIter.hasNext()) {
					jo = sourceIter.next().getAsJsonObject();

					sourceValue = Utiles.seletor(selector, jo);
					// System.out.println(result.get(i++));
					// System.out.println(sourceValue);
					if (c == String.class)
						method.invoke(result.get(i++), sourceValue.getAsString());
					if (c == Number.class)
						method.invoke(result.get(i++), sourceValue.getAsNumber());
					if (c == Boolean.class)
						method.invoke(result.get(i++), sourceValue.getAsBoolean());
				}
			}
		}
		return result;
	}

	public void processFacetList() {
		List<String> conf;
		List<Facet> result = new LinkedList<Facet>();
		Facet facet;

		if ((conf = mainConfig.getFacets()) == null)
			this.facets = new ArrayList<Facet>();

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
				facet.add(new FacetUnite(jso.get("key").getAsString(), jso.get("doc_count").getAsInt(), true));

			}
			result.add(facet);
		}
		this.facets = result;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "---------------------------------------------------\n"
				+ "---------------------------------------------------\n" + this.mainConfig + this.jestClient + "\n"
				+ this.query + this.jestResult.toString() + "---------------------------------------------------\n";
	}

	

	public List<Facet> getFacets() {
		return this.facets;
	}

	public static void main(String args[])
			throws JsonIOException, JsonSyntaxException, IOException, InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		JsonObject jo = new JsonParser()
				.parse(new FileReader(new File("/home/amine/Bureau/confQuery/Voiture/query.json"))).getAsJsonObject();
		System.out.println(jo);
		FirstBuildController f = new FirstBuildController(jo);
		System.out.println(f.query);
		f.connection();
		System.out.println(f.jestClient);
		System.out.println(f.execute().jestResult.getJsonString());
		f.processFacetList();
		System.out.println(f.getFacets());
		System.out.println(f.processJsonToObjects().toString());

	}

}
