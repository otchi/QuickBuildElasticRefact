package com.edifixio.amine.config;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

public class MappingAlias extends Mapping {
	private Properties alias;

	public MappingAlias() {
		super();
	}

	public Properties getAlias() {
		return alias;
	}

	public void setAlias(Properties alias) {
		this.alias = alias;
		resolveAlias();
	}
	
	protected Properties resolveAlias(){
		resolveMappingAlias();
		return alias;
		
	}
	
	protected void resolveMappingAlias(){
		Properties newAlias=new Properties();
		Iterator<Entry<Object, Object>> aliasIter=alias.entrySet().iterator();
		while(aliasIter.hasNext()){
			Entry<Object, Object> entry=aliasIter.next();
			newAlias.put((String)entry.getKey(),resolve((String)entry.getValue()));
			
		}
		alias=newAlias;

	}
	
	protected String resolve(String value){
		if(value==null) return "";
		String[] split=value.split("::");
		String[] arrayResult=new String[split.length];
		StringBuilder result=new StringBuilder();
		
		for(int i=0;i<split.length;i++){
			arrayResult[i]=(String) ((split[i].subSequence(0, 2).equals("$$"))?
					resolve((String)alias.get(split[i].substring(2)))
				:split[i]);
	
		}
		for(String r:arrayResult){
			
				result.append("::");
				result.append(r);
		}
		value=result.toString().substring(2);;
	
		
		return value;
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		// return super.toString();
		return super.toString() + "\n" + this.alias;
	}
	
	
	public static void main(String args[]){
		MappingAlias ma=new MappingAlias();
		Properties alias=new Properties();
		alias.put("dd", "cc::pp");
		alias.put("kk", "$$dd::pm");
		alias.put("tim", "$$kk::pp");
		System.out.println(alias);
		ma.setAlias(alias);
		ma.resolveAlias();
		System.out.println(ma.alias);
	}
	

}
