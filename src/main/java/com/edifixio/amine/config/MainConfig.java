package com.edifixio.amine.config;

import java.util.List;
import java.util.Properties;



public class MainConfig {


	private String host;
	private Properties indexes;
	private List<String> facets;
	private MappingAlias requestMapping;
	private MappingAlias  metaResponceMapping;
	private Mapping responseMapping;
	
	
	public Properties getIndexes() {
		return indexes;
	}

	public void setIndexes(Properties indexes) {
		this.indexes = indexes;
	}
	
	public MappingAlias  getMetaResponceMapping() {
		return metaResponceMapping;
	}

	public void setMetaResponceMapping(MappingAlias  metaResponceMapping) {
		this.metaResponceMapping = metaResponceMapping;
	}

	public Mapping getResponseMapping() {
		return responseMapping;
	}

	public void setResponseMapping(Mapping responseMapping) {
		this.responseMapping = responseMapping;
	}

	public MappingAlias getRequestMapping() {
		return requestMapping;
	}

	public void setRequestMapping(MappingAlias  requestMapping) {
		this.requestMapping = requestMapping;
	}

	

	public List<String> getFacets() {
		return facets;
	}

	public void setFacets(List<String> facets) {
		this.facets = facets;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		//return super.toString();
		return "*************************************************"+
				"------------------------------------------------"+"\n"+
				this.host+"\n"+
				"------------------------------------------------"+"\n"+
				this.indexes+"\n"+
				"------------------------------------------------"+"\n"+
				this.facets+"\n"+
				"------------------------------------------------"+"\n"+
				this.metaResponceMapping+"\n"+
				"------------------------------------------------"+"\n"+
				this.responseMapping+"\n"+
				"------------------------------------------------"+"\n"+
				this.requestMapping+"\n"
				+"------------------------------------------------"+"\n"+
				"*************************************************";
		
		
	}
}
