package com.edifixio.amine.config;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
	}
	
	public void resolve(){
		resolveAlias();
		resolveMapping();
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	protected void resolveMapping(){
		Properties result=new Properties();
		Iterator<Entry<Object, Object>> mapIter=this.mapping.entrySet().iterator();
		Entry<Object, Object> entry;
		
		while(mapIter.hasNext()){
			entry=mapIter.next();
			if(entry.getValue().getClass()==String.class)
			 result.put(entry.getKey(), resolveTerm((String) entry.getValue()));
			else 
				 result.put(entry.getKey(), resolveTerm((List<String>) entry.getValue()));
		}
		
		
		
		this.mapping=result;
	}
	
	
	
	protected List<String> resolveTerm(List<String> values){
		List<String> result=new LinkedList<String>();
		Iterator<String> valusIter=values.iterator();
		
		while(valusIter.hasNext()){
			result.add(resolveTerm(valusIter.next()));
		}
		return result;
	}
	
	
	protected String resolveTerm(String value){
		String[] listOfTerm=value.split("::");
		String result="";
		for(String str:listOfTerm){
			result+=(String) "::"+
					(((str.length()>2) && str.subSequence(0,2).equals("$$"))?
							alias.get(str.substring(2))	:	str);
	
			}
		
		return result.substring(2);
		
		}
		
	
	
	protected void resolveAlias(){
		Properties newAlias=new Properties();
		Iterator<Entry<Object, Object>> aliasIter=alias.entrySet().iterator();
		while(aliasIter.hasNext()){
			Entry<Object, Object> entry=aliasIter.next();
			newAlias.put((String)entry.getKey(),recurciveResolveAlias((String)entry.getValue()));
			
		}
		alias=newAlias;

	}
	
	protected String recurciveResolveAlias(String value){
		if(value==null) return "";
		String[] split=value.split("::");
		String[] arrayResult=new String[split.length];
		StringBuilder result=new StringBuilder();
		
		for(int i=0;i<split.length;i++){
			arrayResult[i]=(String) ((split[i].subSequence(0, 2).equals("$$"))?
					recurciveResolveAlias((String)alias.get(split[i].substring(2)))
				:split[i]);
	
		}
		for(String r:arrayResult){
			
				result.append("::");
				result.append(r);
		}
		value=result.toString().substring(2);
		return value;
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "\n" + this.alias;
	}
	
	
	public static void main(String args[]){
		MappingAlias ma=new MappingAlias();
		Properties alias=new Properties();
		Properties map=new Properties();
		alias.put("dd", "cc::pp");
		alias.put("kk", "$$dd::pm");
		alias.put("tim", "$$kk::pp");
		
		map.put("cc", "$$tim::b");
		System.out.println(alias);
		ma.setMapping(map);
		ma.setAlias(alias);
		ma.resolve();	
		System.out.println(ma.alias);
		System.out.println(ma.getMapping());
	}
	

}
