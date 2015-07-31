package com.edifixio.amine.controller;

import java.util.List;

import com.edifixio.amine.facets.Facet;

public abstract interface IQuickBuilder {
	public List<Facet> getFacets();
	public List<Object> getResponse();
}