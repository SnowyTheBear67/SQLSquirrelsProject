package com.cognixia.tv_tracker;

public class ShowNotFoundException extends Exception {
	
	private static String showName; // holds the name of the show that could not be found
	
	// construct exception to be thrown when shows is not found
	public ShowNotFoundException(String showName)
	{
		super("The show " + showName + " could not be found");
	}

}