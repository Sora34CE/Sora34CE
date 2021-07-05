
public class Intern extends Employee{
	Intern(int yearsWorked) { // constructor
		super(yearsWorked);
	}
	final int UNPAID_VACATION = 10; // days
	final double HEALTH_INSURANCE = 5000; // $/year
	final double INCOME = 40000; // $/year
	int usedUnpaidVacation; // keeps track of how much
	//unpaid vacation has been used.
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
		return (HEALTH_INSURANCE + INCOME);
	}
	int yearsTillRetirement() { /* Overridden from
	Employee class. This should be rounded up to the nearest int.
	The calculation should take place as floating point arithmetic to
	ensure precision (hint: cast as double where necessary). This is
	calculated by: 35 – (yearsWorked + (usedUnpaidVacation
	/ 260))
	*/
		float e = (float)(35 - (getYears() + (usedUnpaidVacation / 260)));
		return (int)e;
	}
}
