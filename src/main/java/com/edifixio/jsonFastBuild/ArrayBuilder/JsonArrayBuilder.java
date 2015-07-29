package com.edifixio.jsonFastBuild.ArrayBuilder;

import com.edifixio.jsonFastBuild.ObjectBuilder.IRootJsonBuilder;
import com.edifixio.jsonFastBuild.ObjectBuilder.IStartBuildJsonObject;
import com.edifixio.jsonFastBuild.ObjectBuilder.JsonObjectBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonArrayBuilder<ParentType> 
									implements IRootJsonBuilder,
												IStartBuildJsonArray<ParentType>, 
												IBuildJsonArray<ParentType>{
	
	private ParentType parent;
	private JsonArray jsonArray;
	
	@SuppressWarnings("unchecked")
	private JsonArrayBuilder(){
		this.jsonArray=new JsonArray();
		this.parent=(ParentType) this;
	}
	
	public JsonArrayBuilder(ParentType parent){
		this.jsonArray=new JsonArray();
		this.parent=parent;
	}
	
	public JsonElement getJsonElement() {
		// TODO Auto-generated method stub
		return this.jsonArray;
	}



	public IBuildJsonArray<ParentType> begin() {
		// TODO Auto-generated method stub
		return (IBuildJsonArray<ParentType>) this;
	}



	public ParentType emptyArray() {
		// TODO Auto-generated method stub
		return this.parent;
	}

	
	public static IStartBuildJsonArray<IRootJsonBuilder> init(){
		
		return new JsonArrayBuilder<IRootJsonBuilder>();
	}

	public IStartBuildJsonObject<IBuildJsonArray<ParentType>> putObject() {
		
		IStartBuildJsonObject<IBuildJsonArray<ParentType>> jsob=new JsonObjectBuilder<IBuildJsonArray<ParentType>>(this);
		this.jsonArray.add(((IRootJsonBuilder)jsob).getJsonElement());
		//System.out.print(((IRootJsonBuilder)parent).getJsonElement());
		return ( IStartBuildJsonObject<IBuildJsonArray<ParentType>>)jsob;
		// TODO Auto-generated method stub
	
	}

	public ParentType end() {
		// TODO Auto-generated method stub
		return this.parent;
	}

	public IStartBuildJsonArray<IBuildJsonArray<ParentType>> putArray() {
		// TODO Auto-generated method stub
		IStartBuildJsonArray<IBuildJsonArray<ParentType>> jsab=new JsonArrayBuilder<IBuildJsonArray<ParentType>>(this);
		this.jsonArray.add(((IRootJsonBuilder)jsab).getJsonElement());
		return jsab;
	}

	public IBuildJsonArray<ParentType> putEmptyObject() {
		// TODO Auto-generated method stub
		this.putArray();
		return this;
	}

	public IBuildJsonArray<ParentType> putEmptyArray() {
		// TODO Auto-generated method stub
		this.putObject();
		return this;
	}

	public IBuildJsonArray<ParentType> putValue(String value) {
		// TODO Auto-generated method stub
		this.jsonArray.add(new JsonParser().parse(value));
		return this;
	}

	public IBuildJsonArray<ParentType> putValue(Number value) {
		// TODO Auto-generated method stub
		this.jsonArray.add(new JsonParser().parse(value.toString()));
		return this;
	}

	public IBuildJsonArray<ParentType> putValue(Character value) {
		// TODO Auto-generated method stub
		this.jsonArray.add(new JsonParser().parse(value.toString()));
		return this;
	}

	public IBuildJsonArray<ParentType> putValue(Boolean value) {
		// TODO Auto-generated method stub
		this.jsonArray.add(new JsonParser().parse(value.toString()));
		return this;
	}



}
