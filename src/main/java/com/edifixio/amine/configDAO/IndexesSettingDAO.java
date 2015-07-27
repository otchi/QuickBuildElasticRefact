package com.edifixio.amine.configDAO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;

import com.edifixio.amine.utiles.Utiles;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class IndexesSettingDAO extends ConfigDAO<Properties> {

	public IndexesSettingDAO(JsonObject jsonObject) {
		super(jsonObject);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Properties getConfig() {
		// TODO Auto-generated method stub
		return this.jsonToProprety(null);
	}

	public static void main(String args[]) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		JsonParser jsonParser = new JsonParser();
		JsonObject jo = jsonParser
				.parse(new FileReader(new File(
						"/home/amine/workspace" 
								+ "/QuickBuildElastic_Refactoring1"
								+ "/src/resources/model.json")))
				.getAsJsonObject();

		System.out.println(new IndexesSettingDAO(Utiles.seletor(MainConfigDAO._CONF_INDEXES, jo).getAsJsonObject())
				.getConfig());
	}

}
