package com.edifixio.amine.controller;

import java.util.List;

public abstract interface IQuickBuilder {
	public List<Facet> getFacets();
	public List<Object> getResponse();
}