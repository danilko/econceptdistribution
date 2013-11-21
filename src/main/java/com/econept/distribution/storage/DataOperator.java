package com.econept.distribution.storage;

import com.econept.distribution.object.EventObject;

public interface DataOperator
{
	public EventObject readEvent(String pDataType, String pDataIndex) throws Exception;
	public void write(EventObject pObject, String pDataType, String pDataIndex) throws Exception;
	
	public int getAllEventCount(String pDataType);
	
	public void removeAllData(String pDataType);
}  // interface DataReader
