package com.edifixio.amine.config;

import java.util.Properties;

public  class  Mapping{

	private Class<?> BeanClass;
	private Properties mapping;
	
	
	
	public Class<?> getBeanClass() {
		return BeanClass;
	}
	public void setBeanClass(Class<?> beanClass) {
		BeanClass = beanClass;
	}
	public Properties getMapping() {
		return mapping;
	}
	public void setMapping(Properties mapping) {
		this.mapping = mapping;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		//return super.toString();
		return this.BeanClass+"\n"
				+this.mapping;
	}

}
