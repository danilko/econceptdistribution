package com.econept.distribution.object;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventObject 
{
	@JsonProperty("location")
	private String mLocation;
	@JsonProperty("date")
	private Date mDate;
	@JsonProperty("message")
	private String mMessage;
	
	public EventObject()
	{
		mLocation="";
		mDate=new Date();
		mMessage="";
	}  // EventObject
	
	public EventObject(EventObject pObject)
	{
		mLocation=pObject.mLocation;
		mDate=pObject.mDate;
		mMessage=pObject.mMessage;
	}  // EventObject
	
	public String getMessage()
	{
		return mMessage;
	}  // String getMessage
	
	public void setMessage(String pMessage)
	{
		mMessage = pMessage;
	}  // String setMessage
	
	public String getLocation()
	{
		return mLocation;
	}  // String getLocation
	
	public void setLocation(String pLocation)
	{
		mLocation= pLocation;
	}  // String setLocation
	
	public Date getDate()
	{
		return mDate;
	}  // String getDate
	
	public void setDate(Date pDate)
	{
		mDate = pDate;
	}  // String setDate
}  // class EventObject 
