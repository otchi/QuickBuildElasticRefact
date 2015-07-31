package com.edifixio.amine.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.edifixio.amine.beans.RequestBean;
import com.edifixio.amine.facets.Facet;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class FirstQuickBuilder implements IQuickBuilder{
	private FirstBuildController buildController=null;
	public FirstQuickBuilder(String JsonConfigPath){
		JsonObject jo;
		try {
			jo = new JsonParser().parse(new FileReader(new File(JsonConfigPath)))
					.getAsJsonObject();
			this.buildController=new FirstBuildController(jo);
			this.buildController.connection();
			this.buildController.execute();	
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Facet> getFacets() {
		// TODO Auto-generated method stub
		return this.buildController.getFacetList();
	}
	public List<Object> getResponse() {
		// TODO Auto-generated method stub
		try {
			return this.buildController.processJsonToObjects();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){
		FirstQuickBuilder quickBuilder=new FirstQuickBuilder("/home/amine/Bureau/confQuery/Voiture/query.json");
		System.out.println(quickBuilder.getResponse());
		System.out.println(quickBuilder.getFacets());
	}

}
