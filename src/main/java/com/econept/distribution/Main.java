/**
 * 
 */
package com.econept.distribution;

import com.econept.distribution.process.AsynchronousProcessActivator;

/**
 * @author DANILKO
 * 
 */
public class Main {


	/**
	 * @param pArgument
	 * @throws Exception
	 */
	public static void main(String[] pArgument) throws Exception {
		AsynchronousProcessActivator lActivator = new AsynchronousProcessActivator();
		//lActivator.activate("FILE");
		lActivator.activate("ES");
		
		System.exit(0);
	} // void main
} // class Main
