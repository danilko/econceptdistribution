package com.econept.distribution.object;

public class EventDescriptorObject 
{
	private String mType;
	private String mIndex;
	
	public EventDescriptorObject() 
	{
		mType =null;
		mIndex=null;
	}  // FileObject

	public EventDescriptorObject(EventDescriptorObject pObject) 
	{
		setIndex(pObject.mIndex);
		setIndex(pObject.mType);
	}  // FileObject
	
	public String getType() {
		return mType;
	}

	public void setType(String pType) 
	{
		mType = pType;
	}  // void setType

	public String getIndex() 
	{
		return mIndex;
	}  // String getInde

	public void setIndex(String pIndex) {
		mIndex = pIndex;
	}  // void setIndex
}  //  class FileObject
