package com.edifixio.jsonFastBuild.ObjectBuilder;


public interface IStartBuildJsonObject<ParentType> {
	public IPutProprety<ParentType> begin();
	public ParentType emptyObject();

}
