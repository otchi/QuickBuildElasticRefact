package com.edifixio.amine.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public  class FirstBuildController extends BuildController {


	public FirstBuildController(JsonObject jsonRequest)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		super(jsonRequest);
		// TODO Auto-generated constructor stub
	}


	public List<Facet> getFacetList() {
		// TODO Auto-generated method stub
		
			super.processFacetList();
			return this.facets;
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "---------------------------------------------------\n"
				+ "---------------------------------------------------\n" + this.mainConfig + this.jestClient + "\n"
				+ this.query + this.jestResult.toString() + "---------------------------------------------------\n";
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
		f.execute();
		System.out.println(f.jestResult.getJsonString());
		f.processFacetList();
		System.out.println(f.getFacetList());
		System.out.println(f.processJsonToObjects().toString());

	}



	

}
