package com.edifixio.amine.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.edifixio.amine.config.MainConfig;
import com.edifixio.amine.configDAO.MainConfigDAO;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Controller {
	public static final String JSON_CONFIG_PATH="/home/amine/workspace" 
												+ "/QuickBuildElastic_Refactoring1"
												+ "/src/resources/model.json";
	MainConfig mainConfig;
	public Controller() throws JsonIOException, JsonSyntaxException, FileNotFoundException{
		JsonObject jo = new JsonParser()
				.parse(
					new FileReader(
						new File(JSON_CONFIG_PATH)
							))
				.getAsJsonObject();

mainConfig=new MainConfigDAO(jo).getConfig();
		
	}
	public Controller(Object request,Object response,Object metaResponse) throws JsonIOException, JsonSyntaxException, FileNotFoundException{
	
	}
	
	public static void main(String args[]) throws JsonIOException, JsonSyntaxException,
													FileNotFoundException{
		System.out.println(new Controller().mainConfig.getRequestMapping().getAlias());
	}

}
