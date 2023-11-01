package com.cognixia.tv_tracker;

public class UserNotFoundException extends Exception {
	
	private static String userName; //holds the name of the user that will be displayed for the exception when user is not found
	
	//construct the exception that is thrown when user is not found
	public UserNotFoundException(String userName) {
		
		super("User " + userName + " could not be found");
	}

}
