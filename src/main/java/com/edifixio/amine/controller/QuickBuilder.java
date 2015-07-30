package com.edifixio.amine.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


public class QuickBuilder {
	private IterateBuildController buildController;
	
	
	public QuickBuilder(){
		super();
	}
	
	public QuickBuilder(String requestPath){
				try {
					this.buildController=
							new IterateBuildController(
									new JsonParser()
									.parse(new FileReader(new File(requestPath)))
									.getAsJsonObject());
				} catch (JsonIOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	public QuickBuilder(JsonObject jsonObject){
		try {
			this.buildController=new IterateBuildController(jsonObject);
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void firstExecute(){
		
	}
	
	public void excute(Object requestBean,List<Facet> facets){
		
		try {
			
			
			this.buildController.setRequest(requestBean);
			this.buildController.connection();
			this.buildController.processRequest();
			this.buildController.putFilterOfFacetsRequest();
			this.buildController.execute();
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
	
	public List<Object> getResults(){
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
		
		return new LinkedList<Object>();
	}
	
	public List<Facet> getNewFacet(){
		//return this.buildController.processFacetList();
		return null;
	}

	public IterateBuildController getBuildController() {
		return buildController;
	}

	public void setBuildController(IterateBuildController buildController) {
		this.buildController = buildController;
	}
	
	
/*	public static void main(String[] args){
		
		QuickBuilder quickBuilder=new QuickBuilder("/home/amine/workspaceHistory/workspace/"
				+ "QuickBuildElasticRefact/src/resources"
				+ "/model.json");
		quickBuilder.excute(new RequestBean() ,null);
		List<Object> objs=quickBuilder.getResults();
		System.out.println(objs.size());
		for(Object obj:objs){
			ResponseBean r=(ResponseBean)obj;
			//System.out.println(r.getVoitureName());
		}
	}*/
	

}
