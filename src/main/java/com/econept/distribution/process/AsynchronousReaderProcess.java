package com.econept.distribution.process;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.econept.distribution.object.EventObject;
import com.econept.distribution.object.EventDescriptorObject;
import com.econept.distribution.storage.DataOperator;
import com.econept.distribution.storage.DataStorage;
import com.econept.distribution.storage.impl.BaseDataOperatorImpl;
import com.econept.distribution.storage.impl.BaseDataStorageImpl;

public class AsynchronousReaderProcess extends Thread {
	private final Logger mLogger = LoggerFactory.getLogger(this.getClass()
			.getName());

	private boolean mRun;

	private DataOperator mDataOperator;

	private DataStorage mDataStorage;
	private String mDataType;
	private String[] mQueryList;
	
	private CountDownLatch mCountDownLatch=null;
	
	public AsynchronousReaderProcess(CountDownLatch pCountDownLatch) 
	{
		mRun = true;
		mDataOperator = new BaseDataOperatorImpl();
		mCountDownLatch = pCountDownLatch;
		
		mDataType = null;
		mDataStorage = null;
	} // void ReaderProcess

	public void setDataStorage(DataStorage pDataStorage)
	{
		mDataStorage = pDataStorage;
	}  // void setDataStorage
	
	public void setDataOpeartor(DataOperator pDataOperator)
	{
		mDataOperator = pDataOperator;
	}  // void setDataStorage
	
	
	public void setDataType(String pDataType)
	{
		mDataType = pDataType;
	}  // void setDataType
	
	public void run()
	{
		while (mRun) {
			try {
				EventDescriptorObject lObject = mDataStorage.getData();
				
				if (lObject == null) 
				{
					mRun = false;
				} // if

				if (mRun) 
				{
					EventObject lEventObject = mDataOperator.readEvent(lObject
							.getType(), lObject.getIndex());

					String lMessage = lEventObject.getMessage();

					boolean lFind = true;

					for (String lTerm : mQueryList) {
						if (!lMessage.contains(lTerm)) 
						{
							lFind = false;
							break;
						} // if
					} // for
					
					if (lFind) 
					{
						for (String lTerm : SingleEventGenertatorProcess.COMMON_NON_KEYWORD) 
						{
							lMessage = lMessage.replace(lTerm, "");
						} // for
						lEventObject.setMessage(lMessage);
						
						mDataOperator.write(lEventObject,
								mDataType, lObject.getIndex());
					} // if
				}  // if
				
			} // try
			catch (Exception pException) {
				mLogger.error("Thread Interrupted");
				mLogger.debug(pException.toString());

				mRun = false;
			} // catch
		} // while
		
		if(mCountDownLatch != null)
		{
			mCountDownLatch.countDown();
		}  // if
	} // void run

	public void setQueryTerm(String[] pQueryList) 
	{
		mQueryList = pQueryList;
	} // void setQueryTerm
} // class ReaderProcess
