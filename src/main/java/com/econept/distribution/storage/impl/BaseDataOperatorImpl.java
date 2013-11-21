package com.econept.distribution.storage.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.econept.distribution.object.EventObject;
import com.econept.distribution.storage.DataOperator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseDataOperatorImpl implements DataOperator
{
	private final Logger mLogger = LoggerFactory.getLogger(this.getClass().getName());
	
	private ObjectMapper mMapper;
	
	public BaseDataOperatorImpl()
	{
		mMapper = new ObjectMapper();
	}  // BaseDataOperatorImpl
	
	public void write(EventObject pObject, String pDataType, String pDataIndex) throws Exception
	{
		try
		{
			mMapper.writeValue(new File(pDataType + "/" +pDataIndex), pObject);
		}  // try
		catch (Exception pException) 
		{
			mLogger.info("Error read from file");
			mLogger.debug(pException.toString());
			
			throw pException;
		} // catch
	}  // void write
	
	public EventObject readEvent(String pDataType, String pDataIndex)  throws Exception 
	{
		EventObject lObject = null;
		
		try
		{
			lObject = mMapper.readValue(new File(pDataType + "/" +pDataIndex), EventObject.class);
		}  // try
		catch (Exception pException) 
		{
			mLogger.info("Error read from file");
			mLogger.debug(pException.toString());
			
			throw pException;
		} // catch
		
		return lObject;
	}  // EventObject readEvent
	
	public int getAllEventCount(String pDataType)
	{
		File lDirectory = new File(pDataType);
		String [] lList = lDirectory.list();
		
		if(lList != null)
		{
			return lList.length;
		}  // if
		else
		{
			return 0;
		}  // else
	}  // int getAllEventCount
	
	public void removeAllData(String pDataType)
	{
		File lFile = new File(pDataType);

		String[] lFiles = lFile.list();

		for (String lCurrentFile : lFiles) {
			File lTemp = new File(lFile, lCurrentFile);
			lTemp.delete();
		} // for
	}  // void
}  //  class BaseDataOperatorImpl
