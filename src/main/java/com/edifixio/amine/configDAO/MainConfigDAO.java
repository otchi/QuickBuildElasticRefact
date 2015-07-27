package com.edifixio.amine.configDAO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.edifixio.amine.config.MainConfig;
import com.edifixio.amine.utiles.Utiles;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class MainConfigDAO extends ConfigDAO<MainConfig> {
	public static final String _CONF= "_config";
	public static final String _CONF_FACET= _CONF+"::_facets";
	public static final String _CONF_RESPONSE= _CONF+"::_response";
	public static final String _CONF_REQUEST= _CONF+"::_request";
	public static final String _CONF_META_RESPONSE= _CONF+"::_meta_response";
	public static final String _CONF_INDEXES= _CONF+"::_indexes";
	public static final String _CONF_HOST= _CONF+"::_host";

	public MainConfigDAO(JsonObject jsonObject) {
		super(jsonObject);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public MainConfig getConfig() {
		// TODO Auto-generated method stub
		MainConfig mainConfig=new MainConfig();
		mainConfig.setFacets(JsonToArray(
							Utiles.seletor(_CONF_FACET, jsonObject)
										.getAsJsonArray()));
		mainConfig.setResponseMapping(
				new MappingDAO(
							Utiles.seletor(_CONF_RESPONSE,jsonObject)
								.getAsJsonObject()).getConfig());
		
		mainConfig.setRequestMapping(
				new MappingAliasDAO(
							Utiles.seletor(_CONF_REQUEST, jsonObject).getAsJsonObject())
								.getConfig());
		
		mainConfig.setMetaResponceMapping(
				new MappingAliasDAO(
							Utiles.seletor(_CONF_META_RESPONSE, jsonObject)
							.getAsJsonObject())
							.getConfig());
		mainConfig.setIndexes(new IndexesSettingDAO (
							Utiles.seletor(_CONF_INDEXES, jsonObject)
								.getAsJsonObject())
								.getConfig());
		mainConfig.setHost(Utiles.seletor(_CONF_HOST, jsonObject).getAsString());
	
		return mainConfig;
	}

	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		// TODO Auto-generated method stub
		
			JsonParser jsonParser = new JsonParser();
			JsonObject jo = jsonParser
					.parse(new FileReader(new File(
							"/home/amine/workspace" 
									+ "/QuickBuildElastic_Refactoring1"
									+ "/src/resources/model.json")))
					.getAsJsonObject();
			
			System.out.println(new MainConfigDAO(jo).getConfig());
		

	}



}
