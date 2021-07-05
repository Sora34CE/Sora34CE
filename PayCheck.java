// PayCheck.java
// Jason Zheng
// CruzID: jzheng43
// Program 1 - Paycheck Calculator
// Takes the hours worked per week and pay rate to calculate the paycheck.

import java.util.*;
import java.text.DecimalFormat;
public class PayCheck {

	public static void main(String[] args) {
		Scanner scnr = new Scanner(System.in);
		
		//Establish variables
		System.out.println("Please enter the total hours worked from Monday to Sunday: ");
		double totalHours = scnr.nextDouble();
		System.out.println("Please enter your pay rate in dollars per hour: ");
		double payRate = scnr.nextDouble();
		
		//Calculate paycheck
		double regularPay = 0.0;
		double overTimePay = 0.0;
		double payCheck = 0.00;
		if (totalHours <= 40.00) {
			regularPay = totalHours * payRate;
		}
		else {
			regularPay = 40.00 * payRate;
			overTimePay = (totalHours - 40.00) * payRate * 1.5;
		}
		payCheck = regularPay + overTimePay;
		
		//Round decimals
		DecimalFormat df = new DecimalFormat("##.00");
		
		//Final check
		System.out.println("Your paycheck for this week is: " + (df.format(payCheck)));
	}

}
