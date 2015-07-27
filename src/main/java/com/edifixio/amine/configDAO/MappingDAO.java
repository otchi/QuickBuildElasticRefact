package com.edifixio.amine.configDAO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.edifixio.amine.config.Mapping;
import com.edifixio.amine.utiles.Utiles;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class MappingDAO extends ConfigDAO<Mapping> {

	public MappingDAO(JsonObject jsonObject) {
		super(jsonObject);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Mapping getConfig() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Mapping mapping = new Mapping();
		try {
			return this.getClassAndMapping(mapping);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapping;

	}

	public static void main(String args[]) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		JsonParser jsonParser = new JsonParser();
		JsonObject jo = jsonParser
				.parse(new FileReader(new File(
						"/home/amine/workspace" 
								+ "/QuickBuildElastic_Refactoring1"
								+ "/src/resources/model.json")))
				.getAsJsonObject();

		System.out.println(new MappingDAO(Utiles.seletor(MainConfigDAO._CONF_RESPONSE, jo).getAsJsonObject())
				.getConfig());
	}

}
