package com.edifixio.jsonFastBuild.ObjectBuilder;

import com.edifixio.jsonFastBuild.ArrayBuilder.IStartBuildJsonArray;
import com.edifixio.jsonFastBuild.ArrayBuilder.JsonArrayBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonObjectBuilder<ParentType> implements IBuildJsonObject<ParentType>,IRootJsonBuilder,IPutProprety<ParentType>,IStartBuildJsonObject<ParentType>{

	private ParentType parent;
	private JsonObject jsonObject;
	private String property;
	
	
	@SuppressWarnings("unchecked")
	public JsonObjectBuilder(){
		this.parent=(ParentType) this;
		this.jsonObject=new JsonObject();
	}
	public JsonObjectBuilder(ParentType parent){
		this.parent=parent;
		this.jsonObject=new JsonObject();
	}
	

	public IPutProprety<ParentType> begin() {
		// TODO Auto-generated method stub
		return (IPutProprety<ParentType>) this;
	}

	public ParentType emptyObject() {
		// TODO Auto-generated method stub
		//System.out.print(((IRootJsonBuilder)parent).getJsonElement()+"\n");
		return this.parent;
	}

	public IBuildJsonObject<ParentType> putPreprety(String property) {
		// TODO Auto-generated method stub
		this.property=property;
		//System.out.print(((IRootJsonBuilder)parent).getJsonElement()+"\n");
		return this;
	}

	public ParentType end() {
		// TODO Auto-generated method stub
		return this.parent;
	}

	public IStartBuildJsonObject<IPutProprety<ParentType>> putObject() {
		// TODO Auto-generated method stub
		IStartBuildJsonObject<IPutProprety<ParentType>> jsob=new JsonObjectBuilder<IPutProprety<ParentType>>(this);
		this.jsonObject.add(property, ((IRootJsonBuilder)jsob).getJsonElement());
		//System.out.print(((IRootJsonBuilder)parent).getJsonElement());
		return ( IStartBuildJsonObject<IPutProprety<ParentType>>)jsob;
	}

		
	
	
	
	public static IStartBuildJsonObject<IRootJsonBuilder> init(){
		
		return new JsonObjectBuilder<IRootJsonBuilder>();
	}

	public JsonElement getJsonElement() {
		// TODO Auto-generated method stub
		return this.jsonObject;
	}
	public IStartBuildJsonArray<IPutProprety<ParentType>> putArray() {
		// TODO Auto-generated method stub
		IStartBuildJsonArray<IPutProprety<ParentType>> jsab=new JsonArrayBuilder<IPutProprety<ParentType>>(this);
		this.jsonObject.add(property, ((IRootJsonBuilder)jsab).getJsonElement());
		return jsab;
	}
	public IPutProprety<ParentType> putValue(String value) {
		// TODO Auto-generated method stub
		this.jsonObject.addProperty(property, value);
		return this;
	}
	public IPutProprety<ParentType> putValue(Number value) {
		// TODO Auto-generated method stub
		this.jsonObject.addProperty(property, value);
		return this;
	}
	public IPutProprety<ParentType> putValue(Character value) {
		// TODO Auto-generated method stub
		this.jsonObject.addProperty(property, value);
		return this;
	}
	public IPutProprety<ParentType> putValue(Boolean value) {
		// TODO Auto-generated method stub
		this.jsonObject.addProperty(property, value);
		return this;
	}
	public IStartBuildJsonObject<IPutProprety<ParentType>> putObject(
			String proprety) {
		// TODO Auto-generated method stub
		return this.putPreprety(proprety).putObject();
	}
	public IPutProprety<ParentType> putEmptyObject(String proprety) {
		// TODO Auto-generated method stub
		 this.putPreprety(proprety).putObject();
		 return this;
	}
	public IPutProprety<ParentType> putPreprety(String proprety, String value) {
		// TODO Auto-generated method stub
		return this.putPreprety(proprety).putValue(value);
	}
	public IPutProprety<ParentType> putPreprety(String proprety, Number value) {
		// TODO Auto-generated method stub
		return this.putPreprety(proprety).putValue(value);
	}
	public IPutProprety<ParentType> putPreprety(String proprety, Character value) {
		// TODO Auto-generated method stub
		return this.putPreprety(proprety).putValue(value);
	}
	public IPutProprety<ParentType> putPreprety(String proprety, Boolean value) {
		// TODO Auto-generated method stub
		return this.putPreprety(proprety).putValue(value);
	}
	public IStartBuildJsonArray<IPutProprety<ParentType>> putArray(
			String proprety) {
		// TODO Auto-generated method stub
		return this.putPreprety(proprety).putArray();
	}
	public IPutProprety<ParentType> putEmptyArray(String proprety) {
		// TODO Auto-generated method stub
		 this.putPreprety(proprety).putArray();
		 return this;
	}
	
	
	


}
