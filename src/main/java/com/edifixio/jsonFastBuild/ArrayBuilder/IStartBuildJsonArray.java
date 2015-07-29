package com.edifixio.jsonFastBuild.ArrayBuilder;


public interface IStartBuildJsonArray <ParentType>{
	
	public IBuildJsonArray<ParentType> begin();
	public ParentType emptyArray();

}
