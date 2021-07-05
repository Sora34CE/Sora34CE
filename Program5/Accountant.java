
public class Accountant extends Employee {
	Accountant(int yearsWorked){ // constructor
		super(yearsWorked);
	}
	final int PAID_VACATION = 10; // days
	final int UNPAID_VACATION = 10; // days
	final double HEALTH_INSURANCE = 15000; // $/year
	final double INCOME = 125000; // $/year
	int usedUnpaidVacation; // keeps track of how much
	//unpaid vacation has been used.
	int usedVacation; // keeps track of how much paid vacation
	// has been used.
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
	double YTDValue() { 
		return (double)(HEALTH_INSURANCE + INCOME + PAID_VACATION * (INCOME / 260));
	}
	int yearsTillRetirement() { /* Overridden from
	Employee class. This should be rounded up to the nearest int.
	The calculation should take place as floating point arithmetic to
	ensure precision (hint: cast as double where necessary). This
	number should not be below zero. This is calculated by: 35 –
	(yearsWorked + (usedUnpaidVacation / 260) +
	((usedPaidVacation / 260) * 2))
	*/
		float c = (float)(35 - (getYears() + (usedUnpaidVacation / 260) + ((usedVacation / 260) * 2)));
		return (int)c;
	}
}
