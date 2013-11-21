package com.econept.distribution.storage;


import com.econept.distribution.object.EventDescriptorObject;

public interface DataStorage 
{
	public void populateData();
	public void setDataType(String pDataType);
	public EventDescriptorObject getData();
}  // interface DataStorage 
