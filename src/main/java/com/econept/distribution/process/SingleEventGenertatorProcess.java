package com.econept.distribution.process;

import java.security.SecureRandom;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.econept.distribution.object.EventObject;
import com.econept.distribution.storage.DataOperator;
import com.econept.distribution.storage.impl.BaseDataOperatorImpl;

public class SingleEventGenertatorProcess {
	private final Logger mLogger = LoggerFactory.getLogger(this.getClass()
			.getName());

	private boolean mRun;

	private SecureRandom mRandom;
	
	private int mEventDataSize;
	private int mEventDataLength;

	private String mDataType;
	private DataOperator mDataOperator;

	public final static String[] COMMON_NON_KEYWORD = { "but", "a", "as",
			"there", "is", "was", "are", "were", "will", "would", "many" };
	
	private final static String[] RANDOM_KEYWORD = { "er", "bill", "cd",
		"sad", "wasdfs", "random", "wdx", "saaas", "mental", "eagle", "test" };
	
	private String[] mQueryList;

	public SingleEventGenertatorProcess() {
		mRun = true;
		mRandom=new SecureRandom();
	} // void ReaderProcess

	public void setDataOperator(DataOperator pDataOperator)
	{
		mDataOperator = pDataOperator;
	}  // void setDataOperator
	
	
	public void setDataType(String pDataType)
	{
		mDataType = pDataType;
	}  // void setDataType
	
	
	public void run() {
		int lCurrentSize = 0;
		while (mRun) 
		{
			try 
			{
				if(lCurrentSize > mEventDataSize)
				{
					mRun = false;
					continue;
				}  // if
				
				EventObject lEventObject = new EventObject();

				try {
					
					lEventObject.setMessage(getRandomMessage());
					lEventObject.setDate(new Date());
					lEventObject.setLocation("1");
					
					mDataOperator
							.write(lEventObject, mDataType, Integer.toString(lCurrentSize));
					
					lCurrentSize++;
				} // try
				catch (Exception pException) {
					mLogger.info("Unable to save Event");
					mLogger.debug(pException.toString());

					throw pException;
				} // catch
			} // try
			catch (Exception pException) {
				mLogger.error("Thread Interrupted");
				mLogger.debug(pException.toString());

				mRun = false;
			} // catch
		} // while
	} // void run

	public void setQueryTerm(String[] pQueryList) {
		mQueryList = pQueryList;
	} // void setQueryTerm

	public String [] getQueryTerm() 
	{
		return mQueryList;
	} // String setQueryTerm
	
	public void setEventDataSize(int pEventDataSize) 
	{
		mEventDataSize = pEventDataSize;
	} // pEventDataSize
	
	public int getEventDataSize() 
	{
		return mEventDataSize;
	} // pEventDataSize
	
	public void setEventDataLength(int pEventDataLength) 
	{
		mEventDataLength = pEventDataLength;
	} // pEventDataLength
	
	public int getEventDataLength() 
	{
		return mEventDataLength;
	} // pEventDataLength
	
	public String getRandomMessage()
	{
		StringBuilder lBuilder = new StringBuilder();
		
		int lCurrentLength = 0;
		
		while(true)
		{
			int lKedwordTyepeRandom = mRandom.nextInt(3);
			
			int lKeywordRandom = 0;
			String lKeyword = null;
			if(lKedwordTyepeRandom == 0)
			{
				 lKeywordRandom = mRandom.nextInt(COMMON_NON_KEYWORD.length);
				 lKeyword=COMMON_NON_KEYWORD[lKeywordRandom];
			}  // if
			else if(lKedwordTyepeRandom == 1)
			{
				lKeywordRandom = mRandom.nextInt(mQueryList.length);
				lKeyword=mQueryList[lKeywordRandom];
			}  // else if
			else
			{
				lKeywordRandom = mRandom.nextInt(RANDOM_KEYWORD.length);
				lKeyword=RANDOM_KEYWORD[lKeywordRandom];
			}  // else
			
			if((lCurrentLength + lKeyword.length() + 1 ) < mEventDataLength)
			{
				lCurrentLength = lCurrentLength + lKeyword.length();
				
				lBuilder.append(" ");
				lBuilder.append(lKeyword);
			}  // if
			else
			{
				break;
			}   // else
		}  // while
		
		return lBuilder.toString();
	} // pEventDataSize
}
