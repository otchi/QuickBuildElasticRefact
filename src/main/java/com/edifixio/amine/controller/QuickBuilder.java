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

public class QuickBuilder implements IQuickBuilder{
	IterateBuildController buildController=null;
	List<Facet> oldFacets;
	
	public QuickBuilder(String JsonConfigPath,Object requestBean,List<Facet> oldFacets){
		JsonObject jo;
		this.oldFacets=oldFacets;
	
		try {
			jo = new JsonParser().parse(new FileReader(new File("/home/amine/workspaceHistory/workspace/"
					+ "QuickBuildElasticRefact/src/resources"
					+ "/model.json")))
					.getAsJsonObject();
			this.buildController = new IterateBuildController(jo);
			this.buildController.setRequest(requestBean);
			this.buildController.connection();
			this.buildController.processRequest();
			this.buildController.putFilterOfFacetsRequest(this.oldFacets);
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
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public List<Facet> getFacets() {
		// TODO Auto-generated method stub
		return this.buildController.getFacetList(oldFacets);
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
		FirstQuickBuilder fQuickBuilder=new FirstQuickBuilder("/home/amine/Bureau/confQuery/Voiture/query.json");
		List<Facet> factes;
		System.out.println(fQuickBuilder.getResponse());
		System.out.println(factes=fQuickBuilder.getFacets());
		QuickBuilder quickBuilder=new QuickBuilder("/home/amine/workspaceHistory/workspace/"
				+ "QuickBuildElasticRefact/src/resources"
				+ "/model.json", new RequestBean(), factes);
		System.out.println(quickBuilder.getResponse());
		System.out.println(quickBuilder	);
		
		//System.out.println(quickBuilder.getFacets());
	}

}
