
public class Marketer extends Employee {
	Marketer(int yearsWorked){ // constructor
		super(yearsWorked);
	}
	final double CLIENT_BONUS = 2000; // per new client
	final int PAID_VACATION = 10; // days
	final int UNPAID_VACATION = 10; // days
	final double HEALTH_INSURANCE = 10000; // $/year
	final double INCOME = 100000; // $/year
	int numBonuses; // keeps track of how many commission
	// based bonuses the employee received.
	int usedUnpaidVacation; // keeps track of how much
	//unpaid vacation has been used.
	int usedVacation; // keeps track of how much paid vacation
	// has been used.
	void signDeal() {
		// adds one to numBonuses.
		numBonuses++;
	}
	void usePaidVacation() { // adds one to usedVacation
		 //instance variable.
		usedVacation++;
	}
	void useUnpaidVacation() { // adds one to
	// usedUnpaidVacation
		usedUnpaidVacation++;
	}
	void workYear() { // adds one to yearsWorked.
		setYears(getYears() + 1);
	}
	double YTDValue() { /* overridden from Employee class.
	This is calculated by taking the sum of:
	HEALTH_INSURANCE
	INCOME
	PAID_VACATION * (INCOME / 260)
	*/
		return (CLIENT_BONUS * numBonuses + HEALTH_INSURANCE + INCOME+  PAID_VACATION * (INCOME / 260));
	}
	int yearsTillRetirement() { /* Overridden from
	Employee class. This should be rounded up to the nearest int.
	The calculation should take place as floating point arithmetic to
	ensure precision (hint: cast as double where necessary). This
	number should not be below zero. This is calculated by: 35 –
	(yearsWorked + (usedUnpaidVacation / 260) +
	((usedPaidVacation / 260) * 2))
	*/
		float y = (float)(35 - (getYears() + (usedUnpaidVacation / 260) + ((usedVacation / 260) * 2)));
		return (int)y;
	}
}
