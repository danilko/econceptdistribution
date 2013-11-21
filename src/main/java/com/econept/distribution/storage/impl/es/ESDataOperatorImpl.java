package com.econept.distribution.storage.impl.es;

import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.econept.distribution.object.EventObject;
import com.econept.distribution.storage.DataOperator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ESDataOperatorImpl implements DataOperator
{
	TransportClient mClient;
	ObjectMapper mMapper;
	
	public ESDataOperatorImpl() 
	{
		//Settings lSettings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch").build();
		mClient = new TransportClient();
		mClient = mClient.addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
	
		mMapper = new ObjectMapper();
	}  // ESDataOperatorImpl

	public EventObject readEvent(String pDataType, String pDataIndex) throws Exception 
	{
		 GetResponse lResponse = mClient.prepareGet("messages", pDataType, pDataIndex)
		 	.execute()
			.actionGet();
		
		return mMapper.readValue(lResponse.getSourceAsString(), EventObject.class);
	}  // EventObject readEvent

	public void write(EventObject pObject, String pDataType, String pDataIndex)
			throws Exception 
	{
		mClient.prepareIndex("messages", pDataType, pDataIndex)
				.setSource(mMapper.writeValueAsString(pObject))
				.execute()
				.actionGet();
	}  // void write
	
	public int getAllEventCount(String pDataType)
	{
		CountResponse lResponse = mClient.prepareCount("messages").setTypes(pDataType).execute().actionGet();
		
		return (int)lResponse.getCount();
	}  //  long getAllEventCount
	

	public void removeAllData(String pDataType)
	{
		mClient.prepareDelete().setIndex("messages").setType(pDataType).setId("").execute().actionGet();
	}  // removeAllData
}  // class ESDataOperatorImpl

