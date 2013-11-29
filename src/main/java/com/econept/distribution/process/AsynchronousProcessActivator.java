package com.econept.distribution.process;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.econept.distribution.storage.DataOperator;
import com.econept.distribution.storage.DataStorage;
import com.econept.distribution.storage.impl.BaseDataOperatorImpl;
import com.econept.distribution.storage.impl.BaseDataStorageImpl;
import com.econept.distribution.storage.impl.es.ESDataOperatorImpl;
import com.econept.distribution.storage.impl.es.ESDataStorageImpl;

public class AsynchronousProcessActivator {
	private final static Logger mLogger = LoggerFactory
			.getLogger(AsynchronousProcessActivator.class);

	private String mOriginType;
	private String mDestType;
	private String mImplType;
	private String[] mQueryTerms = { "win", "us", "2012", "election", "president" };
	
	DataOperator mDataOperator;
	DataStorage mDataStorage;
	
	
	public AsynchronousProcessActivator() 
	{
		mDataOperator = null;
	}
	
	public void activate(String pImplType) throws Exception 
	{
		mImplType = pImplType;
		if(mImplType.equalsIgnoreCase("FILE"))
		{
			mOriginType = System.getenv("DATA_DIR_ORIGIN");
			mDestType = System.getenv("DATA_DIR_DEST");
			
			mDataStorage = new BaseDataStorageImpl();

			mDataOperator = getDataOperator();
		}  // if
		else if(mImplType.equalsIgnoreCase("ES"))
		{
			mOriginType = "DATA_DIR_ORIGIN";
			mDestType = "DATA_DIR_DEST";
			
			mDataStorage = new ESDataStorageImpl();
			
			mDataOperator = getDataOperator();
		}  // else if
		else
		{
			mLogger.debug("Unknown implmentation type");
			throw new Exception("Unknown implmentation type");
		}  // else
		
		mDataStorage.setDataType(mOriginType);
		
		int[] lDataSizes = {100, 500, 1000, 5000, 10000};

		int lIteration = 10;

		int[] lProcessSizes = {1, 2, 4, 8, 16};
		
		long [] lTotalElapsedTime = {0, 0, 0, 0, 0};
		
		for (int lSize : lDataSizes) 
		{
			// Generate data set
			genearteDataSet(lSize);
			
			int lMaxProcessLength= lProcessSizes.length;
			// Multi Processes
			for (int lProceessIndex = 0; lProceessIndex < lMaxProcessLength; lProceessIndex++)
			{
				long lAverageElapsedTime = executeSingleReaderProcess(lProcessSizes[lProceessIndex],
						lIteration);
				
				mLogger.info(lProcessSizes[lProceessIndex] +  "Process(es) Average Benchmark Over Iteration "
						+ lIteration + " Over Data Size " + lSize + ": " + lAverageElapsedTime
						+ "ms");
				
				lTotalElapsedTime [lProceessIndex] = lTotalElapsedTime [lProceessIndex] + (lAverageElapsedTime/(long)(lSize));
			} // for
		} // for
		
		int lMaxProcessLength=lProcessSizes.length;
		
		for(int lIndex = 0; lIndex < lMaxProcessLength; lIndex++)
		{
			mLogger.info(lProcessSizes[lIndex] +  "Process(es) Average Benchmark Over Iteration: " + (lTotalElapsedTime [lIndex] / (long)(lDataSizes.length))
					+ "ms");
		}  // for
	} // activate

	public void genearteDataSet(int pDataSize)
	{
		// Generate random event messages
		SingleEventGenertatorProcess lSingleEventGenertatorProcess = new SingleEventGenertatorProcess();
		lSingleEventGenertatorProcess.setEventDataLength(140);
		lSingleEventGenertatorProcess.setEventDataSize(pDataSize);
		lSingleEventGenertatorProcess.setQueryTerm(mQueryTerms);
		lSingleEventGenertatorProcess.setDataOperator(mDataOperator);
		lSingleEventGenertatorProcess.setDataType(mOriginType);
		
		mDataOperator.removeAllData(mOriginType);

		lSingleEventGenertatorProcess.run();
	}  // void generateDataSet
	
	public long executeSingleReaderProcess(int pProcessSize,
			int pIteration) throws Exception {
		long lAverageElapsedTime = 0;
		long lElapsedTime = 0;
		
		for (int lIndex = 0; lIndex < pIteration; lIndex++) 
		{
			mDataOperator.removeAllData(mDestType);

			mDataStorage.populateData();
			
			List<AsynchronousReaderProcess> lProcesses = new ArrayList<AsynchronousReaderProcess>();

			CountDownLatch lCountDownLatch = new CountDownLatch(pProcessSize);
			ExecutorService lExecutor = Executors.newFixedThreadPool(pProcessSize);
			
			for (int lProcessIndex = 0; lProcessIndex < pProcessSize; lProcessIndex++) {
				AsynchronousReaderProcess lProcess = new AsynchronousReaderProcess(
						lCountDownLatch);
				lProcess.setDataStorage(mDataStorage);
				lProcess.setDataType(mDestType);
				lProcess.setQueryTerm(mQueryTerms);
				lProcess.setDataOpeartor(mDataOperator);
				
				lProcesses.add(lProcess);
			} // for

			lElapsedTime = System.currentTimeMillis();
			
			// Execute processes
			for (AsynchronousReaderProcess lProcess : lProcesses) 
			{
				lExecutor.execute(lProcess);
			} // for

			lCountDownLatch.await();
			
			lAverageElapsedTime = lAverageElapsedTime
					+ (System.currentTimeMillis() - lElapsedTime);
			
			lExecutor.shutdown();
			
			// halt until all threads are shutting down
			while(lExecutor.isTerminated()){}
		} // for

		lAverageElapsedTime = lAverageElapsedTime / pIteration;

		return lAverageElapsedTime;
	} // void executeSingleReaderProcess

	public DataOperator getDataOperator() throws Exception
	{
		if(mImplType.equalsIgnoreCase("FILE"))
		{
			return new BaseDataOperatorImpl();
		}  // if
		else if(mImplType.equalsIgnoreCase("ES"))
		{
			return new ESDataOperatorImpl();
		}  // else if
		else
		{
			mLogger.debug("Unknown implmentation type");
			throw new Exception("Unknown implmentation type");
		}  // else
	}
}
