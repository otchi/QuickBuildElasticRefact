package com.edifixio.jsonFastBuild.ObjectBuilder;

import com.edifixio.jsonFastBuild.ArrayBuilder.IStartBuildJsonArray;

public interface IPutProprety<ParentType> {
	public IBuildJsonObject<ParentType> putPreprety(String proprety);
	public  ParentType end();
	public IStartBuildJsonObject<IPutProprety<ParentType>>  putObject(String proprety);
	public IStartBuildJsonArray<IPutProprety<ParentType>>  putArray(String proprety);
	public IPutProprety<ParentType> putEmptyObject(String proprety);
	public IPutProprety<ParentType> putEmptyArray(String proprety);
	public IPutProprety<ParentType> putPreprety(String proprety,String value);
	public IPutProprety<ParentType> putPreprety(String proprety,Number value);
	public IPutProprety<ParentType> putPreprety(String proprety,Character value);
	public IPutProprety<ParentType> putPreprety(String proprety,Boolean value);
}
