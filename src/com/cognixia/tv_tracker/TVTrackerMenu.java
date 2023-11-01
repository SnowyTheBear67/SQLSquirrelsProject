package com.cognixia.tv_tracker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TVTrackerMenu {

		private static Scanner sc;
		
		private static WatchListDao watchListDao = new WatchListDaoImpl();
		
		//function to validate user		
		public static void userValidation() {
			
			
			//create temp strings to hold the input username, 
			String inputUser;
			
			String inputPassword;
			
			sc = new Scanner(System.in);
			 
			//boolean to control while loop
			boolean validLogin = false;
			//prompt user for Username & Password
			do{
			System.out.println("**************************************");
			System.out.println("******* Welcome to TV Tracker ********");
			System.out.println("**************************************");
			System.out.println("Please log in to view your saved shows");
			System.out.println("**************************************");
			System.out.println("Please enter your username: ");
			inputUser = sc.nextLine();
			
			System.out.println("Please enter your password: ");
			inputPassword = sc.nextLine();
			
			
			//create a counter so the user is only prompted for a valid username and password 3 times.
			int counter = 1;
			
			int userId;
			
			
			//Now pass that information to the DAO implementation class
			
			try {
				userId = watchListDao.validateUserPass(inputUser, inputPassword);
			} catch (UserNotFoundException e) {
				System.out.println("That Username does not exist.");
				userId = -1;
				e.printStackTrace();
			}
			
			if (userId > 0) {
				
				System.out.println("**************************************");
				System.out.println("********** Login Successful **********");
				System.out.println("**************************************");
				
				TVTrackerMenu.mainMenu(userId);
				
				validLogin = true;
			} else {
				
				System.out.println("**************************************");
				System.out.println("************ Login Failed ************");
				System.out.println("**************************************");
				
				if(counter >=3) {  
					
					validLogin = true;  //it isn't, but this will prevent more than 3 attempts
				} else {
				
					counter++;
					TVTrackerMenu.userValidation();
				}
			}	
			
			}while(!validLogin);
			
			return;
			
			
		}
		
		public static void mainMenu(int userId) {   
			
			try {
				
				watchListDao.establishConnection();
			
			} catch (ClassNotFoundException | SQLException e1) {
				
				System.out.println("\nCould not connect to the TV Tracker Database, application cannot run at this time.");
			}
			
			sc = new Scanner(System.in);
			
			System.out.println("Welcome to the TV Application!");
			
			boolean exit = false;
			
			while(!exit) {
				
				System.out.println("**************************************");
				System.out.println("******* Welcome to TV Tracker ********");
				System.out.println("**************************************");
				System.out.println("************* Main Menu **************");
				System.out.println("**************************************");
				System.out.println("\nWhat would you like to do?");
				System.out.println("1. List all My Shows");
				System.out.println("2. List all My Shows by Status");
				System.out.println("3. Add a Show to My List");
				System.out.println("4. Update Show Status");
				System.out.println("5. Remove a Show from My List");
				System.out.println("6. View All Shows to choose from ");
				System.out.println("7. View All Shows by in a Specific Genre");				
				System.out.println("8. Exit");
				
				int input = sc.nextInt();
				sc.nextLine(); // will stop issue with an infinite loop w/ scanner (new line character can get stuck in buffer)
				
				switch (input) {
				
				case 1:
					getAllMyShows(userId);
					break;
				case 2:
					getAllMyShowsStat(userId);
					break;
					
				case 3:
					addShow(userId);
					break;
				case 4:
					updateShowStatus(userId);
					break;
				case 5:
					deleteShow(userId);
					break;
				case 6:
					getAllShows();
					break;
				case 7:
					getAllShowsGenre();
					break;	
				case 8:
					exit = true;
					break;
				default:
					System.out.println("\nPlease enter an option listed (number 1 - 8)");
					break;
				}
			}		
			
		
			System.out.println("\n\nGoodBye!");
			
			// once we exit, will close the scanner
			sc.close();
			
			try{
				watchListDao.closeConnection();
			} catch (SQLException e) {
				System.out.println("Could not close connection properly");
			}
			
		
		}
		
		public static void updateShowStatus(int userId) {		
		
			//first we need to get the show
			System.out.println("Which show would you like to update?");
				
			String userShow = sc.nextLine();
				
			System.out.println("What is the show's new status?");
			System.out.println("1 - Planning to Watch");
			System.out.println("2 - Currently Watching");
			System.out.println("3 - Completed Watching");
				
			int showStatus = sc.nextInt();
			sc.nextLine();
				
			if(watchListDao.updateShowStatus(userId, userShow, showStatus)) {
					
				System.out.println("Your show status is updated");
									
			} else {
					
				System.out.println("We were unable to update your show, returning to Main Menu");
			}
				
				
			return;
				
				
		}	
		
		private static void getAllShows() {
			//create a list to receive the results 
			
			List<Shows> allShows = new ArrayList<>();
				
			//call DAO method 
					
			allShows = watchListDao.getAllShows();
					
			for(int i = 0; i < allShows.size(); i++) { 
						
				System.out.println(allShows.get(i).toString());
			}
					
			return;
				
		}
		
		private static void getAllShowsGenre() {

			//Prompt for Genre
			System.out.println("Which Genre would you like listed?");
			System.out.println("Action, Comedy, or Fantasy?");
					
			String genre = sc.nextLine();
					
			//Now create a Show list to hold the return
					
			List<Shows> genreShows = new ArrayList<>();
					
			genreShows = watchListDao.getAllShows(genre);
					
			//Now output 
					
			for(int i = 0; i < genreShows.size(); i++) { 
				System.out.println(genreShows.get(i).toString());
			}
					
			return;
					
				
		}	
		
		private static void deleteShow(int userId) {
			
			System.out.println("Please enter the name of the show you would like to delete:");
			
			String tempShow = sc.nextLine();
							
			//delete the show using DAO
			if(watchListDao.deleteShow(userId,tempShow)) {
				System.out.println("We were able to delete " + tempShow + " from your list.");
			} else {
						
						System.out.println("We were unable to delete the record.");
						System.out.println("Returning to Main Menu");
						
					}
					
					return;
					
				
			}
		
		private static void addShow(int userId) {
			//prompt for values for the show
			
			System.out.println("What Show would you like to add to your list?");
			
			String tempShow = sc.nextLine();
			
			//use the DAO to add the new show
			
			try {
				if(watchListDao.addShow(userId,tempShow)) {
					System.out.println("We have added " + tempShow + " to your list." );
					System.out.println("Returning to Main Menu");		
				} else {
					System.out.println("We were unable to add that show to your list.");
					System.out.println("Returning to Main Menu");
				}
			} catch (ShowNotFoundException e) {
				System.out.println("We were unable to find that show to your list.");
				e.printStackTrace();
			}
			
			return;

		}
		
		private static void getAllMyShows(int userId) {
					
			//create the Show List to hold the return
			List<Shows> myShows = new ArrayList<>();
					
			//get list from DAO
					
			myShows = watchListDao.getAllMyShows(userId);
					
			for(int i = 0; i < myShows.size(); i++) { 	
				System.out.println(myShows.get(i).toString());
			}
						
		}
		
		private static void getAllMyShowsStat(int userId) {
					
					
			//create a holding list
			List<Shows> genreList = new ArrayList<>();
					
			System.out.println("Which watching status would you like to review?");
			System.out.println("1 - Planning to Watch");
			System.out.println("2 - Currently Watching");
			System.out.println("3 - Completed Watching");
					
			int tempStatus = sc.nextInt();
			sc.nextLine();
					
					
			//retrieve the show listings
			genreList = watchListDao.getAllMyShows(userId, tempStatus);
					
			//Now print the list
			for (int i = 0; i < genreList.size(); i++) {
				System.out.println(genreList.get(i).toString());
			}
			
			return;

		}
		

}
