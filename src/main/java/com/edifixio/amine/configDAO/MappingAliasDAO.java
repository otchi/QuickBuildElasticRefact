package com.edifixio.amine.configDAO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.edifixio.amine.config.MappingAlias;
import com.edifixio.amine.utiles.Utiles;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class MappingAliasDAO extends ConfigDAO<MappingAlias>{

	public MappingAliasDAO(JsonObject jsonObject) {
		super(jsonObject);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MappingAlias getConfig() {
		// TODO Auto-generated method stub
		MappingAlias mappingAlias = new MappingAlias();
		try {
			mappingAlias =  (MappingAlias) this.getClassAndMapping(mappingAlias);
			mappingAlias =  this.getAlias(mappingAlias);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mappingAlias;
	
	}
	
	public static void main(String args[]) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		JsonParser jsonParser = new JsonParser();
		JsonObject jo = jsonParser
				.parse(new FileReader(new File(
						"/home/amine/workspace" 
								+ "/QuickBuildElastic_Refactoring1"
								+ "/src/resources/model.json")))
				.getAsJsonObject();

		System.out.println(new MappingAliasDAO(Utiles.seletor(MainConfigDAO._CONF_REQUEST, jo).getAsJsonObject())
				.getConfig());
	}

}
