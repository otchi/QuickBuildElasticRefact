package com.edifixio.amine.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import com.edifixio.amine.config.MainConfig;
import com.edifixio.amine.config.Mapping;
import com.edifixio.amine.configDAO.MainConfigDAO;
import com.edifixio.amine.utiles.Utiles;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.Search.Builder;

public abstract  class BuildController {
	protected static final String RESULT_SOURCE = "hits::hits";
	protected MainConfig mainConfig;
	protected JsonObject query;
	protected JestClient jestClient;
	protected JestResult jestResult;
	protected List<Facet> facets;

	/******************************* constructer ****************************************************************************/
	public BuildController(JsonObject jsonRequest)
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

	public void execute() throws IOException {
		Properties indexes = mainConfig.getIndexes();
		Iterator<Entry<Object, Object>> indexesIter = indexes.entrySet().iterator();
		Builder builder = new Search.Builder(query.toString());

		while (indexesIter.hasNext()) {
			Entry<Object, Object> entry = indexesIter.next();
		//	System.out.println(entry.getKey());
			builder.addIndex((String) entry.getKey());
		}
		this.jestResult = this.jestClient.execute(builder.build());
	
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

	protected void processFacetList() {
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
			JsonElement facetResult = Utiles.seletor("aggregations::" + facetName + "::buckets",jestResult.getJsonObject());
			Iterator<JsonElement> facetResultIter = facetResult.getAsJsonArray().iterator();

			while (facetResultIter.hasNext()) {
				JsonObject jso = facetResultIter.next().getAsJsonObject();
				facet.getTerms().add(new FacetUnite(jso.get("key").getAsString(), jso.get("doc_count").getAsInt(), true));

			}
			result.add(facet);
		}
		this.facets = result;
	}
	

}
