import java.sql.*;
import java.io.*;
import java.util.*;

/**
 * A class that connects to PostgreSQL and disconnects.
 * You will need to change your credentials below, to match the usename and password of your account
 * in the PostgreSQL server.
 * The name of your database in the server is the same as your username.
 * You are asked to include code that tests the methods of the MovieTheaterApplication class
 * in a similar manner to the sample RunFilmsApplication.java program.
*/


public class RunMovieTheaterApplication
{
    public static void main(String[] args) {
    	
    	Connection connection = null;
    	try {
    	    //Register the driver
    		Class.forName("org.postgresql.Driver"); 
    	    // Make the connection.
            // You will need to fill in your real username (twice) and password for your
            // Postgres account in the arguments of the getConnection method below.
            connection = DriverManager.getConnection(
                                                     "jdbc:postgresql://cse180-db.lt.ucsc.edu/jzheng43",
                                                     "jzheng43",
                                                     "database04binary");
            
            if (connection != null)
                System.out.println("Connected to the database!");

            /* Include your code below to test the methods of the MovieTheaterApplication class.
             * The sample code in RunFilmsApplication.java should be useful.
             * That code tests other methods for a different database schema.
             * Your code below: */
            MovieTheaterApplication mov = new MovieTheaterApplication(connection);

	    String testPriceCode = "A";
	    int firstMovID = 101;
	    String newMovieName = "Avatar 1";
            int secMovID = 888;
            String newMovieName2 = "Star Wars: A New Hope";
            int maxCount1 = 15;
            int maxCount2 = 99;

	    Integer showings = mov.getShowingsCount(testPriceCode);
	    /*
 	    * Output of getShowingsCount
            * when the parameter thePriceCode is ‘B’.
            */
	    System.out.println("/*");
	    System.out.println("* Output of getShowingsCount");
            System.out.println("* when the parameter thePriceCode is ‘B’.");
            System.out.println(showings);
            System.out.println("*/");
            
	    int test1 = mov.updateMovieName(firstMovID, newMovieName);
            /*
            * Output of updateMovieName when theMovieID is 3
            * and newMovieName is ‘Avatar 1’
            */
            System.out.println("/*");
            System.out.println("* Output of updateMovieName when theMovieID is 101");
            System.out.println("* and newMovieID is 'Avatar 1'");
            System.out.println(test1);
	    System.out.println("*/");

            int test2 = mov.updateMovieName(secMovID, newMovieName2);
	    /*
            * Output of updateMovieName when theMovieID is 3
            * and newMovieName is ‘Avatar 1’
            */
            System.out.println("/*");
            System.out.println("* Output of updateMovieName when theMovieID is 888");
            System.out.println("* and newMovieID is 'Star Wars: A New Hope'");
            System.out.println(test2);
            System.out.println("*/");

            int ticket15 = mov.reduceSomeTicketPrices(maxCount1);
            System.out.println("Reduced ticket price: " + ticket15);

            int ticket99 = mov.reduceSomeTicketPrices(maxCount2);
            System.out.println("Reduced ticket price: " + ticket99);

            /*******************
            * Your code ends here */
            
    	}
    	catch (SQLException | ClassNotFoundException e) {
    		System.out.println("Error while connecting to database: " + e);
    		e.printStackTrace();
    	}
    	finally {
    		if (connection != null) {
    			// Closing Connection
    			try {
					connection.close();
				} catch (SQLException e) {
					System.out.println("Failed to close connection: " + e);
					e.printStackTrace();
				}
    		}
    	}
    }
}
