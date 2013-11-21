package com.econept.distribution.storage.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.econept.distribution.object.EventDescriptorObject;
import com.econept.distribution.storage.DataOperator;
import com.econept.distribution.storage.DataStorage;

public class BaseDataStorageImpl implements DataStorage
{
	private final Logger mLogger = LoggerFactory.getLogger(this.getClass().getName());
	
	private ArrayList <EventDescriptorObject> mEventDescriptorList;
	
	private final static Object SYNCHRONIZED_DESCRIPTOR_BLOCK_OBJECT = new Object();
	private String mDataType;
	private DataOperator mOperator;
	
	public BaseDataStorageImpl() 
	{
		mEventDescriptorList = new ArrayList <EventDescriptorObject>();
		mOperator = new BaseDataOperatorImpl();
	}  // BaseDataStorage
	
	public void populateData()
	{
		synchronized(SYNCHRONIZED_DESCRIPTOR_BLOCK_OBJECT)
		{
			int lTotalCount = mOperator.getAllEventCount(mDataType);
			
			for(int lIndex = 0; lIndex < lTotalCount; lIndex++)
			{
				EventDescriptorObject lObject = new EventDescriptorObject();
				lObject.setIndex(Integer.toString(lIndex));
				lObject.setType(mDataType);
				
				mEventDescriptorList.add(lObject);
			}  // for
		}  // synchronized
	}  // String void populateData
	
	public void setDataType(String pDataType)
	{
		mDataType = pDataType;
	}  // void setDataType
	
	public EventDescriptorObject getData()
	{
		synchronized(SYNCHRONIZED_DESCRIPTOR_BLOCK_OBJECT)
		{
			if(mEventDescriptorList.size() > 0)
			{
				return mEventDescriptorList.remove(0);
			}  // if
		}  // synchronized
		
		return null;
	}  // String void populateData
}  // BaseDataStorageImpl
