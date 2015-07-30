package com.edifixio.amine.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import com.edifixio.amine.beans.RequestBean;
import com.edifixio.amine.beans.ResponseBean;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


public class QuickBuilder {
	private BuildController buildController;
	
	
	public QuickBuilder(){
		super();
	}
	
	public QuickBuilder(String requestPath){
				try {
					this.buildController=
							new BuildController(
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
			this.buildController=new BuildController(jsonObject);
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
			
			this.buildController.setFacets(facets);
			this.buildController.setRequest(requestBean);
			this.buildController.connection();
			this.buildController.processRequest();
			this.buildController.processFacetRequest();
			this.buildController.processJsonResult();
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
			return this.buildController.processResultObjects();
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
		return this.buildController.processFacetList();
	}

	public BuildController getBuildController() {
		return buildController;
	}

	public void setBuildController(BuildController buildController) {
		this.buildController = buildController;
	}
	
	
	public static void main(String[] args){
		
		QuickBuilder quickBuilder=new QuickBuilder("/home/amine/workspaceHistory/workspace/"
				+ "QuickBuildElasticRefact/src/resources"
				+ "/model.json");
		quickBuilder.excute(new RequestBean() ,null);
		List<Object> objs=quickBuilder.getResults();
		System.out.println(objs.size());
		for(Object obj:objs){
			ResponseBean r=(ResponseBean)obj;
			System.out.println(r.getVoitureName());
		}
	}
	

}
