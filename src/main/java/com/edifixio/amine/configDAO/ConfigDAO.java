package com.edifixio.amine.configDAO;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import com.edifixio.amine.config.Mapping;
import com.edifixio.amine.config.MappingAlias;
import com.edifixio.amine.utiles.Utiles;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class ConfigDAO<Type> {

	protected JsonObject jsonObject;

	public ConfigDAO(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public abstract Type getConfig();

	protected Mapping getClassAndMapping(Mapping mapping) throws ClassNotFoundException {
		// TODO Auto-generated method stub

			mapping.setBeanClass(Class.forName(jsonObject.get("class").getAsString()));
			mapping.setMapping(this.jsonToProprety("mapping"));


		return mapping;
	}

	protected MappingAlias getAlias(MappingAlias mappingAlias) {
	  mappingAlias.setAlias(this.jsonToProprety("alias"));	
	  return mappingAlias;
	}

	protected Properties jsonToProprety(String selector) {
		JsonObject jso=Utiles.seletor(selector,jsonObject).getAsJsonObject();
	
		Iterator<Entry<String, JsonElement>> mappingIter = jso.entrySet().iterator();
		Properties properties = new Properties();

		while (mappingIter.hasNext()) {
			Entry<String, JsonElement> entry = mappingIter.next();
			
			if(entry.getValue().isJsonPrimitive())
				properties.put(entry.getKey(), entry.getValue().getAsString());
			else
				if(entry.getValue().isJsonArray())
					properties.put(entry.getKey(), 
									JsonToArray(
											entry.getValue().getAsJsonArray()));
		}
		
		return properties;

	}
	
	protected static List<String> JsonToArray(JsonArray jsonArray){
		List<String> strList=new LinkedList<String>();
		Iterator<JsonElement> jsIter=jsonArray.iterator();

		while(jsIter.hasNext()){
			strList.add(jsIter.next().getAsString());
		}
		
		return strList;	
	}
	


}
